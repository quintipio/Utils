package fr.quintipio.javautils.utils;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;

import javax.faces.bean.ManagedBean;

import org.hibernate.engine.jdbc.NonContextualLobCreator;
import org.hibernate.engine.jdbc.StreamUtils;

/**
 * Méthodes utilitaires pour <code>Blob</code> (Binary Large OBjects).
 * NDLR : la méthode Hibernate.createBlob est dépréciée dans les dernieres versions hibernate (4.2.0 en fev 2013) LA methode à utiliser valable dans toutes les versions est la suivante : <b>NonContextualLobCreator.INSTANCE.createBlob</b>
 *
 * @author Quentin
 */
//@javax.transaction.Transactional
//@ManagedBean
public final class LobUtils {

    /** The Constant BUF_4KO. */
    static final int                  BUF_4KO = 4096;

    /** The Constant BUF_8KO. */
    private static final int          BUF_8KO = 8192;

    /**
     * Bdd blob vers file real.
     *
     * @param blob
     *            the blob
     * @param folder
     *            the folder
     * @return the file
     */
    public static File bddBlobVersFileReal(final Blob blob, final String folder) throws Exception  {
        try {
            final byte[] buffer = new byte[BUF_8KO];

            final File fichier = new File(folder);
            if (fichier.exists()) {
                fichier.delete();
            }

            final FileOutputStream fos = new FileOutputStream(fichier);
            InputStream bt;

            try {
                bt = blob.getBinaryStream();
                int bytesRead = 0;
                while ((bytesRead = bt.read(buffer, 0, BUF_8KO)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
                bt.close();
                return fichier;
            }
            catch (final Exception e) {
                throw e;
            }
        }
        catch (final Exception e) {
            throw e;
        }
    }

    /**
     * Utile pour envoyer un cv (blob) vers le facesContexte (format octal).
     *
     * @param blob
     *            the blob
     * @return the byte[]
     */
    public static byte[] blobToByte(final Blob blob) throws Exception {
        ByteArrayOutputStream baos = toBytes(blob);
        final byte[] retour = baos.toByteArray();
        baos = null;
        return retour;
    }

    /**
     * Blob to string.
     *
     * @param blob
     *            the blob
     * @return the string
     */
    public static String blobToString(final Blob blob) throws Exception { // NO_UCD (unused code)
        final StringBuilder sb = new StringBuilder();
        try {
            ByteArrayOutputStream baos = toBytes(blob);

            sb.append(new String(baos.toByteArray()));
            baos = null;
        }
        catch (final Exception e) {
            throw e;
        }
        return sb.toString();
    }

    /**
     * Blob vers base64.
     *
     * @param blob
     *            the blob
     * @return the string
     */
    public static String blobVersBase64(final Blob blob) throws Exception  { // NO_UCD (unused code)
        final byte[] bytes = LobUtils.blobToByte(blob);
        return "data:image/png;base64," + StringUtils.bytesToBase64(bytes);
    }

    /**
     * Transforme un byte[] en Blob.
     *
     * @param chaine
     *            the chaine
     * @return the blob
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    public static Blob bytesToBlob(final byte[] chaine) throws Exception {
        Blob myBlob = null;
        if (!StringUtils.isEmpty(new String(chaine))) {
            try {
                myBlob = NonContextualLobCreator.INSTANCE.wrap(NonContextualLobCreator.INSTANCE.createBlob(chaine));
            }
            catch (final Exception e) {
                throw e;
            }
        }
        return myBlob;
    }

    /**
     * Bytes vers base64.
     *
     * @param bytes
     *            the bytes
     * @return the string
     */
    public static String bytesVersBase64(final byte[] bytes) throws Exception {
        return "data:image/png;base64," + StringUtils.bytesToBase64(bytes);
    }

    /**
     * File vers blob, transforme un File en Blob.
     *
     * @param tempArmoirie
     *            the temp armoirie
     * @return the blob
     */
    public static Blob fileVersBlob(final File tempArmoirie) throws Exception {
        FileInputStream imgIS;
        Blob imgBlob = null;
        try {
            imgIS = new FileInputStream(tempArmoirie);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(imgIS.available());
            StreamUtils.copy(imgIS, buffer);

            imgBlob = NonContextualLobCreator.INSTANCE.wrap(NonContextualLobCreator.INSTANCE.createBlob(buffer.toByteArray()));
            imgIS.close();
            buffer.close();
            buffer = null;
            imgIS = null;
        }
        catch (final Exception e) {
            throw e;
        }

        return imgBlob;
    }

    /**
     * Methode pour redimensionner une image.
     *
     * @param bImage
     *            the b image
     * @param factor
     *            the factor
     * @return the buffered image
     */
    public static BufferedImage scale(final BufferedImage bImage, final double factor) { // NO_UCD (unused code)
        final int destWidth = (int) (bImage.getWidth() * factor);
        final int destHeight = (int) (bImage.getHeight() * factor);
        // créer l'image de destination
        final GraphicsConfiguration configuration = bImage.createGraphics().getDeviceConfiguration();
        final BufferedImage bImageNew = configuration.createCompatibleImage(destWidth, destHeight, Transparency.BITMASK);// garde la transparence PNG

        final Graphics2D graphics = bImageNew.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // dessiner l'image de destination
        graphics.drawImage(bImage, 0, 0, destWidth, destHeight, 0, 0, bImage.getWidth(), bImage.getHeight(), null);
        graphics.dispose();

        return bImageNew;
    }

    /**
     * String to blob.
     *
     * @param contenu
     *            the contenu
     * @return the blob
     */
    public static Blob stringToBlob(final String contenu) throws Exception  { // NO_UCD (unused code)
        Blob blob = null;

        try {
            blob = bytesToBlob(contenu.getBytes());
        }
        catch (final Exception e) {
            throw e;
        }
        return blob;
    }

    /**
     * Routine basique.
     *
     * @param blob
     *            the blob
     * @return the byte array output stream
     */
    public static ByteArrayOutputStream toBytes(final Blob blob) throws Exception  { // NO_UCD (use private)
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream bt;
        if (blob != null) {
            try {
                bt = blob.getBinaryStream();
                int bytesRead = 0;
                final byte[] buffer = new byte[BUF_8KO];
                while ((bytesRead = bt.read(buffer, 0, BUF_8KO)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                bt.close();
            }
            catch (final Exception e) {
                throw e;
            }
        }
        return baos;
    }

    /**
     * Instantiates a new lob utils.
     */
    private LobUtils() {}

    /**
     * Sérialize un objet
     *
     * @param obj
     *            l'objet à convertir
     * @return l'objet converti
     * @throws IOException
     */
    public static byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    /**
     * désérialize un objet
     *
     * @param bytes
     *            l'objet à convertir
     * @return l'objet converti
     * @throws IOException
     */
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        }
    }

}
