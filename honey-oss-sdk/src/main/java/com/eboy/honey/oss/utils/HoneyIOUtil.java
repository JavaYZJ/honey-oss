package com.eboy.honey.oss.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author yangzhijie
 * @date 2020/11/4 10:57
 */
@Slf4j
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
