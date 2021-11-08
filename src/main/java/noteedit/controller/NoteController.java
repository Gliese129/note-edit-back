package com.gliese.noteedit.controller;

import com.gliese.noteedit.entity.Back;
import com.gliese.noteedit.entity.FileInfo;
import com.gliese.noteedit.entity.Note;
import com.gliese.noteedit.service.NoteService;
import com.gliese.noteedit.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(value = "笔记相关操作api", tags = { "笔记" })
public class NoteController {
    @Autowired
    private NoteService noteService;

    @PostMapping("/note/save-note")
    @ApiOperation(value = "保存笔记", notes = "只保存text文本，预览由前端配置")
    public Back saveNote(@RequestBody Note note) {
        log.info("saving note...");
        log.info("attach file id: " + note.getFileId());
        noteService.saveNote(note);
        return new Back(200, "save/update success");
    }

    @GetMapping("/note/get-note-by-file-id")
    @ApiOperation(value = "根据文件id获取对应笔记", notes = "")
    public Back getNoteByFile(@RequestParam("fileId") Long fileId) {
        log.info("fetching note...");
        log.info("attach file id: " + fileId);
        Note note = noteService.getNoteByFile(fileId);
        log.info("note info: " + note);
        return new Back(200, "fetch success", note);
    }
}
