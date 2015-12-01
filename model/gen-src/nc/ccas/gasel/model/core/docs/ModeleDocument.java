package nc.ccas.gasel.model.core.docs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import java_gaps.ResourceUtils;
import nc.ccas.gasel.model.core.docs.auto._ModeleDocument;

import org.apache.cayenne.util.Base64Codec;

public class ModeleDocument extends _ModeleDocument {
	private static final long serialVersionUID = 4248698880270221138L;

	private static byte[] gzip(String data) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			ObjectOutputStream oos = new ObjectOutputStream(gzip);
			oos.writeObject(data);
			gzip.close();

			return out.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String gunzip(byte[] data) {
		try {
			GZIPInputStream gunzip = new GZIPInputStream( //
					new ByteArrayInputStream(data));
			ObjectInputStream ois = new ObjectInputStream(gunzip);
			return (String) ois.readObject();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static String encode(byte[] data) {
		return new String(Base64Codec.encodeBase64(data));
	}

	private static byte[] decode(String string) {
		try {
			return Base64Codec.decodeBase64(string.getBytes("ASCII"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	// ------------------------------------------------------------------
	// Decoding
	//

	@Override
	public String getData() {
		return gunzip(decode(super.getData()));
	}

	// ------------------------------------------------------------------
	// Encoding
	//

	@Override
	public void setData(String data) {
		super.setData(encode(gzip(data)));
	}

	// ------------------------------------------------------------------
	// Mise à vide
	//

	public void setEmpty() {
		try {
			setData(ResourceUtils.readResource(ModeleDocument.class, "empty.doc.xml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
