package noteedit.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mu.KotlinLogging
import noteedit.entity.HttpResult
import noteedit.service.FileService
import noteedit.service.FolderService
import noteedit.utils.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/folder")
@Api(value = "文件夹操作", description = "文件夹操作api")
open class FolderController {
    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var folderService: FolderService

    @GetMapping("/list-root")
    @ApiOperation(value = "获取根目录", notes = "获取所有根目录文件夹")
    open fun listRoot(): HttpResult {
        logger.info("fetching root folders...")
        val roots = folderService.listRoot().let {
            logger.info {
                if(it.isNotEmpty())
                    "fetch root folders success, size: ${it.size}"
                else "no root folder found"
            }
            FileUtils.foldersInfoToFilesInfo(it.toMutableList())
        }
        return HttpResult(200, "获取根目录成功", roots)
    }

    @PostMapping("list-sub-folders-by-path")
    @ApiOperation(value = "获取根据路径获取子目录", notes = "仅仅会列出目录，如果不存在则返回空数组")
    open fun listSubFoldersByPath(@RequestBody paths: MutableList<String>): HttpResult {
        if(paths.size == 0)
            return listRoot()
        logger.info { "fetching sub folders by path: $paths" }
        val subFolders = folderService.getLeafFolderByPath(paths).let {
            if(it.subfolders.isNullOrEmpty())
                mutableListOf()
            else folderService.getListByIds(it.subfolders!!).toMutableList()
        }.let {
            FileUtils.foldersInfoToFilesInfo(it)
        }
        return HttpResult(200, "获取子目录成功", subFolders)
    }


}