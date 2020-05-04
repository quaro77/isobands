package work;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public class PolygonData {

	Polygon shape;
	public List<Double> x;
	public List<Double> y;
	boolean hole;
	public List<PolygonData> holes;
	public int nVert;

	public PolygonData() {
		shape = new Polygon();
		x = new ArrayList<>();
		y = new ArrayList<>();
		nVert = 0;
		hole = false;
		holes = new ArrayList<>();
	}

}
