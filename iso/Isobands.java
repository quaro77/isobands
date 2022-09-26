package iso;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import work.ExportPolygons;
import work.SingleCalc;
import work.Util;
import work.Visual;

public class Isobands {

	private static double minX = 1000.0;
	private static double maxX = -1000.0;
	private static double minY = 1000.0;
	private static double maxY = -1000.0;
	private static double minPga = 1000.0;
	private static double maxPga = -1000.0;

	private static Map<Double, Area> areaMap;

	private double[][] grid;
	private static double[] xAxis;
	private static double[] yAxis;

	private double minLevel;
	private double maxLevel;
	private double step;

	public void readCsv(String inputFile) {
		File f = new File(inputFile);
		FileReader fw = null;
		BufferedReader bw = null;

		try {
			fw = new FileReader(f);
			bw = new BufferedReader(fw);

			// trovo il numero di righe e colonne

			int numX = 1;
			int numY = 0;

			String yString = "";

			String s = "";
			while ((s = bw.readLine()) != null) {

				String[] riga = s.split(";");

				if (!yString.equals(riga[1])) {
					yString = riga[1];
					numY++;
					numX = 1;
				} else {
					numX++;
				}
			}

			bw.close();
			fw.close();

			System.out.println("Dimensione x: " + numX + "; Dimensione y: " + numY);

			grid = new double[numX][numY];
			xAxis = new double[numX];
			yAxis = new double[numY];

			fw = new FileReader(f);
			bw = new BufferedReader(fw);

			int i = 0;
			int j = -1;

			yString = "";

			s = "";
			while ((s = bw.readLine()) != null) {
				String[] riga = s.split(";");

				double x = Double.parseDouble(riga[0]);
				double y = Double.parseDouble(riga[1]);
				double val = Double.parseDouble(riga[2]);
				if (!yString.equals(riga[1])) {
					yString = riga[1];
					j++;
					i = 0;
				} else {
					i++;
				}
				xAxis[i] = x;
				yAxis[j] = y;

				if (x < minX) {
					minX = x;
				}
				if (x > maxX) {
					maxX = x;
				}
				if (y < minY) {
					minY = y;
				}
				if (y > maxY) {
					maxY = y;
				}
				if (val < minPga) {
					minPga = val;
				}
				if (val > maxPga) {
					maxPga = val;
				}

				grid[i][j] = val;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setMinLevel(double minLevel) {
		this.minLevel = minLevel;
	}

	public void setMaxLevel(double maxLevel) {
		this.maxLevel = maxLevel;
	}

	public void setStep(double step) {
		this.step = step;
	}

	private void calculateCurves() {

		long startTime = System.currentTimeMillis();

		if (this.maxLevel == 0.0) {
			if (step >= 1.0) {
				maxLevel = Math.ceil(maxPga);
			} else {
				maxLevel = maxPga;
			}
		}

		int nLevels = (int) Math.ceil((maxLevel - minLevel) / step) + 1;

		System.out.println("Number of levels: " + nLevels);

		double[] levels = new double[nLevels];

		levels[0] = minLevel;

		for (int i = 1; i < levels.length; i++) {
			levels[i] = levels[i - 1] + step;
		}

		areaMap = new HashMap<>();

		Thread[] tPool = new Thread[levels.length - 1];

		for (int lev = 0; lev < levels.length - 1; lev++) {

			double levelLow = levels[lev];
			double levelSup = levels[lev + 1];

			SingleCalc s = new SingleCalc();

			s.setup(areaMap, grid, xAxis, yAxis, levelLow, levelSup);

			tPool[lev] = new Thread(s);
			tPool[lev].start();
		}

		for (int i = 0; i < tPool.length; i++) {
			try {
				tPool[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// remove overlaps:
		for (int lev = levels.length - 3; lev >= 0; lev--) {
			Area up = areaMap.get(levels[lev + 1]);
			Area low = areaMap.get(levels[lev]);
			low.subtract(up);
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Calculation time: " + (endTime - startTime) / 1000.0 + " seconds.");

//		System.out.println("checking...");
//		Set<Double> ks = areaMap.keySet();
//		List<Double> ksList = new ArrayList<>(ks);
//		Collections.sort(ksList);
//		boolean[][] med = new boolean[grid.length - 1][grid[8].length - 1];
//		for (Double k : ks) {
//			Area a = areaMap.get(k);
//			for (int i = 0; i < xAxis.length - 1; i++) {
//				for (int j = 0; j < yAxis.length - 1; j++) {
//					double x = (xAxis[i] + xAxis[i + 1]) / 2.0 * Util.magnify;
//					double y = (yAxis[j] + yAxis[j + 1]) / 2.0 * Util.magnify;
//
//					if (a.contains(x, y)) {
//						med[i][j] = true;
//					}
//				}
//			}
//		}
//
//		for (int i = 0; i < med.length; i++) {
//			for (int j = 0; j < med[i].length; j++) {
//				if (med[i][j] == false) {
//					System.out.println("cell " + i + ", " + j + " is empty.");
//				}
//			}
//		}

	}

	public void exportCsv(String outputFile) {
		ExportPolygons.exportCsv(areaMap, outputFile);
	}

	public void exportShp(String outputFile) {
		ExportPolygons.exportShp(areaMap, minX, minY, maxX, maxY, outputFile);
	}

	public void draw() {
		Visual.draw(areaMap, minX, minY, maxX, maxY, minPga, maxPga, xAxis, yAxis);
	}

	public void mirrorY(boolean mirror) {
		Visual.mirrorY = mirror;
	}

	public static void main(String[] args) {

		Isobands i = new Isobands();
		i.setMinLevel(0.0);
//		i.setMaxLevel(70.0);
		i.setStep(0.01);

		i.readCsv("c:/testdir/myinputdata.txt");

		i.calculateCurves();
		i.mirrorY(true);
		i.draw();

		i.exportShp("c:/testdir/myoutputshakemap");

	}

}
