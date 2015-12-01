package java_gaps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils {

	public static String readResource(Class<?> classForPath, String fileName)
			throws IOException {
		String pkgPath = classForPath.getPackage().getName().replace('.', '/');
		return readResource("/" + pkgPath + "/" + fileName);
	}

	public static String readResource(String resourcePath) throws IOException {
		InputStream inputStream = ResourceUtils.class
				.getResourceAsStream(resourcePath);
		if (inputStream == null)
			throw new IOException("No resource for path " + resourcePath);

		return readStream(inputStream);
	}

	public static byte[] readStreamAsBytes(InputStream stream)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[4096];
		int count;
		while ((count = stream.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		out.close();
		return out.toByteArray();
	}

	public static String readStream(InputStream stream, String encoding)
			throws IOException {
		return new String(readStreamAsBytes(stream), encoding);
	}

	public static String readStream(InputStream stream) throws IOException {
		return new String(readStreamAsBytes(stream));
	}

}
