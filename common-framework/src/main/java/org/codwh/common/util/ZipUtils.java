package org.codwh.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    static Logger logger = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * GZIP解压缩
     *
     * @param encoded
     * @return
     */
    public static byte[] decodeGZip(byte[] encoded) {
        try {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(encoded));
            return StreamUtils.readAll(gis);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * GZIP压缩
     *
     * @param source
     * @return
     */
    public static byte[] encodeGZip(byte[] source) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gos = new GZIPOutputStream(baos);
            gos.write(source);
            gos.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * ZIP解压缩
     *
     * @param encoded
     * @return
     */
    public static byte[] decodeZip(byte[] encoded) {
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(encoded));
        return StreamUtils.readAll(zis);
    }

    /**
     * ZIP压缩
     *
     * @param source
     * @return
     */
    public static byte[] encodeZip(byte[] source) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            zos.write(source);
            zos.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }
}
