package noteedit.controller;

import noteedit.entity.Back;
import noteedit.entity.FileInfo;
import noteedit.entity.Note;
import noteedit.service.NoteService;
import noteedit.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
@Slf4j
@Api(value = "笔记相关操作api", tags = { "笔记" })
public class NoteController {
    @Autowired
    private NoteService noteService;

    @PostMapping("/save")
    @ApiOperation(value = "保存笔记", notes = "只保存text文本，预览由前端配置")
    public Back saveNote(@RequestBody Note note) {
        log.info("saving note...");
        log.info("attach file id: " + note.getFileId());
        noteService.saveNote(note);
        return new Back(200, "save/update success");
    }

    @GetMapping("/get-note-by-file")
    @ApiOperation(value = "根据文件id获取对应笔记", notes = "")
    public Back getNoteByFile(@RequestParam("fileId") Long fileId) {
        log.info("fetching note...");
        log.info("attach file id: " + fileId);
        Note note = noteService.getNoteByFile(fileId);
        log.info("note info: " + note);
        return new Back(200, "fetch success", note);
    }
}
