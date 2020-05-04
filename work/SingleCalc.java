package work;

import java.awt.Polygon;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleCalc implements Runnable {

	private static Object lock = new Object();

	private Map<Double, Area> areaMap;
	private double[][] grid;
	private double[] xAxis;
	private double[] yAxis;
	private double levelLow;
	private double levelSup;

	public void setup(Map<Double, Area> areaMap, double[][] grid, double[] xAxis, double[] yAxis, double levelLow, double levelSup) {
		this.areaMap = areaMap;
		this.grid = grid;
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.levelLow = levelLow;
		this.levelSup = levelSup;
	}

	private Area calculateLevel() {
		System.out.println("Level " + levelLow + " - " + levelSup);

		Area areaTot = new Area();

		for (int i = 0; i < grid.length - 1; i++) {
			for (int j = 0; j < grid[i].length - 1; j++) {

				double[] cell = new double[4];

				cell[0] = grid[i][j];
				cell[1] = grid[i + 1][j];
				cell[2] = grid[i + 1][j + 1];
				cell[3] = grid[i][j + 1];

				String index = "" + eval(cell[0], levelLow, levelSup) + eval(cell[1], levelLow, levelSup) + eval(cell[2], levelLow, levelSup) + eval(cell[3], levelLow, levelSup);

				int internal = evalInternal(cell, levelLow, levelSup);

//				if (i == 131 && j == 84) {
//					System.out.println("");
//				}

				List<List<Point>> vertexes = PolyCells.getPoints(index, xAxis[i], yAxis[j], xAxis[i + 1] - xAxis[i], yAxis[j + 1] - yAxis[j], internal);

				for (int ix = 0; ix < vertexes.size(); ix++) {
					Polygon poly = new Polygon();
					for (Point p : vertexes.get(ix)) {
						int x = (int) (Math.round(p.x * Util.magnify));
						int y = (int) (Math.round(p.y * Util.magnify));
						poly.addPoint(x, y);
					}
					Area area = new Area(poly);
					areaTot.add(area);

				}
			}
		}

		System.out.println("Finish - Level " + levelLow + " - " + levelSup);

		return areaTot;
	}

	private static int eval(double val, double limLow, double limSup) {
		if (val < limLow) {
			return 0;
		}
		if (val >= limSup) {
			return 2;
		}
		return 1;
	}

	private static int evalInternal(double[] cell, double limLow, double limSup) {
		double media = (cell[0] + cell[1] + cell[2] + cell[3]) / 4.0;
		return eval(media, limLow, limSup);
	}

	public void run() {

		Area a = calculateLevel();

		synchronized (lock) {
			areaMap.put(this.levelLow, a);
		}

	}

}
