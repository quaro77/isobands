package shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;

import work.PolygonData;

public class ShapeUtil {

	protected static int decodeInteger(byte[] in, ByteOrder order) {
		ByteBuffer bb = ByteBuffer.wrap(in);
		bb.order(order);
		return bb.getInt();
	}

	protected static short decodeShort(byte[] in, ByteOrder order) {
		ByteBuffer bb = ByteBuffer.wrap(in);
		bb.order(order);
		return bb.getShort();
	}

	protected static double decodeDouble(byte[] in, ByteOrder order) {
		ByteBuffer bb = ByteBuffer.wrap(in);
		bb.order(order);
		return bb.getDouble();
	}

	protected static byte[] encodeInteger(int in, ByteOrder order) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(order);
		bb.putInt(in);
		return bb.array();
	}

	protected static byte[] encodeShort(short in, ByteOrder order) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(order);
		bb.putShort(in);
		return bb.array();
	}

	protected static byte[] encodeDouble(double in, ByteOrder order) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(order);
		bb.putDouble(in);
		return bb.array();
	}

	protected static void addBytes(byte[] ba, List<Byte> byteList) {
		for (byte b : ba) {
			byteList.add(b);
		}
	}

	protected static void putBytes(byte[] ba, List<Byte> byteList, int index) {
		for (int i = 0; i < ba.length; i++) {
			byteList.set(index + i, ba[i]);
		}
	}

	protected static double[] findBBox(Map<Double, List<PolygonData>> map) {

		double[] out = new double[4];

		double xMin = 1000.0;
		double yMin = 1000.0;
		double xMax = -1000.0;
		double yMax = -1000.0;

		for (Double k : map.keySet()) {
			for (PolygonData p : map.get(k)) {

				for (double x : p.x) {
					if (x < xMin) {
						xMin = x;
					} else if (x > xMax) {
						xMax = x;
					}

				}
				for (double y : p.y) {
					if (y < yMin) {
						yMin = y;
					} else if (y > yMax) {
						yMax = y;
					}
				}
				for (PolygonData h : p.holes) {

					for (double x : h.x) {
						if (x < xMin) {
							xMin = x;
						} else if (x > xMax) {
							xMax = x;
						}

					}
					for (double y : h.y) {
						if (y < yMin) {
							yMin = y;
						} else if (y > yMax) {
							yMax = y;
						}
					}
				}
			}
			out[0] = xMin;
			out[1] = yMin;
			out[2] = xMax;
			out[3] = yMax;
		}
		return out;

	}

}
