package fr.quintipio.javautils.utils;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Classe utilitaire pour les base 64
 * @author Quentin
 */
public class Base64Utils {

    private Base64Utils() {}
    
    public static byte[] decode(final byte[] b) throws Exception {
        final ByteArrayInputStream bais = new ByteArrayInputStream(b);
        final InputStream b64is = MimeUtility.decode(bais, "base64");
        final byte[] tmp = new byte[b.length];
        final int n = b64is.read(tmp);
        final byte[] res = new byte[n];
        System.arraycopy(tmp, 0, res, 0, n);
        return res;
    }

    public static byte[] encode(final byte[] b) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final OutputStream b64os = MimeUtility.encode(baos, "base64");
        b64os.write(b);
        baos.close();
        return baos.toByteArray();
    }

}
