package red.honey.oss.server.api.rpc.dubbo.impl;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import red.honey.oss.api.dto.FileDto;
import red.honey.oss.api.dto.FileShardDto;
import red.honey.oss.api.service.dubbo.PureFileRpcService;
import red.honey.oss.server.application.service.impl.PureFileService;

import javax.validation.Valid;

/**
 * @author yangzhijie
 * @date 2020/11/4 14:50
 */
@Service(version = "1.0")
public class PostFileRpcServiceImpl implements PureFileRpcService {

    @Autowired
    private PureFileService fileService;

    /**
     * 保存文件实体信息
     *
     * @param fileDto 文件传输层实体
     * @return 是否成功
     */
    @Override
    public Boolean postFileInfo(@Valid FileDto fileDto) {
        return fileService.postFileInfo(fileDto);
    }

    /**
     * 保存文件分片信息
     *
     * @param fileShardDto 分片实体
     * @return 是否成功
     */
    @Override
    public Boolean postFileShardInfo(@Valid FileShardDto fileShardDto) {
        return fileService.postFileShardInfo(fileShardDto);
    }


}
