package com.gliese.noteedit.utils;

import com.gliese.noteedit.entity.FileInfo;
import com.gliese.noteedit.entity.FolderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
public class FileUtils {

    public static final String folderPath = "C:\\files";

    /**
     * 将上传文件保存到指定文件夹
     * @param file 文件
     * @param fileType 文件类型
     * @return 文件路劲
     */
    public static String saveFile(MultipartFile file, String fileType) {
        String fileName = UUID.randomUUID().toString() + "." + fileType;
        File dest = new File(folderPath, fileName);
        if(!dest.getParentFile().exists())
            dest.getParentFile().mkdirs();
        try {
            file.transferTo(dest);
            log.info("upload success, file: " + fileName + " in folder: " + folderPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return folderPath + "\\" + fileName;
    }

    /**
     * 新建文件夹至数据库
     * @param allFolders 数据库内已有的文件夹
     * @param myFolders 文件所在目录
     * @param totNum 一共的文件夹数量
     * @return
     */
    public static List<FolderInfo> makeNewFolders(List<FolderInfo> allFolders, List<String> myFolders, Long totNum) {
        Queue<Long> q = new LinkedList<>(); // * bfs队列
        Map<Long, Integer> map = new HashMap<>(); // * id-index键值对
        for (int i = 0; i < allFolders.size(); ++i) {
            FolderInfo folder = allFolders.get(i);
            map.put(folder.getId(), i);
            if(folder.getDepth() == 1)
                q.add(folder.getId());
        }
        // * bfs查找最后存在的目录
        List<FolderInfo> newFolders = new ArrayList<>();
        FolderInfo lastExistFolder = null;
        while(!q.isEmpty()) {
            Long fatherId = q.poll();
            if(map.containsKey(fatherId)) {
                FolderInfo fatherNode = allFolders.get(map.get(fatherId));
                int depth = fatherNode.getDepth();
                if (fatherNode.getFolderName().equals(myFolders.get(depth - 1))) {
                    lastExistFolder = fatherNode;
                    List<Long> childrenId = fatherNode.getChildren();
                    for (Long childId : childrenId)
                        q.add(childId);
                }
            }
        }
        // * 添加新目录至队列
        int startPos = lastExistFolder == null ? 0 : lastExistFolder.getDepth();
        Long nowId = new Long(totNum);
        for(int i = startPos; i < myFolders.size(); ++i) {
            FolderInfo newFolder = new FolderInfo();
            newFolder.setFolderName(myFolders.get(i));
            newFolder.setId(++nowId);
            newFolder.setChildren(new ArrayList<>());
            newFolders.add(newFolder);
        }
        // * 目录填充father和children
        if(newFolders.size() > 0) {
            FolderInfo father = lastExistFolder;
            for(int i = 0; i < newFolders.size(); ++i) {
                FolderInfo node = newFolders.get(i);
                if(father == null) { // * 无父节点
                    node.setFather(0L);
                    node.setDepth(1);
                } else {
                    node.setFather(father.getId());
                    node.setDepth(father.getDepth() + 1);
                    father.getChildren().add(node.getId());
                }
                father = node;
            }
        }
        newFolders.add(lastExistFolder);
        // * children转化为json
        for(FolderInfo folder: newFolders) {
            if(folder != null)
                folder.setChildrenJson(JsonUtils.objectToJson(folder.getChildren()));
        }
        return newFolders;
    }

    /**
     * folder info -> file info
     * @param folders
     * @return
     */
    public static List<FileInfo> foldersToFiles(List<FolderInfo> folders) {
        if(folders == null)
            return new ArrayList<>();
        List<FileInfo> files = new ArrayList<>();
        for(FolderInfo folder: folders) {
            FileInfo file = new FileInfo();
            file.setFileType("folder");
            file.setFileName(folder.getFolderName());
            file.setFolderId(folder.getId());
            files.add(file);
        }
        return files;
    }

    /**
     * 删除文件
     * @param realPath
     */
    public static void deleteFile(String realPath) {
        File file = new File(realPath);
        file.delete();
    }
}
