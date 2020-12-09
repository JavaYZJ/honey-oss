package red.honey.oss.api.service.dubbo;

import red.honey.oss.api.dto.FileDto;
import red.honey.oss.api.dto.FileShardDto;

/**
 * @author yangzhijie
 * @date 2020/11/4 14:40
 */
public interface PureFileRpcService {

    /**
     * 保存文件实体信息
     *
     * @param fileDto 文件传输层实体
     * @return 是否成功
     */
    Boolean postFileInfo(FileDto fileDto);

    /**
     * 保存文件分片信息
     *
     * @param fileShardDto 分片实体
     * @return 是否成功
     */
    Boolean postFileShardInfo(FileShardDto fileShardDto);
}
