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
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import work.PolygonData;

public class CreateDbf {

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

			byte[] bytes = new byte[1];
			bytes[0] = fileBytes[0];
			System.out.println("version -> " + bytes[0]);

			bytes = new byte[3];
			bytes[0] = fileBytes[1];
			bytes[1] = fileBytes[2];
			bytes[2] = fileBytes[3];
			System.out.println("YY -> " + bytes[0]);
			System.out.println("MM -> " + bytes[1]);
			System.out.println("DD -> " + bytes[2]);

			bytes = new byte[4];
			bytes[0] = fileBytes[4];
			bytes[1] = fileBytes[5];
			bytes[2] = fileBytes[6];
			bytes[3] = fileBytes[7];
			System.out.println("N records -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.LITTLE_ENDIAN));
			System.out.println(bytes[0] + " " + bytes[1] + " " + bytes[2] + " " + bytes[3]);

			bytes = new byte[2];
			bytes[0] = fileBytes[8];
			bytes[1] = fileBytes[9];
			System.out.println("N bytes in header -> " + ShapeUtil.decodeShort(bytes, ByteOrder.LITTLE_ENDIAN));

			bytes = new byte[2];
			bytes[0] = fileBytes[10];
			bytes[1] = fileBytes[11];
			System.out.println("N bytes in record -> " + ShapeUtil.decodeShort(bytes, ByteOrder.LITTLE_ENDIAN));

			int index = 32;

			while (fileBytes[index] != 13) {

				bytes = new byte[11];
				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				bytes[4] = fileBytes[index++];
				bytes[5] = fileBytes[index++];
				bytes[6] = fileBytes[index++];
				bytes[7] = fileBytes[index++];
				bytes[8] = fileBytes[index++];
				bytes[9] = fileBytes[index++];
				bytes[10] = fileBytes[index++];
				System.out.println("Field name -> " + new String(bytes));

				bytes = new byte[1];
				bytes[0] = fileBytes[index++];
				System.out.println("Field type -> " + new String(bytes));

				bytes = new byte[4];
				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				System.out.println("Address -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.LITTLE_ENDIAN));

				System.out.println("Field dim. -> " + fileBytes[index++]);
				System.out.println("Decimal count -> " + fileBytes[index++]);

				index += 14;

			}

			index += 1;

			while (index < fileBytes.length) {
				bytes = new byte[1];
				bytes[0] = fileBytes[index++];
				System.out.println("Deletion flag -> " + new String(bytes));

				bytes = new byte[14];
				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				bytes[4] = fileBytes[index++];
				bytes[5] = fileBytes[index++];
				bytes[6] = fileBytes[index++];
				bytes[7] = fileBytes[index++];
				bytes[8] = fileBytes[index++];
				bytes[9] = fileBytes[index++];
				bytes[10] = fileBytes[index++];
				bytes[11] = fileBytes[index++];
				bytes[12] = fileBytes[index++];
				bytes[13] = fileBytes[index++];
				System.out.println("Field 1 value -> " + new String(bytes));

//				bytes = new byte[14];
//				bytes[0] = fileBytes[index++];
//				bytes[1] = fileBytes[index++];
//				bytes[2] = fileBytes[index++];
//				bytes[3] = fileBytes[index++];
//				bytes[4] = fileBytes[index++];
//				bytes[5] = fileBytes[index++];
//				bytes[6] = fileBytes[index++];
//				bytes[7] = fileBytes[index++];
//				bytes[8] = fileBytes[index++];
//				bytes[9] = fileBytes[index++];
//				bytes[10] = fileBytes[index++];
//				bytes[11] = fileBytes[index++];
//				bytes[12] = fileBytes[index++];
//				bytes[13] = fileBytes[index++];
//				System.out.println("Field 2 value -> " + new String(bytes));
//
//				bytes = new byte[12];
//				bytes[0] = fileBytes[index++];
//				bytes[1] = fileBytes[index++];
//				bytes[2] = fileBytes[index++];
//				bytes[3] = fileBytes[index++];
//				bytes[4] = fileBytes[index++];
//				bytes[5] = fileBytes[index++];
//				bytes[6] = fileBytes[index++];
//				bytes[7] = fileBytes[index++];
//				bytes[8] = fileBytes[index++];
//				bytes[9] = fileBytes[index++];
//				bytes[10] = fileBytes[index++];
//				bytes[11] = fileBytes[index++];
//				System.out.println("Field 3 value -> " + new String(bytes));
//
//				bytes = new byte[12];
//				bytes[0] = fileBytes[index++];
//				bytes[1] = fileBytes[index++];
//				bytes[2] = fileBytes[index++];
//				bytes[3] = fileBytes[index++];
//				bytes[4] = fileBytes[index++];
//				bytes[5] = fileBytes[index++];
//				bytes[6] = fileBytes[index++];
//				bytes[7] = fileBytes[index++];
//				bytes[8] = fileBytes[index++];
//				bytes[9] = fileBytes[index++];
//				bytes[10] = fileBytes[index++];
//				bytes[11] = fileBytes[index++];
//				System.out.println("Field 4 value -> " + new String(bytes));
//
//				bytes = new byte[12];
//				bytes[0] = fileBytes[index++];
//				bytes[1] = fileBytes[index++];
//				bytes[2] = fileBytes[index++];
//				bytes[3] = fileBytes[index++];
//				bytes[4] = fileBytes[index++];
//				bytes[5] = fileBytes[index++];
//				bytes[6] = fileBytes[index++];
//				bytes[7] = fileBytes[index++];
//				bytes[8] = fileBytes[index++];
//				bytes[9] = fileBytes[index++];
//				bytes[10] = fileBytes[index++];
//				bytes[11] = fileBytes[index++];
//				System.out.println("Field 5 value -> " + new String(bytes));
//
//				bytes = new byte[14];
//				bytes[0] = fileBytes[index++];
//				bytes[1] = fileBytes[index++];
//				bytes[2] = fileBytes[index++];
//				bytes[3] = fileBytes[index++];
//				bytes[4] = fileBytes[index++];
//				bytes[5] = fileBytes[index++];
//				bytes[6] = fileBytes[index++];
//				bytes[7] = fileBytes[index++];
//				bytes[8] = fileBytes[index++];
//				bytes[9] = fileBytes[index++];
//				bytes[10] = fileBytes[index++];
//				bytes[11] = fileBytes[index++];
//				bytes[12] = fileBytes[index++];
//				bytes[13] = fileBytes[index++];
//				System.out.println("Field 6 value -> " + new String(bytes));
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

		System.out.println("Starting DBF file creation...");

		List<Byte> byteList = new LinkedList<>();

		int nRecords = 0;

		for (Double k : map.keySet()) {
			nRecords += map.get(k).size();
		}

		Set<Double> ks = map.keySet();
		List<Double> ksList = new ArrayList<>(ks);
		Collections.sort(ksList);

		writeHeader(nRecords, byteList);
		writeRecords(map, byteList);

		File f = new File(filename + ".dbf");
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

		System.out.println("DBF file successfully created.");
	}

	private int writeHeader(int nRecords, List<Byte> byteList) {

		byte b = 3; // version
		byteList.add(b);

		Calendar gc = new GregorianCalendar();
		b = Byte.parseByte("" + (gc.get(Calendar.YEAR) - 1900)); // YY
		byteList.add(b);
		b = Byte.parseByte("" + (gc.get(Calendar.MONTH) + 1)); // MM
		byteList.add(b);
		b = Byte.parseByte("" + gc.get(Calendar.DAY_OF_MONTH)); // DD
		byteList.add(b);
		byte[] bb = ShapeUtil.encodeInteger(nRecords, ByteOrder.LITTLE_ENDIAN); // num records
		ShapeUtil.addBytes(bb, byteList);

		bb = ShapeUtil.encodeShort((short) 0, ByteOrder.LITTLE_ENDIAN); // header length (sovrascritto dopo)
		ShapeUtil.addBytes(bb, byteList);

		bb = ShapeUtil.encodeShort((short) 15, ByteOrder.LITTLE_ENDIAN); // lenght of a field
		ShapeUtil.addBytes(bb, byteList);

		for (int i = 0; i < 20; i++) {
			b = Byte.parseByte("0"); // reserved
			byteList.add(b);
		}

		// FIELD DESCRIPTOR

		bb = "PARAMVALUE ".getBytes(); // field name
		ShapeUtil.addBytes(bb, byteList);

		bb = "N".getBytes();
		ShapeUtil.addBytes(bb, byteList); // field type

		bb = ShapeUtil.encodeInteger(0, ByteOrder.LITTLE_ENDIAN); // address in memory (not used)
		ShapeUtil.addBytes(bb, byteList);

		b = 14; // size of field
		byteList.add(b);

		b = 4; // decimals
		byteList.add(b);

		for (int i = 0; i < 14; i++) {
			b = Byte.parseByte("0"); // reserved
			byteList.add(b);
		}

		// END FIELD DESCRIPTOR

		b = 13; // end
		byteList.add(b);

		bb = ShapeUtil.encodeShort((short) byteList.size(), ByteOrder.LITTLE_ENDIAN); // header length
		ShapeUtil.putBytes(bb, byteList, 8);

		return byteList.size();
	}

	private void writeRecords(Map<Double, List<PolygonData>> map, List<Byte> byteList) {

		Set<Double> ks = map.keySet();
		List<Double> ksList = new ArrayList<>(ks);
		Collections.sort(ksList);

		for (Double k : ksList) {
			for (int ii = 0; ii < map.get(k).size(); ii++) {
				ShapeUtil.addBytes(" ".getBytes(), byteList); // deletion marker
				double val = Math.round(k * 10000.0) / 10000.0;
				String valString = "" + val;
				String[] split = valString.split("\\.");
				String sup = split[0];
				String decimals = split[1];

				int l = decimals.length();

				if (l > 4) {
					decimals = decimals.substring(0, 4);
				} else if (l < 4) {
					for (int i = 0; i < 4 - l; i++) {
						decimals = decimals + "0";
					}
				}

				valString = sup + "." + decimals;
				l = valString.length();
				if (l < 14) {
					for (int i = 0; i < 14 - l; i++) {
						valString = " " + valString;
					}
				}
				ShapeUtil.addBytes(valString.getBytes(), byteList);
			}
		}
	}

	public static void main(String[] args) {

		CreateDbf cd = new CreateDbf();

		Map<Double, List<PolygonData>> map = new HashMap<>();

		map.put(0.01, null);
		map.put(0.02, null);

		cd.write(map, "c:/testdir/shapefile_test_out");
		cd.read("c:/testdir/shapefile_test_out.dbf");
//		cd.read("c:/testdir/shape/pga.dbf");

	}

}
