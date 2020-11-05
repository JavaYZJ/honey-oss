package com.eboy.honey.oss.server.application.service.impl;

import com.eboy.honey.oss.api.dto.FileDto;
import com.eboy.honey.oss.api.dto.FileShardDto;
import com.eboy.honey.oss.server.application.dao.FileMapper;
import com.eboy.honey.oss.server.application.dao.FileShardMapper;
import com.eboy.honey.oss.server.application.po.FilePo;
import com.eboy.honey.oss.server.application.po.FileShardPo;
import com.eboy.honey.oss.server.application.utils.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
