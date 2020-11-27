package red.honey.oss.api.entiy;

import java.io.OutputStream;
import java.io.Serializable;

/**
 * @author yangzhijie
 * @date 2020/8/21 10:38
 */
public class OutputMode implements Serializable {

    private static final long serialVersionUID = 4405061815568815455L;
    /**
     * 输出到指定的文件目录
     */
    private String filePath;

    /**
     * 输出指定的输出流
     */
    private OutputStream outputStream;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
