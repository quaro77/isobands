package shape;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CreatePrj {

	public void write(String filename, String projection) {

		File f = new File(filename + ".prj");
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);
			switch (projection) {
			case "4326":
				bw.write("GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]]");
				break;
			}
		} catch (Exception e) {

		} finally {
			try {
				bw.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
