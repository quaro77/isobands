package work;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import shape.CreateDbf;
import shape.CreateIndex;
import shape.CreatePrj;
import shape.CreateShp;

public class ExportPolygons {

	public static void export(Map<Double, Area> map) {

		Set<Double> ks = map.keySet();
		List<Double> ksList = new ArrayList<>(ks);
		Collections.sort(ksList);

		for (double k : ksList) {

			Area a = map.get(k);

			System.out.println("PGA: " + k);

			PathIterator pi = a.getPathIterator(null);

			double[] coords = new double[6];

			while (!pi.isDone()) {

				int type = pi.currentSegment(coords);
				int wRule = pi.getWindingRule();

				System.out.print("type: " + type + " - wRule: " + wRule + " - coords: ");
				System.out.print("[" + coords[0] / Util.magnify + "," + coords[1] / Util.magnify + "],");
				System.out.print("[" + coords[2] / Util.magnify + "," + coords[3] / Util.magnify + "],");
				System.out.println("[" + coords[4] / Util.magnify + "," + coords[5] / Util.magnify + "]");

				pi.next();
			}

		}
	}

	public static void exportCsv(Map<Double, Area> map, String filename) {

		Set<Double> ks = map.keySet();
		List<Double> ksList = new ArrayList<>(ks);
		Collections.sort(ksList);

		File f = new File(filename);
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);

			String intest = "pga;wkt";

			bw.write(intest);
			bw.newLine();

			for (double k : ksList) {

				String riga = k + ";MULTIPOLYGON(";

				Area a = map.get(k);

				List<PolygonData> list = areaToMultiPolygon(a);

				for (PolygonData p : list) {

					riga += "((";

					for (int i = 0; i < p.nVert; i++) {
						riga += p.x.get(i) / Util.magnify + " " + p.y.get(i) / Util.magnify + ",";
					}
					if (riga.endsWith(",")) {
						riga = riga.substring(0, riga.length() - 1);
					}
					riga += "),";

					for (PolygonData h : p.holes) {
						riga += "(";
						for (int i = 0; i < h.nVert; i++) {
							riga += h.x.get(i) / Util.magnify + " " + h.y.get(i) / Util.magnify + ",";
						}
						if (riga.endsWith(",")) {
							riga = riga.substring(0, riga.length() - 1);
						}
						riga += "),";
					}
					if (riga.endsWith(",")) {
						riga = riga.substring(0, riga.length() - 1);
					}
					riga += "),";
				}
				if (riga.endsWith(",")) {
					riga = riga.substring(0, riga.length() - 1);
				}
				riga += ")";

				bw.write(riga);
				bw.newLine();

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

	public static void exportShp(Map<Double, Area> map, double minX, double minY, double maxX, double maxY, String filename) {

		Set<Double> ks = map.keySet();
		List<Double> ksList = new ArrayList<>(ks);
		Collections.sort(ksList);

		Map<Double, List<PolygonData>> mapOut = new HashMap<>();

		for (double k : ksList) {
			Area a = map.get(k);
			List<PolygonData> list = areaToMultiPolygon(a);
			mapOut.put(k, list);
		}

		deMagnify(mapOut);

		CreateShp cs = new CreateShp();
		cs.write(mapOut, filename);

		CreateIndex ci = new CreateIndex();
		ci.write(mapOut, filename);

		CreateDbf cd = new CreateDbf();
		cd.write(mapOut, filename);
		
		CreatePrj pj = new CreatePrj();
		pj.write(filename, "4326");

	}

	private static void deMagnify(Map<Double, List<PolygonData>> map) {

		for (Double k : map.keySet()) {
			for (PolygonData p : map.get(k)) {
				for (int i = 0; i < p.x.size(); i++) {
					p.x.set(i, p.x.get(i) / Util.magnify);
				}
				for (int i = 0; i < p.y.size(); i++) {
					p.y.set(i, p.y.get(i) / Util.magnify);
				}
				for (PolygonData h : p.holes) {
					for (int i = 0; i < h.x.size(); i++) {
						h.x.set(i, h.x.get(i) / Util.magnify);
					}
					for (int i = 0; i < h.y.size(); i++) {
						h.y.set(i, h.y.get(i) / Util.magnify);
					}
				}
			}
		}

	}

	private static List<PolygonData> areaToMultiPolygon(Area a) {

		PathIterator pi = a.getPathIterator(null);

		double[] coords = new double[6];

		List<PolygonData> list = new ArrayList<>();

		PolygonData p = new PolygonData();

		while (!pi.isDone()) {
			int type = pi.currentSegment(coords);

			if (type != 4) {
				p.x.add(coords[0]);
				p.y.add(coords[1]);
				p.shape.addPoint((int) Math.round(coords[0]), (int) Math.round(coords[1]));
				p.nVert++;
			} else {
				p.x.add(coords[0]);
				p.y.add(coords[1]);
				p.shape.addPoint((int) Math.round(coords[0]), (int) Math.round(coords[1]));
				p.nVert++;

				if (p.x.get(0) != coords[0] || p.y.get(0) != coords[1]) {
					p.x.add(p.x.get(0));
					p.y.add(p.y.get(0));
					p.shape.addPoint((int) Math.round(p.x.get(0)), (int) Math.round(p.y.get(0)));
					p.nVert++;
				}

				list.add(p);
				p = new PolygonData();
			}
			pi.next();
		}

		for (PolygonData p1 : list) {
			for (PolygonData p2 : list) {
				if (p1 != p2 && p2.shape.contains(p1.x.get(0), p1.y.get(0))) {
					p1.hole = true;
					p2.holes.add(p1);
				}
			}
		}

		List<PolygonData> out = new ArrayList<>();

		for (PolygonData p1 : list) {
			if (!p1.hole) {
				out.add(p1);
			}
		}

		return out;
	}

}