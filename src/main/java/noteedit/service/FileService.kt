package noteedit.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import mu.KotlinLogging
import noteedit.entity.FileInfo
import noteedit.exception.NameAlreadyExistsException
import noteedit.mapper.FileMapper
import noteedit.utils.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class FileService: ServiceImpl<FileMapper, FileInfo>() {
    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var fileMapper: FileMapper
    /**
     * 添加文件信息
     * @param fileInfo 文件信息
     * @throws NameAlreadyExistsException 文件夹下已存在同名文件
     * @return 是否添加成功
     */
    open fun add(fileInfo: FileInfo): Boolean {
        logger.info { "adding file $fileInfo ..." }
        require(
            fileMapper.selectList(
                QueryWrapper<FileInfo>()
                    .eq("folder_id", fileInfo.folderId)
                    .eq("name", fileInfo.name)
            ).isNullOrEmpty()
        ) { NameAlreadyExistsException("该文件夹下已存在同名文件") }
        return fileMapper.insert(fileInfo) == 1
    }

    /**
     * 获取指定文件夹下的所有文件信息
     * @param folderId 文件夹id
     * @return 文件列表
     */
    open fun listAllInFolder(folderId: Long): List<FileInfo> {
        logger.info { "listing all files in folder $folderId ..." }
        return fileMapper.selectList(
            QueryWrapper<FileInfo>()
                .eq("folder_id", folderId)
        ) ?: emptyList()
    }

    /**
     * 根据id获取文件信息
     * @param id 文件id
     * @return 文件信息
     */
    open fun getById(id: Long): FileInfo? {
        logger.info { "getting file $id ..." }
        return fileMapper.selectById(id)
    }

    /**
     * 更新文件信息
     * @param fileInfo 新的文件信息
     * @return 是否更新成功
     */
    open fun update(fileInfo: FileInfo): Boolean {
        logger.info { "updating file $fileInfo ..." }
        return fileMapper.updateById(fileInfo) == 1
    }

    /**
     * 删除文件信息
     * @param id 文件id
     * @return 是否删除成功
     */
    open fun delete(id: Long): Boolean {
        logger.info { "deleting file $id ..." }
        return (fileMapper.deleteById(id) == 1).also {
            getById(id)?.let {
                FileUtils.deleteFile(it.realPath)
            }
        }
    }
}