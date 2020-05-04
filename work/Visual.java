package work;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visual {

	private static double minX = 1000.0;
	private static double maxX = -1000.0;
	private static double minY = 1000.0;
	private static double maxY = -1000.0;
	private static double minPga = 1000.0;
	private static double maxPga = -1000.0;

	private static double[] xAxis;
	private static double[] yAxis;

	private static JFrame frame;
	private static JPanel inner;
	private static String label = "";
	private static Set<Double> selected;
	private static Map<Double, Area> areaMap;
	private static AffineTransform oldXForm;

	private static double zoomW = 1.0;
	private static double zoomExtent = 1.0;

	private static double startXPress = 0.0;
	private static double startYPress = 0.0;
	private static double startX = 0.0;
	private static double startY = 0.0;

	public static boolean mirrorY = false;

	public static void draw(Map<Double, Area> areaMapL, double minXL, double minYL, double maxXL, double maxYL, double minPgaL, double maxPgaL, double[] xAxisL, double[] yAxisL) {

		areaMap = areaMapL;
		minX = minXL;
		minY = minYL;
		maxX = maxXL;
		maxY = maxYL;
		minPga = minPgaL;
		maxPga = maxPgaL;
		xAxis = xAxisL;
		yAxis = yAxisL;

		frame = new JFrame();

		selected = new HashSet<>();

		frame.setTitle("Shakemap");
		frame.setSize(900, 760);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		double dimX = (maxX - minX) * Util.magnify;
		double dimY = (maxY - minY) * Util.magnify;
		double zoomX = 900 / dimX;
		double zoomY = 760 / dimY;
		zoomExtent = Math.min(zoomX, zoomY);

		inner = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;

				g2.setColor(Color.DARK_GRAY);
				g2.fillRect(0, 0, 900, 760);

				Stroke thinStroke = new BasicStroke(1.0f);
				Stroke wideStroke = new BasicStroke(2.0f);

				double dimPga = maxPga - minPga;

				double zoom = zoomExtent * zoomW;

				if (oldXForm == null) {
					oldXForm = g2.getTransform();
				} else {
					g2.setTransform(oldXForm);
				}

				Color c1 = Color.GREEN;
				Color c2 = Color.RED;

				Set<Double> ks = areaMap.keySet();
				List<Double> ksList = new ArrayList<>(ks);
				Collections.sort(ksList);

				Area area;
				Area areaB;

				for (Double k : ks) {

					double percPga = (k - minPga) / dimPga;

					percPga = Math.max(percPga, 0.0);

					float ratio = (float) percPga;
					int red = (int) (c2.getRed() * ratio + c1.getRed() * (1 - ratio));
					int green = (int) (c2.getGreen() * ratio +
							c1.getGreen() * (1 - ratio));
					int blue = (int) (c2.getBlue() * ratio +
							c1.getBlue() * (1 - ratio));
					Color c = new Color(red, green, blue);
					g2.setColor(c);
					area = areaMap.get(k);

					if (mirrorY) {
						areaB = area.createTransformedArea(AffineTransform.getScaleInstance(zoom, -zoom));
						areaB.transform(AffineTransform.getTranslateInstance(-minX * Util.magnify * zoom + startX, minY * Util.magnify * zoom + startY + 700));
					} else {
						areaB = area.createTransformedArea(AffineTransform.getScaleInstance(zoom, zoom));
						areaB.transform(AffineTransform.getTranslateInstance(-minX * Util.magnify * zoom + startX, -minY * Util.magnify * zoom + startY));
					}

					g2.fill(areaB);

					g2.setStroke(thinStroke);
					g2.setColor(Color.BLUE);

					g2.draw(areaB);

				}

				for (Double k : selected) {
					area = areaMap.get(k);

					if (mirrorY) {
						areaB = area.createTransformedArea(AffineTransform.getScaleInstance(zoom, -zoom));
						areaB.transform(AffineTransform.getTranslateInstance(-minX * Util.magnify * zoom + startX, minY * Util.magnify * zoom + startY + 700));
					} else {
						areaB = area.createTransformedArea(AffineTransform.getScaleInstance(zoom, zoom));
						areaB.transform(AffineTransform.getTranslateInstance(-minX * Util.magnify * zoom + startX, -minY * Util.magnify * zoom + startY));
					}
					g2.setColor(Color.YELLOW);
					g2.fill(areaB);

					g2.setStroke(wideStroke);
					g2.setColor(Color.RED);
					g2.draw(areaB);
				}

				g2.setColor(Color.WHITE);
				g2.drawString(label, 10, 720);

			};
		};

		addListeners();

		frame.add(inner);
		frame.repaint();

	}

	private static void addListeners() {
		inner.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				startX = e.getX() - startXPress;
				startY = e.getY() - startYPress;
				inner.repaint();
			}
		});

		inner.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {

					double[] coords = screenToCoords(e.getX(), e.getY());

					zoomW += 0.1;

					startX = 0;
					startY = 0;

					int[] screen = coordsToScreen(coords[0], coords[1]);
					int xGap = screen[0] - e.getX();
					int yGap = screen[1] - e.getY();
					startX = -xGap;
					startY = -yGap;

					inner.repaint();
				} else if (e.getWheelRotation() > 0) {
					if (zoomW > 0.1) {

						double[] coords = screenToCoords(e.getX(), e.getY());

						zoomW -= 0.1;

						startX = 0;
						startY = 0;

						int[] screen = coordsToScreen(coords[0], coords[1]);
						int xGap = screen[0] - e.getX();
						int yGap = screen[1] - e.getY();
						startX = -xGap;
						startY = -yGap;

						inner.repaint();
					}
				}

			}
		});

		inner.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				startXPress = e.getX() - startX;
				startYPress = e.getY() - startY;
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getButton() == MouseEvent.BUTTON3) {
					label = "";
					selected = new HashSet<>();
					inner.repaint();
					return;
				}

				double[] coords = screenToCoords(e.getX(), e.getY());

				double x = coords[0];
				double y = coords[1];

				coordsToScreen(x, y);

				int indX = -1;
				int indY = -1;

				for (int i = 0; i < xAxis.length - 1; i++) {
					if (x >= xAxis[i] && x <= xAxis[i + 1]) {
						indX = i;
					}
				}
				for (int i = 0; i < yAxis.length - 1; i++) {
					if (y >= yAxis[i] && y <= yAxis[i + 1] || y >= yAxis[i + 1] && y <= yAxis[i]) {
						indY = i;
					}
				}

				selected = new HashSet<>();

				double xM = x * Util.magnify;
				double yM = y * Util.magnify;

				for (Double k : areaMap.keySet()) {
					Area a = areaMap.get(k);

					if (a.contains(xM, yM)) {
						selected.add(k);
					}
				}

				label = Math.round(x * 1000.0) / 1000.0 + ", " + Math.round(y * 1000.0) / 1000.0 + " - [" + indX + ", " + indY + "]";
				if (!selected.isEmpty()) {
					label += " - Selected: ";
					for (Double d : selected) {
						label += d + "; ";
					}
				}
				inner.repaint();

			}
		});

		inner.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// progress = true;
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

	}

	private static double[] screenToCoords(int x, int y) {

		double[] out = new double[2];
		double zoom = zoomExtent * zoomW * Util.magnify;
		out[0] = minX + (x - startX) / zoom;

		if (mirrorY) {
			out[1] = minY + (700 - y + startY) / zoom;
		} else {
			out[1] = minY + (y - startY) / zoom;
		}

		// System.out.println("Screen to coords: [ " + x + ", " + y + " ] -> [ " + out[0] + ", " + out[1] + " ]");

		return out;
	}

	private static int[] coordsToScreen(double x, double y) {

		int[] out = new int[2];
		double zoom = zoomExtent * zoomW * Util.magnify;
		out[0] = (int) Math.round((x - minX) * zoom + startX);

		if (mirrorY) {
			out[1] = (int) Math.round(-(y - minY) * zoom + startY + 700);
		} else {
			out[1] = (int) Math.round((y - minY) * zoom + startY);
		}

		// System.out.println("Coords to screen: [ " + x + ", " + y + " ] -> [ " + out[0] + ", " + out[1] + " ]");

		return out;
	}

}
