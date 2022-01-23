package noteedit.utils

import mu.KotlinLogging
import noteedit.entity.FileInfo
import noteedit.entity.FolderInfo
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.util.*

object FileUtils {
    private const val root = "C:\\files"
    private val logger = KotlinLogging.logger {}

    /**
     * 保存上传的文件
     * @param file 文件
     * @return 文件路径
     */
    fun saveFile(file: MultipartFile): String {
        val type = file.name.substring(file.name.lastIndexOf("."))
        val dest = File(root, "${UUID.randomUUID()}.$type")
        if(File(root).exists().not())
            File(root).mkdirs()
        try {
            file.transferTo(dest)
            logger.info("upload ${file.name} success")
            logger.debug("${file.name} -> ${dest.name}")
        } catch (e: IOException) {
            logger.error(e.toString())
        }
        return dest.path
    }

    /**
     * 列出需要新建的文件夹
     * @param allFolders 所有文件夹
     * @param locatedFolders 需添加文件所在的文件夹
     * @param lstId 最后一个文件夹的id
     */
    fun listNewFolders(allFolders: List<FolderInfo>, locatedFolders: List<String>, lstId: Long): MutableList<FolderInfo> {
        val q = LinkedList<Pair<FolderInfo, Int>>() // pair(id, depth)
        allFolders.forEach {
            if(it.depth == 1)
                q.add(it to 1)
        }
        //  查找最后一个已存在的文件夹
        var lstExistFolder: FolderInfo? = null
        var lstIndex = -1
        while(!q.isEmpty()) {
            val (father, depth) = q.poll()
            if(depth == locatedFolders.size) { // 全部路径都已经存在
                lstExistFolder = father
                lstIndex = locatedFolders.size - 1
                break
            }
            if(locatedFolders.getOrNull(depth - 1) == father.name) { // 存在对应深度的文件夹
                lstExistFolder = father
                lstIndex = depth - 1
                father.subfolders?.forEach {
                    q.add(Pair(allFolders[it.toInt() - 1], depth + 1))
                }
            }
        }
        // 添加新的目录
        val newFolders = lstExistFolder?.let { mutableListOf(it) } ?: mutableListOf()
        var index = lstId + 1
        locatedFolders.forEachIndexed { i, locatedFolder ->
            FolderInfo(id=++index, name=locatedFolder, depth=i + 1)
                .also {
                    it.subfolders = mutableListOf()
                    it.fatherId = lstExistFolder?.id ?: 0
                    lstExistFolder?.subfolders?.add(index)
                    lstExistFolder = it
                    newFolders.add(it)
                }
        }
        logger.debug("find ${newFolders.size} new folders")
        return newFolders
    }

    /**
     * 将文件夹信息转成文件信息
     */
    fun foldersInfoToFilesInfo(folders: MutableList<FolderInfo>): MutableList<FileInfo> {
        val files = mutableListOf<FileInfo>()
        folders.forEach {
            files.add(FileInfo(id=0, name=it.name, folderId=it.id, type="folder"))
        }
        return files
    }

    /**
     * 删除文件
     * @param filePath 文件路径
     */
    fun deleteFile(filePath: String): Boolean {
        val file = File(filePath)
        return file.delete()
    }
}