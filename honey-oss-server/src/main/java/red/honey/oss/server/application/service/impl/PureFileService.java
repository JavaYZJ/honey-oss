package red.honey.oss.server.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import red.honey.oss.api.dto.FileDto;
import red.honey.oss.api.dto.FileShardDto;
import red.honey.oss.server.application.dao.FileMapper;
import red.honey.oss.server.application.dao.FileShardMapper;
import red.honey.oss.server.application.po.FilePo;
import red.honey.oss.server.application.po.FileShardPo;
import red.honey.oss.server.application.utils.BeanConvertUtil;

/**
 * @author yangzhijie
 * @date 2020/11/4 14:18
 */
@Service
public class PureFileService {

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private FileShardMapper fileShardMapper;


    @Transactional(rollbackFor = Exception.class)
    public Boolean postFileInfo(FileDto fileDto) {
        FilePo filePo = BeanConvertUtil.convert(fileDto, FilePo.class);
        return fileMapper.addFile(filePo);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean postFileShardInfo(FileShardDto fileShardDto) {
        FileShardPo shardPo = BeanConvertUtil.convert(fileShardDto, FileShardPo.class);
        return fileShardMapper.addFileShard(shardPo);
    }
}
