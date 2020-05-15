package org.codwh.common.util;

import java.io.File;

public class EnvironmentUtils {

    public static String getSystemPath(String absolutePath) {
        return getContextPath() + File.separator + absolutePath;
    }

    public static String getContextPath() {
        return System.getProperty("user.dir");
    }
}
