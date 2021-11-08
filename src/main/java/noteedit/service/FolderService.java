package com.gliese.noteedit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gliese.noteedit.entity.FileInfo;
import com.gliese.noteedit.entity.FolderInfo;
import com.gliese.noteedit.mapper.FolderMapper;
import com.gliese.noteedit.utils.BasicUtils;
import com.gliese.noteedit.utils.FileUtils;
import com.gliese.noteedit.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class FolderService extends ServiceImpl<FolderMapper, FolderInfo> {
    @Autowired
    private FolderMapper folderMapper;

    /**
     * 在数据库中创建新文件夹
     * @param fileInfo 文件信息
     * @return 文件所在目录id
     */
    public Long mkdirs(FileInfo fileInfo) {
        Long num = folderMapper.selectCount(new QueryWrapper<>());
        QueryWrapper<FolderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("depth").in("folder_name", fileInfo.getFolders());
        List<FolderInfo> existsFolders = folderMapper.selectList(queryWrapper);
        for(FolderInfo folder: existsFolders) {
            List<Integer> children = (List<Integer>) JsonUtils.jsonToObject(folder.getChildrenJson(), List.class);
            List<Long> children2 = new ArrayList<>();
            for(Integer child: children) {
                children2.add(child.longValue());
            }
            folder.setChildren(children2);
        }
        List<FolderInfo> newFolders = FileUtils.makeNewFolders(existsFolders, fileInfo.getFolders(), num);
        FolderInfo updateFolder = newFolders.get(newFolders.size() - 1);
        newFolders.remove(newFolders.size() - 1);
        if(updateFolder != null)
            folderMapper.updateById(updateFolder);
        this.saveOrUpdateBatch(newFolders, 100);
        if(newFolders.size() > 0) { // * 有新目录
            log.info("new folders made");
            return newFolders.get(newFolders.size() - 1).getId();
        }
        log.info("no new folders found");
        return updateFolder.getId(); // * 无新目录
    }

    /**
     * 获取根目录
     */
    public List<FolderInfo> listRoot() {
        QueryWrapper<FolderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("depth", 1);
        List<FolderInfo> result = folderMapper.selectList(queryWrapper);
        return result;
    }

    public FolderInfo getFolderById(Long id) {
        if(id == null)
            return null;
        QueryWrapper<FolderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        FolderInfo result = folderMapper.selectOne(queryWrapper);
        result.setChildren(BasicUtils.intToLongBatch((List<Integer>) JsonUtils.jsonToObject(result.getChildrenJson(), List.class)));
        return result;
    }
    public List<FolderInfo> getFoldersById(List<Long> ids) {
        if(ids == null || ids.size() == 0)
            return null;
        if(ids.size() == 1) {
            List<FolderInfo> result = new ArrayList<>();
            result.add(getFolderById(ids.get(0)));
            return result;
        }
        QueryWrapper<FolderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<FolderInfo> results = folderMapper.selectList(queryWrapper);
        if(results == null)
            return null;
        for(FolderInfo result: results) {
            result.setChildren(BasicUtils.intToLongBatch((List<Integer>) JsonUtils.jsonToObject(result.getChildrenJson(), List.class)));
        }
        return results;
    }

    public FolderInfo getLeafFolderByPath(List<String> path) {
        QueryWrapper<FolderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("folder_name", path);
        List<FolderInfo> results = folderMapper.selectList(queryWrapper);
        Map<Long, Integer> map = new HashMap<>();
        Queue<Integer> q = new LinkedList<>();
        int index = 0;
        for(FolderInfo result: results) {
            map.put(result.getId(), index++);
            result.setChildren(BasicUtils.intToLongBatch((List<Integer>) JsonUtils.jsonToObject(result.getChildrenJson(), List.class)));
            if(result.getDepth() == 1)
                q.add(index - 1);
        }
        int depth = 0;
        FolderInfo leaf = null;
        while(!q.isEmpty()) {
            int top = q.poll();
            FolderInfo folder = results.get(top);
            if(folder.getFolderName().equals(path.get(depth))) {
                depth++;
                leaf = folder;
                for(Long childId: folder.getChildren())
                    if(map.containsKey(childId))
                        q.add(map.get(childId));
            }
        }
        return leaf;
    }
}
