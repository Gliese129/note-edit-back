package com.gliese.noteedit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gliese.noteedit.entity.FileInfo;
import com.gliese.noteedit.mapper.FileMapper;
import com.gliese.noteedit.utils.FileUtils;
import com.gliese.noteedit.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileService extends ServiceImpl<FileMapper, FileInfo> {
    @Autowired
    private FileMapper fileMapper;

    /**
     * 将文件信息放进数据库
     * @param fileInfo
     */
    public boolean linkFileToDatabase(FileInfo fileInfo) {
        log.info("linking files to database...");
        log.info("file info: " + fileInfo.toString());
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("folder_id", fileInfo.getFolderId());
        List<FileInfo> files = fileMapper.selectList(queryWrapper);
        for(FileInfo file: files) {
            if(file.getFileName().equals(fileInfo.getFileName())) { // * 重名，返回false
                return false;
            }
        }
        fileMapper.insert(fileInfo);
        return true;
    }

    public List<FileInfo> listAllFilesInFolder(Long folderId) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("folder_id", folderId);
        return fileMapper.selectList(queryWrapper);
    }

    public FileInfo getFileById(Long id) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return fileMapper.selectOne(queryWrapper);
    }
}
