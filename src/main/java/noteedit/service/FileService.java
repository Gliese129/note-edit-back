package noteedit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import noteedit.entity.FileInfo;
import noteedit.mapper.FileMapper;
import noteedit.utils.FileUtils;
import noteedit.utils.JsonUtils;
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
     * @param fileInfo 文件信息
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

    /**
     * 获取文件夹下的所有文件
     * @param folderId 文件夹id
     * @return 文件列表
     */
    public List<FileInfo> listAllFilesInFolder(Long folderId) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("folder_id", folderId);
        return fileMapper.selectList(queryWrapper);
    }

    /**
     * 根据id获取文件信息
     * @param id 文件id
     * @return 文件信息
     */
    public FileInfo getFileById(Long id) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return fileMapper.selectOne(queryWrapper);
    }

    /**
     * 更新文件信息
     * @param fileInfo 新的文件信息
     * @return 是否更新成功
     */
    public boolean updateFileInfo(FileInfo fileInfo) {
        int affectedRows = fileMapper.updateById(fileInfo);
        return affectedRows == 1;
    }

    /**
     * 删除文件
     * @param id 文件id
     * @return 是否删除成功
     */
    public boolean deleteFileById(Long id) {
        FileInfo fileInfo = getFileById(id);
        int affectedRows = fileMapper.deleteById(id);
        if(affectedRows == 1) {
            FileUtils.deleteFile(fileInfo.getRealPath());
            return true;
        }
        return false;
    }
}
