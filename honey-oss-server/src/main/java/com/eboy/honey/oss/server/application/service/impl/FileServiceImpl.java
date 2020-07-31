package com.eboy.honey.oss.server.application.service.impl;

import com.eboy.honey.oss.server.application.BeanConvertUtil;
import com.eboy.honey.oss.server.application.dao.FileMapper;
import com.eboy.honey.oss.server.application.po.FilePo;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.FileShardService;
import com.eboy.honey.oss.server.application.vo.FileShardVo;
import com.eboy.honey.oss.server.application.vo.FileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yangzhijie
 * @date 2020/7/29 16:57
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileShardService fileShardService;

    @Autowired
    private FileMapper fileMapper;

    @Override
    public boolean addLocalFile(FileVo fileVo) {
        FilePo filePo = BeanConvertUtil.convert(fileVo, FilePo.class);
        return fileMapper.addFile(filePo);
    }

    @Override
    public List<FileVo> getFileByIds(List<String> ids) {
        List<FilePo> filePos = fileMapper.getFileByIds(ids);
        return convert(filePos);
    }

    @Override
    public List<FileVo> getFileByFileKeys(List<String> fileKeys) {
        List<FilePo> filePos = fileMapper.getFileByFileKeys(fileKeys);
        return convert(filePos);
    }

    @Override
    public boolean updateFileName(String fileId, String fileName) {
        return fileMapper.updateFileName(fileId, fileName);
    }

    @Override
    public boolean deleteFileByIds(List<String> ids) {
        return fileMapper.deleteFileByIds(ids);
    }

    @Override
    public boolean deletedFileByFileKeys(List<String> fileKeys) {
        return fileMapper.deletedFileByFileKeys(fileKeys);
    }

    /**
     * 将分片信息拼入文件并进行转化
     *
     * @param sources 文件源
     * @return 含有分片信息的文件
     */
    private List<FileVo> convert(List<FilePo> sources) {
        Map<String, List<FileShardVo>> rs = fileShardService.getFileShardInfoByFileKeys(
                sources.stream()
                        .map(FilePo::getFileKey)
                        .collect(Collectors.toList()));
        List<FileVo> fileVos = BeanConvertUtil.convertByList(sources, FileVo.class);
        fileVos.forEach(fileVo -> fileVo.setFileShardVos(rs.get(fileVo.getFileKey())));
        return fileVos;
    }

}
