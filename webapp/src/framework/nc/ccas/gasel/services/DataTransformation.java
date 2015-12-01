package nc.ccas.gasel.services;

import java.io.IOException;
import java.io.OutputStream;

public interface DataTransformation {

	DataTransformation IDENTITY = new DataTransformation() {
		@Override
		public void transformData(byte[] data, OutputStream output) throws IOException {
			output.write(data);
		}
	};

	void transformData(byte[] data, OutputStream output) throws IOException;

}
