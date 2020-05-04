package shape;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import work.Point;
import work.PolygonData;

public class CreateShp {

	public void read(String filename) {

		File f = new File(filename);

		if (!f.exists()) {
			System.out.println("File inesistente.");
			return;
		}
		InputStream is = null;
		ByteArrayOutputStream os = null;

		try {
			is = new FileInputStream(f);
			os = new ByteArrayOutputStream();

			byte[] buffer = new byte[4096];
			int read = 0;
			while ((read = is.read(buffer)) != -1) {
				os.write(buffer, 0, read);
			}

			byte[] fileBytes = os.toByteArray();

			is.close();

			byte[] bytes = new byte[4];

			bytes[0] = fileBytes[0];
			bytes[1] = fileBytes[1];
			bytes[2] = fileBytes[2];
			bytes[3] = fileBytes[3];

			System.out.println("header -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.BIG_ENDIAN));

			int index = 4;
			for (int i = 0; i < 5; i++) {
				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				System.out.println("unused -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.BIG_ENDIAN));
			}

			bytes[0] = fileBytes[index++];
			bytes[1] = fileBytes[index++];
			bytes[2] = fileBytes[index++];
			bytes[3] = fileBytes[index++];
			System.out.println("file length -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.BIG_ENDIAN));

			bytes[0] = fileBytes[index++];
			bytes[1] = fileBytes[index++];
			bytes[2] = fileBytes[index++];
			bytes[3] = fileBytes[index++];
			System.out.println("version -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.LITTLE_ENDIAN));

			bytes[0] = fileBytes[index++];
			bytes[1] = fileBytes[index++];
			bytes[2] = fileBytes[index++];
			bytes[3] = fileBytes[index++];
			System.out.println("shape type -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.LITTLE_ENDIAN));

			bytes = new byte[8];
			for (int i = 0; i < 8; i++) {
				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				bytes[4] = fileBytes[index++];
				bytes[5] = fileBytes[index++];
				bytes[6] = fileBytes[index++];
				bytes[7] = fileBytes[index++];
				System.out.println("BBOX -> " + ShapeUtil.decodeDouble(bytes, ByteOrder.LITTLE_ENDIAN));
			}
			System.out.println(index);

			int n = 0;
			boolean done = false;

			while (index < fileBytes.length && !done) {

				// RECORD HEADER
				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				System.out.println("record num -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.BIG_ENDIAN));

				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				System.out.println("content length -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.BIG_ENDIAN));

				// RECORD
				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				System.out.println("shape type -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.LITTLE_ENDIAN));

				for (int i = 0; i < 4; i++) {
					bytes[0] = fileBytes[index++];
					bytes[1] = fileBytes[index++];
					bytes[2] = fileBytes[index++];
					bytes[3] = fileBytes[index++];
					bytes[4] = fileBytes[index++];
					bytes[5] = fileBytes[index++];
					bytes[6] = fileBytes[index++];
					bytes[7] = fileBytes[index++];
					System.out.println("BBOX -> " + ShapeUtil.decodeDouble(bytes, ByteOrder.LITTLE_ENDIAN));
				}
				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				System.out.println("numRings -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.LITTLE_ENDIAN));

//				if (ShapeUtil.decodeInteger(bytes, ByteOrder.LITTLE_ENDIAN) > 1) {
//					done = true;
//				}

				int numRings = ShapeUtil.decodeInteger(bytes, ByteOrder.LITTLE_ENDIAN);

				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				System.out.println("numPoints -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.LITTLE_ENDIAN));

				int numPoints = ShapeUtil.decodeInteger(bytes, ByteOrder.LITTLE_ENDIAN);

				if (numPoints > 200) {
					done = false;
				}

				for (int i = 0; i < numRings; i++) {
					bytes[0] = fileBytes[index++];
					bytes[1] = fileBytes[index++];
					bytes[2] = fileBytes[index++];
					bytes[3] = fileBytes[index++];
					System.out.println("index -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.LITTLE_ENDIAN));
				}

				for (int i = 0; i < numPoints; i++) {
					bytes[0] = fileBytes[index++];
					bytes[1] = fileBytes[index++];
					bytes[2] = fileBytes[index++];
					bytes[3] = fileBytes[index++];
					bytes[4] = fileBytes[index++];
					bytes[5] = fileBytes[index++];
					bytes[6] = fileBytes[index++];
					bytes[7] = fileBytes[index++];
					System.out.println("X -> " + ShapeUtil.decodeDouble(bytes, ByteOrder.LITTLE_ENDIAN));
					bytes[0] = fileBytes[index++];
					bytes[1] = fileBytes[index++];
					bytes[2] = fileBytes[index++];
					bytes[3] = fileBytes[index++];
					bytes[4] = fileBytes[index++];
					bytes[5] = fileBytes[index++];
					bytes[6] = fileBytes[index++];
					bytes[7] = fileBytes[index++];
					System.out.println("Y -> " + ShapeUtil.decodeDouble(bytes, ByteOrder.LITTLE_ENDIAN));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void write(Map<Double, List<PolygonData>> map, String filename) {

		System.out.println("Starting SHP file creation...");

		List<Byte> byteList = new LinkedList<>();

		double[] bbox = ShapeUtil.findBBox(map);

		// file header
		int fileDim = writeFileHeader(byteList, bbox[0], bbox[1], bbox[2], bbox[3]);

		// records
		Set<Double> ks = map.keySet();
		List<Double> ksList = new ArrayList<>(ks);
		Collections.sort(ksList);

		int record = 1;
		for (Double k : ksList) {
			System.out.println("index of " + record + " record:" + fileDim);
			List<PolygonData> polys = map.get(k);
			for (PolygonData poly : polys) {
				fileDim += writeRecord(byteList, record, poly);
				record++;
			}
		}

		byte[] ba = ShapeUtil.encodeInteger(fileDim / 2, ByteOrder.BIG_ENDIAN); // file length in 16bit words
		ShapeUtil.putBytes(ba, byteList, 24);

		File f = new File(filename + ".shp");
		OutputStream os = null;

		try {
			os = new FileOutputStream(f);
			for (Byte b : byteList) {
				os.write(b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("SHP file successfully created.");

	}

	public int writeFileHeader(List<Byte> byteList, double minX, double minY, double maxX, double maxY) {
		byte[] ba = ShapeUtil.encodeInteger(9994, ByteOrder.BIG_ENDIAN); // file code: 9994
		ShapeUtil.addBytes(ba, byteList);

		ba = ShapeUtil.encodeInteger(0, ByteOrder.BIG_ENDIAN); // 5 zeri
		ShapeUtil.addBytes(ba, byteList);
		ShapeUtil.addBytes(ba, byteList);
		ShapeUtil.addBytes(ba, byteList);
		ShapeUtil.addBytes(ba, byteList);
		ShapeUtil.addBytes(ba, byteList);

		ba = ShapeUtil.encodeInteger(0, ByteOrder.BIG_ENDIAN); // file length (sovrascritto dopo)
		ShapeUtil.addBytes(ba, byteList);

		ba = ShapeUtil.encodeInteger(1000, ByteOrder.LITTLE_ENDIAN); // version 1000
		ShapeUtil.addBytes(ba, byteList);

		ba = ShapeUtil.encodeInteger(5, ByteOrder.LITTLE_ENDIAN); // shapetype (5 = polygon)
		ShapeUtil.addBytes(ba, byteList);

		ba = ShapeUtil.encodeDouble(minX, ByteOrder.LITTLE_ENDIAN); // x min
		ShapeUtil.addBytes(ba, byteList);
		ba = ShapeUtil.encodeDouble(minY, ByteOrder.LITTLE_ENDIAN); // y min
		ShapeUtil.addBytes(ba, byteList);
		ba = ShapeUtil.encodeDouble(maxX, ByteOrder.LITTLE_ENDIAN); // x max
		ShapeUtil.addBytes(ba, byteList);
		ba = ShapeUtil.encodeDouble(maxY, ByteOrder.LITTLE_ENDIAN); // y max
		ShapeUtil.addBytes(ba, byteList);
		ba = ShapeUtil.encodeDouble(0.0, ByteOrder.LITTLE_ENDIAN); // z min, z max, m min, m max (0.0)
		ShapeUtil.addBytes(ba, byteList);
		ShapeUtil.addBytes(ba, byteList);
		ShapeUtil.addBytes(ba, byteList);
		ShapeUtil.addBytes(ba, byteList);

		return byteList.size();
	}

	public int writeRecord(List<Byte> byteList, int record, PolygonData p) {

		byte[] ba = ShapeUtil.encodeInteger(record, ByteOrder.BIG_ENDIAN); // record number
		ShapeUtil.addBytes(ba, byteList);

		int lengthIndex = byteList.size();

		ba = ShapeUtil.encodeInteger(0, ByteOrder.BIG_ENDIAN); // record length (sovrascritto dopo)
		ShapeUtil.addBytes(ba, byteList);

		int recordDim = 8;

		double[] d = findPolyDetails(p);

		ba = ShapeUtil.encodeInteger(5, ByteOrder.LITTLE_ENDIAN); // shape type
		ShapeUtil.addBytes(ba, byteList);

		ba = ShapeUtil.encodeDouble(d[0], ByteOrder.LITTLE_ENDIAN); // bbox
		ShapeUtil.addBytes(ba, byteList);
		ba = ShapeUtil.encodeDouble(d[1], ByteOrder.LITTLE_ENDIAN);
		ShapeUtil.addBytes(ba, byteList);
		ba = ShapeUtil.encodeDouble(d[2], ByteOrder.LITTLE_ENDIAN);
		ShapeUtil.addBytes(ba, byteList);
		ba = ShapeUtil.encodeDouble(d[3], ByteOrder.LITTLE_ENDIAN);
		ShapeUtil.addBytes(ba, byteList);
		ba = ShapeUtil.encodeInteger((int) d[4], ByteOrder.LITTLE_ENDIAN); // nRings
		ShapeUtil.addBytes(ba, byteList);
		ba = ShapeUtil.encodeInteger((int) d[5], ByteOrder.LITTLE_ENDIAN); // nPoints
		ShapeUtil.addBytes(ba, byteList);

		recordDim += 44;

		int pointIndex = 0;
		List<Point> pointList = new ArrayList<>();

		ba = ShapeUtil.encodeInteger(pointIndex, ByteOrder.LITTLE_ENDIAN);
		ShapeUtil.addBytes(ba, byteList);
		recordDim += 4;
		for (int i = 0; i < p.nVert; i++) {
			Point pt = new Point(p.x.get(i), p.y.get(i));
			pointList.add(pt);
			pointIndex++;
		}

		for (PolygonData h : p.holes) {
			ba = ShapeUtil.encodeInteger(pointIndex, ByteOrder.LITTLE_ENDIAN);
			ShapeUtil.addBytes(ba, byteList);

			recordDim += 4;
			for (int i = 0; i < h.nVert; i++) {
				Point pt = new Point(h.x.get(i), h.y.get(i));
				pointList.add(pt);
				pointIndex++;
			}
		}

		for (Point pt : pointList) {
			ba = ShapeUtil.encodeDouble(pt.x, ByteOrder.LITTLE_ENDIAN);
			ShapeUtil.addBytes(ba, byteList);
			ba = ShapeUtil.encodeDouble(pt.y, ByteOrder.LITTLE_ENDIAN);
			ShapeUtil.addBytes(ba, byteList);
			recordDim += 16;
		}

		ba = ShapeUtil.encodeInteger((recordDim - 8) / 2, ByteOrder.BIG_ENDIAN); // record length in 16bit words
		ShapeUtil.putBytes(ba, byteList, lengthIndex);

		return recordDim;
	}

	/* utility */

	/*
	 * 0 -> BBox xMin 1 -> BBox yMin 2 -> BBox xMax 3 -> BBox yMax 4 -> nRings 5 ->
	 * nPoints
	 * 
	 */
	public double[] findPolyDetails(PolygonData p) {
		double[] out = new double[6];

		double xMin = 1000.0;
		double yMin = 1000.0;
		double xMax = -1000.0;
		double yMax = -1000.0;

		int nRings = 0;
		int nPoints = 0;

		nRings++;
		
		for (double x : p.x) {
			if (x < xMin) {
				xMin = x;
			} else if (x > xMax) {
				xMax = x;
			}
			nPoints++;
		}
		for (double y : p.y) {
			if (y < yMin) {
				yMin = y;
			} else if (y > yMax) {
				yMax = y;
			}
		}
		for (PolygonData h : p.holes) {
			
			nRings++;
			
			for (double x : h.x) {
				if (x < xMin) {
					xMin = x;
				} else if (x > xMax) {
					xMax = x;
				}
				nPoints++;
			}
			for (double y : h.y) {
				if (y < yMin) {
					yMin = y;
				} else if (y > yMax) {
					yMax = y;
				}
			}
		}

		out[0] = xMin;
		out[1] = yMin;
		out[2] = xMax;
		out[3] = yMax;
		out[4] = nRings;
		out[5] = nPoints;

		return out;
	}

	public static void main(String[] args) {

		CreateShp cs = new CreateShp();

		// cs.read("C:/testdir/shape/pga.shp");

		PolygonData pd = new PolygonData();

		pd.x.add(13.8857);
		pd.y.add(42.18701107144145);
		pd.x.add(13.882741346439758);
		pd.y.add(42.18757416407284);
		pd.x.add(13.878166856564892);
		pd.y.add(42.19053282);
		pd.x.add(13.877599017379163);
		pd.y.add(42.19076517089838);
		pd.x.add(13.877366666666667);
		pd.y.add(42.190988230725694);
		pd.x.add(13.87644980411749);
		pd.y.add(42.19144968328267);
		pd.x.add(13.87150662867499);
		pd.y.add(42.193006117320294);
		pd.x.add(13.869033333333332);
		pd.y.add(42.19577265284909);
		pd.x.add(13.867436568789149);
		pd.y.add(42.197269394178406);
		pd.x.add(13.866658621460822);
		pd.y.add(42.19886616);
		pd.x.add(13.86740562557756);
		pd.y.add(42.20049386905794);
		pd.x.add(13.869033333333332);
		pd.y.add(42.20244541916776);
		pd.x.add(13.870894349178137);
		pd.y.add(42.205338482666384);
		pd.x.add(13.873325173924812);
		pd.y.add(42.2071995);
		pd.x.add(13.875693198759736);
		pd.y.add(42.208872969245704);
		pd.x.add(13.877366666666665);
		pd.y.add(42.209448404946805);
		pd.x.add(13.880609094142574);
		pd.y.add(42.210441930069855);
		pd.x.add(13.88191775154559);
		pd.y.add(42.21098175148021);
		pd.x.add(13.8857);
		pd.y.add(42.21183319242534);
		pd.x.add(13.888927391511672);
		pd.y.add(42.212305445906416);
		pd.x.add(13.891584139777358);
		pd.y.add(42.21308364448467);
		pd.x.add(13.894033333333333);
		pd.y.add(42.213547122715006);
		pd.x.add(13.895869904037841);
		pd.y.add(42.213696267826236);
		pd.x.add(13.896765551971708);
		pd.y.add(42.21553284);
		pd.x.add(13.896920983849114);
		pd.y.add(42.218420492825906);
		pd.x.add(13.897244364224349);
		pd.y.add(42.22065514654017);
		pd.x.add(13.897386908252475);
		pd.y.add(42.22386618);
		pd.x.add(13.89747601869135);
		pd.y.add(42.227308868112175);
		pd.x.add(13.897527761223092);
		pd.y.add(42.2287050893147);
		pd.x.add(13.897616159231564);
		pd.y.add(42.23219952);
		pd.x.add(13.898918810974608);
		pd.y.add(42.235647378450345);
		pd.x.add(13.902366666666666);
		pd.y.add(42.24019863025978);
		pd.x.add(13.90253494068769);
		pd.y.add(42.24036458584436);
		pd.x.add(13.902857659601182);
		pd.y.add(42.24053286);
		pd.x.add(13.907680171513135);
		pd.y.add(42.24355269090273);
		pd.x.add(13.910699999999999);
		pd.y.add(42.244666179153626);
		pd.x.add(13.914422678690482);
		pd.y.add(42.245143518331375);
		pd.x.add(13.915452503281614);
		pd.y.add(42.245285367083625);
		pd.x.add(13.919033333333331);
		pd.y.add(42.24575232908572);
		pd.x.add(13.921083969638094);
		pd.y.add(42.246815562054735);
		pd.x.add(13.922733246192813);
		pd.y.add(42.2488662);
		pd.x.add(13.923576928059417);
		pd.y.add(42.25265594163905);
		pd.x.add(13.924341727693307);
		pd.y.add(42.254174598606696);
		pd.x.add(13.925417036662132);
		pd.y.add(42.25719954);
		pd.x.add(13.925853074025344);
		pd.y.add(42.258713133852204);
		pd.x.add(13.927366666666666);
		pd.y.add(42.260025292086745);
		pd.x.add(13.92933756655488);
		pd.y.add(42.26356197853507);
		pd.x.add(13.93412541516047);
		pd.y.add(42.26395829390081);
		pd.x.add(13.935699999999999);
		pd.y.add(42.26463723626628);
		pd.x.add(13.936509181687176);
		pd.y.add(42.26472369766548);
		pd.x.add(13.942868706746284);
		pd.y.add(42.264368252481255);
		pd.x.add(13.944033333333334);
		pd.y.add(42.26446079054334);
		pd.x.add(13.945508419806178);
		pd.y.add(42.26405779234709);
		pd.x.add(13.950303758448339);
		pd.y.add(42.263469970131354);
		pd.x.add(13.952366666666666);
		pd.y.add(42.26261171414284);
		pd.x.add(13.956278210892734);
		pd.y.add(42.26111108735531);
		pd.x.add(13.958611248253899);
		pd.y.add(42.2592882934171);
		pd.x.add(13.9607);
		pd.y.add(42.25784059783831);
		pd.x.add(13.961088730177199);
		pd.y.add(42.257588270488185);
		pd.x.add(13.961339822174084);
		pd.y.add(42.25719954);
		pd.x.add(13.962721651220761);
		pd.y.add(42.25517788716192);
		pd.x.add(13.964597696883589);
		pd.y.add(42.25276390000175);
		pd.x.add(13.96604525737186);
		pd.y.add(42.2488662);
		pd.x.add(13.965897154989204);
		pd.y.add(42.24573001914693);
		pd.x.add(13.965813486435877);
		pd.y.add(42.24375270947334);
		pd.x.add(13.965659857783448);
		pd.y.add(42.24053286);
		pd.x.add(13.965400105084312);
		pd.y.add(42.2368996288444);
		pd.x.add(13.965383475043728);
		pd.y.add(42.2358493812095);
		pd.x.add(13.96508925424365);
		pd.y.add(42.23219952);
		pd.x.add(13.96446482655859);
		pd.y.add(42.22843469042956);
		pd.x.add(13.9607);
		pd.y.add(42.22551771099313);
		pd.x.add(13.959171559031567);
		pd.y.add(42.22539462219119);
		pd.x.add(13.952802876846382);
		pd.y.add(42.22430239052868);
		pd.x.add(13.952366666666666);
		pd.y.add(42.22426080290219);
		pd.x.add(13.951539371556322);
		pd.y.add(42.224693475772185);
		pd.x.add(13.951220024902913);
		pd.y.add(42.22386618);
		pd.x.add(13.950293594184387);
		pd.y.add(42.22179310585926);
		pd.x.add(13.948416201885767);
		pd.y.add(42.219483307941275);
		pd.x.add(13.9480565871783);
		pd.y.add(42.21553284);
		pd.x.add(13.946922905822838);
		pd.y.add(42.21264326519884);
		pd.x.add(13.944033333333334);
		pd.y.add(42.21113071698379);
		pd.x.add(13.941114902216015);
		pd.y.add(42.21011793345207);
		pd.x.add(13.938554246356404);
		pd.y.add(42.2100537486398);
		pd.x.add(13.9357);
		pd.y.add(42.20944539571789);
		pd.x.add(13.93358633070444);
		pd.y.add(42.20931317098649);
		pd.x.add(13.929218345869533);
		pd.y.add(42.20905118068421);
		pd.x.add(13.927366666666666);
		pd.y.add(42.208935905827424);
		pd.x.add(13.925733871675579);
		pd.y.add(42.20883229629733);
		pd.x.add(13.92030470538809);
		pd.y.add(42.20847087307186);
		pd.x.add(13.919033333333333);
		pd.y.add(42.208389723691965);
		pd.x.add(13.917885103199445);
		pd.y.add(42.20834773105247);
		pd.x.add(13.911481734827262);
		pd.y.add(42.20798123545265);
		pd.x.add(13.910699999999999);
		pd.y.add(42.20795148315078);
		pd.x.add(13.909868826856279);
		pd.y.add(42.208030673808665);
		pd.x.add(13.907927468877876);
		pd.y.add(42.2071995);
		pd.x.add(13.904177313486574);
		pd.y.add(42.205388851731584);
		pd.x.add(13.902366666666666);
		pd.y.add(42.203077941299675);
		pd.x.add(13.90137760349098);
		pd.y.add(42.199855223966935);
		pd.x.add(13.901541209381993);
		pd.y.add(42.19886616);
		pd.x.add(13.900060652761459);
		pd.y.add(42.196560144249986);
		pd.x.add(13.898877083216473);
		pd.y.add(42.19402240624187);
		pd.x.add(13.895982821218599);
		pd.y.add(42.192482309444856);
		pd.x.add(13.894033333333333);
		pd.y.add(42.19113579685673);
		pd.x.add(13.893615947886405);
		pd.y.add(42.19095020578084);
		pd.x.add(13.89249390500585);
		pd.y.add(42.19053282);
		pd.x.add(13.888844226057358);
		pd.y.add(42.187388591427265);
		pd.x.add(13.8857);
		pd.y.add(42.18701107144145);

//		pd.x = new ArrayList<>();
//		pd.y = new ArrayList<>();
//		pd.x.add(9.0);
//		pd.y.add(45.0);
//		pd.x.add(10.0);
//		pd.y.add(45.0);
//		pd.x.add(10.0);
//		pd.y.add(44.0);
//		pd.x.add(9.0);
//		pd.y.add(44.0);
//		pd.x.add(9.0);
//		pd.y.add(45.0);
		pd.holes = new ArrayList<>();
		pd.nVert = 107;

//		PolygonData hole = new PolygonData();
//		hole.x = new ArrayList<>();
//		hole.y = new ArrayList<>();
//		hole.x.add(9.2);
//		hole.y.add(44.8);
//		hole.x.add(9.8);
//		hole.y.add(44.8);
//		hole.x.add(9.8);
//		hole.y.add(44.2);
//		hole.x.add(9.2);
//		hole.y.add(44.8);
//		hole.nVert = 4;
//		// pd.holes.add(hole);

		PolygonData pd2 = new PolygonData();

		pd2.x.add(13.946655973101247);
		pd2.y.add(42.23219952);
		pd2.x.add(13.945141348338087);
		pd2.y.add(42.233307535891164);
		pd2.x.add(13.944033333333334);
		pd2.y.add(42.23365895250463);
		pd2.x.add(13.9425770515974);
		pd2.y.add(42.23365580290096);
		pd2.x.add(13.936064916922449);
		pd2.y.add(42.232564437214386);
		pd2.x.add(13.9357);
		pd2.y.add(42.23256333591312);
		pd2.x.add(13.935439222847183);
		pd2.y.add(42.23246029736144);
		pd2.x.add(13.935355391345755);
		pd2.y.add(42.23219952);
		pd2.x.add(13.935449282461285);
		pd2.y.add(42.23194880226072);
		pd2.x.add(13.935699999999999);
		pd2.y.add(42.23160136736087);
		pd2.x.add(13.936879391831239);
		pd2.y.add(42.23102012722525);
		pd2.x.add(13.941164704752865);
		pd2.y.add(42.22933088912463);
		pd2.x.add(13.944033333333334);
		pd2.y.add(42.22723445879886);
		pd2.x.add(13.946933085427254);
		pd2.y.add(42.22929976558628);
		pd2.x.add(13.946655973101247);
		pd2.y.add(42.23219952);

//		pd2.x = new ArrayList<>();
//		pd2.y = new ArrayList<>();
//		pd2.x.add(11.0);
//		pd2.y.add(45.0);
//		pd2.x.add(12.0);
//		pd2.y.add(45.0);
//		pd2.x.add(11.5);
//		pd2.y.add(44.0);
//		pd2.x.add(11.0);
//		pd2.y.add(45.0);
		pd2.holes = new ArrayList<>();
		pd2.nVert = 15;

		Map<Double, List<PolygonData>> map = new HashMap<>();

		List<PolygonData> list = new ArrayList<>();

		list.add(pd);
		list.add(pd2);
		map.put(0.01, list);

//		list = new ArrayList<>();
//		list.add(pd2);
//		map.put(0.02, list);
//
//		PolygonData pd3 = new PolygonData();
//		pd3.x = new ArrayList<>();
//		pd3.y = new ArrayList<>();
//		pd3.x.add(9.0);
//		pd3.y.add(41.0);
//		pd3.x.add(10.0);
//		pd3.y.add(41.0);
//		pd3.x.add(10.0);
//		pd3.y.add(40.0);
//		pd3.x.add(9.0);
//		pd3.y.add(40.0);
//		pd3.x.add(9.0);
//		pd3.y.add(41.0);
//		pd3.holes = new ArrayList<>();
//		pd3.nVert = 5;
//
//		List<PolygonData> list2 = new ArrayList<>();
//
//		list2.add(pd3);
//
//		map.put(0.03, list2);

//		cs.write(map, "c:/testdir/fake_pga");
//
//		CreateIndex ci = new CreateIndex();
//		ci.write(map, "c:/testdir/fake_pga");
//
//		CreateDbf cd = new CreateDbf();
//		cd.write(map, "c:/testdir/fake_pga");

		cs.read("c:/testdir/curve_output_isobands_02.shp");
//		cs.read("c:/testdir/shape/pga.shp");

	}

}
