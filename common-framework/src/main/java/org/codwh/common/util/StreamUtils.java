package org.codwh.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class StreamUtils {

    /**
     * 从InputStream中读取定长的字符串
     *
     * @param is
     * @param len
     * @return
     */
    public static byte[] read(InputStream is, int len) {
        try {
            byte[] buffer = new byte[len];
            BufferedInputStream bis = new BufferedInputStream(is);
            int readLen = bis.read(buffer);
            buffer = Arrays.copyOfRange(buffer, 0, readLen);
            bis.close();
            return readLen == 0 ? null : buffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从InputStream中读取一行
     *
     * @param is
     * @return
     */
    public static String readLine(InputStream is) {
        try {
            String line = "";
            while (true) {
                int singleChar = is.read();
                if (singleChar == -1 || singleChar == 65535) {
                    break;
                } else {
                    line += String.valueOf((char) singleChar);
                    if (line.endsWith("\n")) {
                        break;
                    }
                }
            }
            return line.equals("") ? null : line.substring(0, line.length() - 2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从InputStream中读取一段，使用byte数组分割
     * 返回的byte数组中不包含分割byte数组的内容
     *
     * @param is
     * @param splitByte
     * @return
     */
    public static byte[] readWithSplit(InputStream is, byte[] splitByte) {
        try {
            byte[] resultBytes = new byte[0];
            while (true) {
                byte[] tempbyte = new byte[1];
                int readSize = is.read(tempbyte);
                if (readSize == -1 || readSize == 65535) {
                    break;
                } else {
                    resultBytes = StreamUtils.byteArrayConcat(resultBytes, resultBytes.length, tempbyte, tempbyte.length);
                }

                if (resultBytes.length >= splitByte.length) {
                    byte[] resultLastedBytes = Arrays.copyOfRange(resultBytes, resultBytes.length - splitByte.length, resultBytes.length);
                    if (Arrays.equals(resultLastedBytes, splitByte)) {
                        resultBytes = Arrays.copyOfRange(resultBytes, 0, resultBytes.length - splitByte.length);
                        break;
                    }
                }
            }
            return resultBytes.length == 0 ? null : resultBytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将first和last合并复制到一个新的byte数组当中
     *
     * @param first
     * @param firstLen
     * @param last
     * @param lastLen
     * @return
     */
    public static byte[] byteArrayConcat(byte[] first, int firstLen, byte[] last, int lastLen) {
        if (firstLen == 0) {
            return last;
        }
        if (lastLen == 0) {
            return first;
        }
        byte[] res = new byte[firstLen + lastLen];
        System.arraycopy(first, 0, res, 0, lastLen);
        System.arraycopy(last, 0, res, firstLen, lastLen);
        return res;
    }

    /**
     * 从InputStream中读取全部字节
     *
     * @param is
     * @return
     */
    @SuppressWarnings("all")
    public static byte[] readAll(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            while (true) {
                byte[] temp = new byte[1024];
                int readSize = bis.read(temp);
                if (readSize > 0) {
                    baos.write(temp, 0, readSize);
                } else if (readSize == -1 || readSize == 65535) {
                    break;
                }
            }
            bis.close();
            return baos.size() == 0 ? null : baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
