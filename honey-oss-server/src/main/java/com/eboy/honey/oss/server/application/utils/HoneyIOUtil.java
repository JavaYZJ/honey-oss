package com.eboy.honey.oss.server.application.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author yangzhijie
 * @date 2020/8/26 16:14
 */
public class HoneyIOUtil {

    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }
}
