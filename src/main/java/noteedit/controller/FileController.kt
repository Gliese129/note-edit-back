package noteedit.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mu.KotlinLogging
import noteedit.entity.FileInfo
import noteedit.entity.HttpResult
import noteedit.exception.FileNotFoundException
import noteedit.exception.IllegalParameterException
import noteedit.exception.NameAlreadyExistsException
import noteedit.service.FileService
import noteedit.service.FolderService
import noteedit.utils.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/file")
@Api(value = "文件操作", description = "文件操作api")
open class FileController {
    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var fileService: FileService
    @Autowired
    lateinit var folderService: FolderService

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", notes = "保证文件大小不超过10M，文件格式正确，且必须放置在文件夹下")
    open fun uploadFile(@RequestParam("file") file: MultipartFile, @RequestParam("path") path: String): HttpResult {
        logger.info("uploading file ${file.name} ...")
        val fileInfo = FileInfo(path)
        fileInfo.realPath = FileUtils.saveFile(file)
        fileInfo.folderId = folderService.addNewList(fileInfo.folders)
        fileService.add(fileInfo).let {
            require(it) { NameAlreadyExistsException("文件名已存在") }
        }
        return HttpResult(200, "上传成功")
    }

    @GetMapping("/list-sub")
    @ApiOperation(value = "获取子目录和文件", notes = "获取指定目录下的所有子目录和文件")
    open fun listSub(@RequestParam("folderId") rootId: Long): HttpResult {
        val root = folderService.getById(rootId)
        require(root != null) { FileNotFoundException("指定的文件夹不存在") }
        logger.info("fetching sub folders and files of ${root.name}(depth ${root.depth})...")
        val subs = folderService.getListByIds(root.subfolders ?: mutableListOf()).let {
            logger.info("found ${it.size} sub folders")
            FileUtils.foldersInfoToFilesInfo(it.toMutableList())
        }.addAll(
            fileService.listAllInFolder(rootId).let {
                logger.info {
                    if (it.isNotEmpty())
                        "found ${it.size} files"
                    else "no sub file found"
                }
                it.toMutableList()
            }
        )
        return HttpResult(200, "获取子文件和子目录成功", subs)
    }

    @GetMapping("/get-by-id")
    @ApiOperation(value = "根据文件id获取相应文件信息", notes = "id应该为大于0的整数，前端校验。realpath中的C:\\files\\会被替换为\\pdf-file")
    open fun getFileById(@RequestParam("id") id: Long): HttpResult {
        logger.info("fetching file $id...")
        require(id > 0) { IllegalParameterException("id应该大于0") }
        return fileService.getById(id)?.also {
            it.realPath = it.realPath.replace("C:\\files\\", "\\pdf-file")
        }?.let {
            HttpResult(200, "获取文件成功", it)
        } ?: HttpResult(404, "文件不存在")
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新文件信息")
    open fun update(@RequestBody fileInfo: FileInfo): HttpResult {
        logger.info("updating file ${fileInfo.id} info...")
        require(fileInfo.id > 0) { IllegalParameterException("id不合法") }
        return fileService.update(fileInfo).let {
            if(it)
                HttpResult(200, "更新文件信息成功")
            else HttpResult(500, "更新文件信息失败")
        }
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除文件")
    open fun delete(@RequestParam("id") id: Long): HttpResult {
        logger.info("deleting file $id...")
        require(id > 0) { IllegalParameterException("id不合法") }
        return fileService.delete(id).let {
            if(it)
                HttpResult(200, "删除文件成功")
            else HttpResult(500, "删除文件失败")
        }
    }
}
