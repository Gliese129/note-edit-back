package com.gliese.noteedit.controller;

import com.gliese.noteedit.entity.Back;
import com.gliese.noteedit.entity.FileInfo;
import com.gliese.noteedit.entity.FolderInfo;
import com.gliese.noteedit.service.FileService;
import com.gliese.noteedit.service.FolderService;
import com.gliese.noteedit.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@Slf4j
@Api(value = "文件和文件夹相关操作api", tags = { "文件&文件夹" })
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private FolderService folderService;

    @PostMapping("/file/upload")
    @ApiOperation(value="文件上传", notes="保证文件大小不超过10M，文件格式正确，且必须放置在文件夹下")
    public Back upload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        log.info("uploading files...");
        if(file == null)
            return new Back(400, "no available file found");
        // * 获取上传文件信息
        FileInfo fileInfo = new FileInfo(path);
        // * 保存
        String realPath = FileUtils.saveFile(file, fileInfo.getFileType());
        fileInfo.setRealPath(realPath);
        // * 将数据保存到数据
        fileInfo.setFolderId(folderService.mkdirs(fileInfo));
        boolean success = fileService.linkFileToDatabase(fileInfo);
        if(!success) {
            FileUtils.deleteFile(fileInfo.getRealPath());
            return new Back(500, "file with the same name exists");
        }
        return new Back(200, "upload success");
    }

    @GetMapping("/file/list-root-folders")
    @ApiOperation(value = "获取根目录", notes = "获取所有根目录，最终以FileInfo类返回")
    public Back listRoot() {
        log.info("fetching root folders...");
        List<FileInfo> roots = FileUtils.foldersToFiles(folderService.listRoot());
        log.info(roots == null ? "no folders found" : ("root folders num: " + roots.size()));
        return new Back(200, "fetch root", roots);
    }

    @GetMapping("/file/list-all-children")
    @ApiOperation(value = "获取目录下的子目录和文件", notes="列出指定目录下所有目录和文件，最终以FileInfo类返回")
    public Back listChildren(@RequestParam("folderId") Long folderId) {
        List<FileInfo> children = new ArrayList<>();
        FolderInfo folderInfo = folderService.getFolderById(folderId);
        List<FileInfo> tmp;
        // * 获取子目录
        log.info("fetching children folders...");
        List<Long> childrenId = folderInfo.getChildren();
        tmp = FileUtils.foldersToFiles(folderService.getFoldersById(childrenId));
        if(tmp != null) {
            log.info("fetch success, num: " + tmp.size());
            children.addAll(tmp);
        }
        // * 获取文件
        log.info("fetching children files...");
        tmp = fileService.listAllFilesInFolder(folderId);
        if(tmp != null) {
            log.info("fetch success, num: " + tmp.size());
            children.addAll(tmp);
        }
        return new Back(200, "fetch success", children);
    }

    @PostMapping("/file/list-all-folders-by-path")
    @ApiOperation(value = "根据路径获取子目录", notes = "仅仅会列出目录，如果不存在则返回空数组")
    public Back listFoldersByPath(@RequestBody List<String> path) {
        log.info("path: " + path + " fetching leaf...");
        if(path == null || path.size() == 0)
            return listRoot();
        FolderInfo folderInfo = folderService.getLeafFolderByPath(path);
        if(folderInfo == null) {
            log.info("no leaf found, maybe there are some nonexistent folders");
            return new Back(200, "no data", new ArrayList<>());
        }
        log.info("leaf folder: " + folderInfo.toString());
        List<FileInfo> result = FileUtils.foldersToFiles(folderService.getFoldersById(folderInfo.getChildren()));
        return new Back(200, "success", result);
    }

    @GetMapping("/file/get-file-by-id")
    @ApiOperation(value = "根据文件id获取相应文件信息", notes = "id应该为大于0的整数，前端校验。realpath中的C:\\files\\会被替换为\\pdf-file")
    public Back getFileById(@RequestParam("id") Long id) {
        log.info("fetching file...\nid = " + id);
        if(id < 0)
            return new Back(400, "illegal file id");
        FileInfo result = fileService.getFileById(id);
        if(result != null)
            result.setRealPath(result.getRealPath().replace("C:\\files", "pdf-file"));
        return new Back(200, "success", result);
    }
}
