package org.codwh.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class FileUtils {

    static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 从相对路径中读取文件内容
     *
     * @param filePath
     * @return
     */
    public static byte[] loadFileFromContextPath(final String filePath, int beginPos, int endPos) {
        String s = filePath.startsWith(File.separator) ? "" : File.separator;
        String fullFilePath = EnvironmentUtils.getContextPath() + s + filePath;
        return loadFileFromSysPath(fullFilePath, beginPos, endPos);
    }

    /**
     * 从系统路径中读取文件内容
     *
     * @param filePath
     * @return
     */
    public static byte[] loadFileFromSysPath(final String filePath, int beginPos, int endPos) {
        byte[] fileContent = null;
        fileContent = loadFile(new File(filePath), beginPos, endPos);
        return fileContent;
    }

    public static byte[] loadFile(File file, int beginPos, int endPos) {
        try {
            long fileSize = file.length();
            if (beginPos < 0 || endPos > fileSize) {
                return null;
            }

            int loadLength = 0;
            if (endPos < 0) {
                //表示全部读取
                loadLength = (int) fileSize - beginPos + 1;
            } else {
                loadLength = endPos - beginPos + 1;
            }

            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            byte[] fileBytes = new byte[loadLength - 1];
            randomAccessFile.seek(beginPos);
            randomAccessFile.read(fileBytes);
            randomAccessFile.close();
            return fileBytes;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * 从相对路径中读取文件内容
     *
     * @param filePath
     * @return
     */
    public static byte[] loadFileFromContextPath(final String filePath) {
        String s = filePath.startsWith(File.separator) ? "" : File.separator;
        String fullPath = EnvironmentUtils.getContextPath() + s + filePath;
        return loadFileFromSysPath(fullPath);
    }

    /**
     * 从系统路径中读取文件内容
     *
     * @param filePath
     * @return
     */
    public static byte[] loadFileFromSysPath(final String filePath) {
        byte[] fileContent = null;
        fileContent = loadFile(new File(filePath));
        return fileContent;
    }

    public static byte[] loadFile(File file) {
        return loadFile(file, 0, -1);
    }

    public static byte[] loadResource(String resourcePath) {
        byte[] fileContent = null;
        fileContent = loadFile(getResourceFile(resourcePath));
        return fileContent;
    }

    public static File getResourceFile(String resourcePath) {
        try {
            resourcePath = URLDecoder.decode(resourcePath, "UTF-8");
            URL url = EnvironmentUtils.class.getClassLoader().getResource(resourcePath);
            if (url != null) {
                File file = new File(url.getFile());
                return file;
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }
}
