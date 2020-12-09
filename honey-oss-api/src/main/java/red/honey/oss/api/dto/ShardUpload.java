package red.honey.oss.api.dto;

import java.io.File;
import java.io.Serializable;

/**
 * @author yangzhijie
 * @date 2020/8/14 14:00
 */
public class ShardUpload implements Serializable {

    private static final long serialVersionUID = 3288038097509081433L;

    /**
     * file
     */
    private File file;
    /**
     * 分片文件名
     */
    private String shardName;
    /**
     * 当前分片
     */
    private int shardIndex;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getShardName() {
        return shardName;
    }

    public void setShardName(String shardName) {
        this.shardName = shardName;
    }

    public int getShardIndex() {
        return shardIndex;
    }

    public void setShardIndex(int shardIndex) {
        this.shardIndex = shardIndex;
    }
}
