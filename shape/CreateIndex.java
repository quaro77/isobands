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

public class CreateIndex {

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

			bytes = new byte[4];
			while (index < fileBytes.length) {

				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				System.out.println("offset -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.BIG_ENDIAN));

				bytes[0] = fileBytes[index++];
				bytes[1] = fileBytes[index++];
				bytes[2] = fileBytes[index++];
				bytes[3] = fileBytes[index++];
				System.out.println("content length -> " + ShapeUtil.decodeInteger(bytes, ByteOrder.BIG_ENDIAN));

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

		System.out.println("Starting SHX file creation...");

		List<Byte> byteList = new LinkedList<>();

		double[] bbox = ShapeUtil.findBBox(map);

		// file header
		int fileDim = writeFileHeader(byteList, bbox[0], bbox[1], bbox[2], bbox[3]);

		// records
		Set<Double> ks = map.keySet();
		List<Double> ksList = new ArrayList<>(ks);
		Collections.sort(ksList);

		int record = 1;
		int offset = 100;

		for (Double k : ksList) {
			List<PolygonData> polys = map.get(k);

			for (PolygonData poly : polys) {
				System.out.println("index of " + record + " record:" + offset);
				offset += writeRecord(byteList, record, poly, offset);
				fileDim += 8;
				record++;
			}
		}

		byte[] ba = ShapeUtil.encodeInteger(fileDim / 2, ByteOrder.BIG_ENDIAN); // file length in 16bit words
		ShapeUtil.putBytes(ba, byteList, 24);

		File f = new File(filename + ".shx");
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

		System.out.println("SHX file successfully created.");

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

	public int writeRecord(List<Byte> byteList, int record, PolygonData p, int startOffset) {

		int offset = 0;

		byte[] ba = ShapeUtil.encodeInteger((startOffset + offset) / 2, ByteOrder.BIG_ENDIAN); // offset
		ShapeUtil.addBytes(ba, byteList);

		int cLengthIndex = byteList.size();

		ba = ShapeUtil.encodeInteger(0, ByteOrder.BIG_ENDIAN); // content length (sovrascritto dopo)
		ShapeUtil.addBytes(ba, byteList);

		// aggiorno offset:

		offset += 8;

		int offsetStart = offset;

		offset += 44;

		offset += 4;

		for (PolygonData h : p.holes) {
			offset += 4;
		}

		offset += (16 * p.nVert);
		for (PolygonData h : p.holes) {
			offset += (16 * h.nVert);

		}

		int cLength = offset - offsetStart;

		ba = ShapeUtil.encodeInteger(cLength / 2, ByteOrder.BIG_ENDIAN); // content length
		ShapeUtil.putBytes(ba, byteList, cLengthIndex);

		return offset;
	}

	public static void main(String[] args) {

		CreateIndex ci = new CreateIndex();

		// ci.read("c:/testdir/export/export.shx");
		ci.read("c:/testdir/export/export.shx");

	}

}
