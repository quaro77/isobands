package work;

import java.util.ArrayList;
import java.util.List;

public class PolyCells {

	// index: indice di tipologia della cella
	// xStart, yStart: coordinate iniziali della cella
	// dimY, dimY: dimensioni della cella
	// internal: valore del punto interno (0: below, 1: inside; 2: above)
	static List<List<Point>> getPoints(String index, double xStart, double yStart, double dimX, double dimY, int internal) {

		List<Point> out = new ArrayList<>();
		List<Point> out2 = null;

		switch (index) {
		// empty:
		case "2222":
		case "0000":
			if (internal == 1) {
				out.add(new Point(0.4, 0));
				out.add(new Point(0.6, 0));
				out.add(new Point(1, 0.4));
				out.add(new Point(1, 0.6));
				out.add(new Point(0.6, 1));
				out.add(new Point(0.4, 1));
				out.add(new Point(0, 0.6));
				out.add(new Point(0, 0.4));
			}
			break;

		// full:
		case "1111":
			out.add(new Point(0, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 1));
			out.add(new Point(0, 1));
			break;

		// single triangle:
		case "2221":
		case "0001":
			out.add(new Point(0, 0.5));
			out.add(new Point(0.5, 1));
			out.add(new Point(0, 1));
			break;
		case "2212":
		case "0010":
			out.add(new Point(0.5, 1));
			out.add(new Point(1, 0.5));
			out.add(new Point(1, 1));
			break;
		case "2122":
		case "0100":
			out.add(new Point(0.5, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 0.5));
			break;
		case "1222":
		case "1000":
			out.add(new Point(0, 0));
			out.add(new Point(0.5, 0));
			out.add(new Point(0, 0.5));
			break;

		// single trapezoid:
		case "2220":
		case "0002":
			if (internal == 1) {
				out.add(new Point(0.4, 0));
				out.add(new Point(0.6, 0));
				out.add(new Point(1, 0.4));
				out.add(new Point(1, 0.6));
				out.add(new Point(0.6, 1));
				out.add(new Point(0.4, 1));
				out.add(new Point(0, 0.6));
				out.add(new Point(0, 0.4));
			}
			break;
		case "2202":
		case "0020":
			if (internal == 1) {
				out.add(new Point(0.4, 0));
				out.add(new Point(0.6, 0));
				out.add(new Point(1, 0.4));
				out.add(new Point(1, 0.6));
				out.add(new Point(0.6, 1));
				out.add(new Point(0.4, 1));
				out.add(new Point(0, 0.6));
				out.add(new Point(0, 0.4));
			}
			break;
		case "2022":
		case "0200":
			if (internal == 1) {
				out.add(new Point(0.4, 0));
				out.add(new Point(0.6, 0));
				out.add(new Point(1, 0.4));
				out.add(new Point(1, 0.6));
				out.add(new Point(0.6, 1));
				out.add(new Point(0.4, 1));
				out.add(new Point(0, 0.6));
				out.add(new Point(0, 0.4));
			}
			break;
		case "0222":
		case "2000":
			if (internal == 1) {
				out.add(new Point(0.4, 0));
				out.add(new Point(0.6, 0));
				out.add(new Point(1, 0.4));
				out.add(new Point(1, 0.6));
				out.add(new Point(0.6, 1));
				out.add(new Point(0.4, 1));
				out.add(new Point(0, 0.6));
				out.add(new Point(0, 0.4));
			}
			break;
		// single rectangle
		case "0011":
		case "2211":
			out.add(new Point(0, 0.5));
			out.add(new Point(1, 0.5));
			out.add(new Point(1, 1));
			out.add(new Point(0, 1));
			break;
		case "0110":
		case "2112":
			out.add(new Point(0.5, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 1));
			out.add(new Point(0.5, 1));
			break;
		case "1100":
		case "1122":
			out.add(new Point(0, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 0.5));
			out.add(new Point(0, 0.5));
			break;
		case "1001":
		case "1221":
			out.add(new Point(0, 0));
			out.add(new Point(0.5, 0));
			out.add(new Point(0.5, 1));
			out.add(new Point(0, 1));
			break;
		case "2200":
		case "0022":
//			out.add(new Point(0, 0.4));
//			out.add(new Point(1, 0.4));
//			out.add(new Point(1, 0.6));
//			out.add(new Point(0, 0.6));
			break;
		case "2002":
		case "0220":
//			out.add(new Point(0.4, 0));
//			out.add(new Point(0.6, 0));
//			out.add(new Point(0.6, 1));
//			out.add(new Point(0.4, 1));
			break;
		// single pentagon
		case "1211":
		case "1011":
			out.add(new Point(0, 0));
			out.add(new Point(0.5, 0));
			out.add(new Point(1, 0.5));
			out.add(new Point(1, 1));
			out.add(new Point(0, 1));
			break;
		case "2111":
		case "0111":
			out.add(new Point(0, 0.5));
			out.add(new Point(0.5, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 1));
			out.add(new Point(0, 1));
			break;
		case "1112":
		case "1110":
			out.add(new Point(0, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 1));
			out.add(new Point(0.5, 1));
			out.add(new Point(0, 0.5));
			break;
		case "1121":
		case "1101":
			out.add(new Point(0, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 0.5));
			out.add(new Point(0.5, 1));
			out.add(new Point(0, 1));
			break;
		case "1200":
		case "1022":
			out.add(new Point(0, 0));
			out.add(new Point(0.6, 0));
			out.add(new Point(1, 0.4));
			out.add(new Point(1, 0.6));
			out.add(new Point(0, 0.6));
			break;
		case "0120":
		case "2102":
			out.add(new Point(0.4, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 0.6));
			out.add(new Point(0.6, 1));
			out.add(new Point(0.4, 1));
			break;
		case "0012":
		case "2210":
			out.add(new Point(0, 0.4));
			out.add(new Point(1, 0.4));
			out.add(new Point(1, 1));
			out.add(new Point(0.4, 1));
			out.add(new Point(0, 0.6));
			break;
		case "2001":
		case "0221":
			out.add(new Point(0.4, 0));
			out.add(new Point(0.6, 0));
			out.add(new Point(0.6, 1));
			out.add(new Point(0, 1));
			out.add(new Point(0, 0.4));
			break;
		case "1002":
		case "1220":
			out.add(new Point(0, 0));
			out.add(new Point(0.6, 0));
			out.add(new Point(0.6, 1));
			out.add(new Point(0.4, 1));
			out.add(new Point(0, 0.6));
			break;
		case "2100":
		case "0122":
			out.add(new Point(0.4, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 0.6));
			out.add(new Point(0, 0.6));
			out.add(new Point(0, 0.4));
			break;
		case "0210":
		case "2012":
			out.add(new Point(0.4, 0));
			out.add(new Point(0.6, 0));
			out.add(new Point(1, 0.4));
			out.add(new Point(1, 1));
			out.add(new Point(0.4, 1));
			break;
		case "0021":
		case "2201":
			out.add(new Point(0, 0.4));
			out.add(new Point(1, 0.4));
			out.add(new Point(1, 0.6));
			out.add(new Point(0.6, 1));
			out.add(new Point(0, 1));
			break;
		// single hexagon
		case "0211":
		case "2011":
			out.add(new Point(0.4, 0));
			out.add(new Point(0.6, 0));
			out.add(new Point(1, 0.4));
			out.add(new Point(1, 1));
			out.add(new Point(0, 1));
			out.add(new Point(0, 0.4));
			break;
		case "2110":
		case "0112":
			out.add(new Point(0, 0.4));
			out.add(new Point(0.4, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 1));
			out.add(new Point(0.4, 1));
			out.add(new Point(0, 0.6));
			break;
		case "1102":
		case "1120":
			out.add(new Point(0, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 0.6));
			out.add(new Point(0.6, 1));
			out.add(new Point(0.4, 1));
			out.add(new Point(0, 0.6));
			break;
		case "1021":
		case "1201":
			out.add(new Point(0, 0));
			out.add(new Point(0.6, 0));
			out.add(new Point(1, 0.4));
			out.add(new Point(1, 0.6));
			out.add(new Point(0.6, 1));
			out.add(new Point(0, 1));
			break;
		case "2101":
		case "0121":
			out.add(new Point(0.4, 0));
			out.add(new Point(1, 0));
			out.add(new Point(1, 0.6));
			out.add(new Point(0.6, 1));
			out.add(new Point(0, 1));
			out.add(new Point(0, 0.4));
			break;
		case "1012":
		case "1210":
			out.add(new Point(0, 0));
			out.add(new Point(0.6, 0));
			out.add(new Point(1, 0.4));
			out.add(new Point(1, 1));
			out.add(new Point(0.4, 1));
			out.add(new Point(0, 0.6));
			break;
		// saddles:
		case "2020":
		case "0202":
			if (internal == 1) {
				out.add(new Point(0.4, 0));
				out.add(new Point(0.6, 0));
				out.add(new Point(1, 0.4));
				out.add(new Point(1, 0.6));
				out.add(new Point(0.6, 1));
				out.add(new Point(0.4, 1));
				out.add(new Point(0, 0.6));
				out.add(new Point(0, 0.4));
			}
			break;
		case "0101":
		case "2121":
			if (internal == 1) {
				out.add(new Point(0.4, 0));
				out.add(new Point(1, 0));
				out.add(new Point(1, 0.6));
				out.add(new Point(0.6, 1));
				out.add(new Point(0, 1));
				out.add(new Point(0, 0.4));
			} else {
				out.add(new Point(0.5, 0));
				out.add(new Point(1, 0));
				out.add(new Point(1, 0.5));

				out2 = new ArrayList<>();
				out2.add(new Point(0, 0.5));
				out2.add(new Point(0.5, 1));
				out2.add(new Point(0, 1));
			}
			break;
		case "1010":
		case "1212":
			if (internal == 1) {
				out.add(new Point(0, 0));
				out.add(new Point(0.6, 0));
				out.add(new Point(1, 0.4));
				out.add(new Point(1, 1));
				out.add(new Point(0.4, 1));
				out.add(new Point(0, 0.6));
			} else {
				out.add(new Point(0, 0));
				out.add(new Point(0.5, 0));
				out.add(new Point(0, 0.5));

				out2 = new ArrayList<>();
				out2.add(new Point(1, 0.5));
				out2.add(new Point(1, 1));
				out2.add(new Point(0.5, 1));
			}
			break;
		case "2120":
		case "0102":
			if (internal == 1) {
				out.add(new Point(0.4, 0));
				out.add(new Point(1, 0));
				out.add(new Point(1, 0.6));
				out.add(new Point(0.6, 1));
				out.add(new Point(0.4, 1));
				out.add(new Point(0, 0.6));
				out.add(new Point(0, 0.4));
			} else {
				out.add(new Point(0.5, 0));
				out.add(new Point(1, 0));
				out.add(new Point(1, 0.5));
			}
			break;
		case "2021":
		case "0201":
			if (internal == 1) {
				out.add(new Point(0.4, 0));
				out.add(new Point(0.6, 0));
				out.add(new Point(1, 0.4));
				out.add(new Point(1, 0.6));
				out.add(new Point(0.6, 1));
				out.add(new Point(0, 1));
				out.add(new Point(0, 0.4));
			} else {
				out.add(new Point(0, 0.5));
				out.add(new Point(0.5, 1));
				out.add(new Point(0, 1));
			}
			break;
		case "1202":
		case "1020":
			if (internal == 1) {
				out.add(new Point(0, 0));
				out.add(new Point(0.6, 0));
				out.add(new Point(1, 0.4));
				out.add(new Point(1, 0.6));
				out.add(new Point(0.6, 1));
				out.add(new Point(0.4, 1));
				out.add(new Point(0, 0.6));
			} else {
				out.add(new Point(0, 0));
				out.add(new Point(0.5, 0));
				out.add(new Point(0, 0.5));
			}
			break;
		case "0212":
		case "2010":
			if (internal == 1) {
				out.add(new Point(0.4, 0));
				out.add(new Point(0.6, 0));
				out.add(new Point(1, 0.4));
				out.add(new Point(1, 1));
				out.add(new Point(0.4, 1));
				out.add(new Point(0, 0.6));
				out.add(new Point(0, 0.4));
			} else {
				out.add(new Point(0.5, 1));
				out.add(new Point(1, 0.5));
				out.add(new Point(1, 1));
			}
			break;
		default:
			System.out.println("polygon not found at: " + xStart + "," + yStart);
			break;
		}

		List<List<Point>> outerOut = new ArrayList<>();

		for (Point p : out) {
			p.x = p.x * dimX + xStart;
			p.y = p.y * dimY + yStart;
		}
		outerOut.add(out);

		if (out2 != null) {
			for (Point p : out2) {
				p.x = p.x * dimX + xStart;
				p.y = p.y * dimY + yStart;
			}
			outerOut.add(out2);
		}

		return outerOut;

	}

}
