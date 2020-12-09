package red.honey.oss.api.utils;


import java.io.File;

/**
 * @author yangzhijie
 * @date 2020/8/26 13:58
 */
public class FilePathUtil {

    /**
     * 获取当前系统盘符
     */
    public final static String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");

    /**
     * 获取当前系统主目录
     */
    public final static String USER_HOME = System.getProperties().getProperty("user.home");
    /**
     * 默认存储缩略图的目录名
     */
    public final static String DEFAULT_THUMBNAIL = "default_thumbnail";

    /**
     * 获取默认目录路径
     *
     * @return 缩略图默认目录路径
     */
    public static String defaultPath() {
        File home = new File(USER_HOME + FILE_SEPARATOR + DEFAULT_THUMBNAIL);
        synchronized (FilePathUtil.class) {
            if (!home.exists()) {
                home.mkdir();
            }
        }
        return home.getAbsolutePath();
    }

    /**
     * 获取默认缩略图存储路径
     *
     * @return 缩略图存储路径
     */
    public static String defaultThumbnailPath() {
        String defaultPath = defaultPath();
        return defaultPath + FILE_SEPARATOR + HoneyFileUtil.get32Uid() + ".";
    }
}
