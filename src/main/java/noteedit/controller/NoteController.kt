package noteedit.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mu.KotlinLogging
import noteedit.entity.HttpResult
import noteedit.entity.Note
import noteedit.service.NoteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/note")
@Api(value = "笔记操作", description = "笔记操作api")
open class NoteController {
    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var noteService: NoteService

    @PostMapping("/save")
    @ApiOperation(value = "保存笔记", notes = "只保存text文本，预览由前端配置")
    open fun save(@RequestBody note: Note): HttpResult {
        logger.info("saving note of file ${note.fileId}...")
        noteService.save(note)
        return HttpResult(200, "保存/更新成功")
    }

    @GetMapping("/get-by-file-id")
    @ApiOperation(value = "根据文件id获取对应笔记")
    open fun getNoteByFile(@RequestParam("fileId") fileId: Long): HttpResult {
        logger.info("fetching note of file $fileId...")
        return noteService.getByFileId(fileId)?.let {
            HttpResult(200, "获取成功", it)
        } ?: HttpResult(404, "获取失败")
    }
}