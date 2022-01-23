package noteedit.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import mu.KotlinLogging
import noteedit.entity.FolderInfo
import noteedit.exception.IllegalPathException
import noteedit.mapper.FolderMapper
import noteedit.utils.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
open class FolderService: ServiceImpl<FolderMapper, FolderInfo>() {
    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var folderMapper: FolderMapper

    /**
     * 新建文件夹
     * @param locatedFolders 文件所在文件夹名称
     * @return 文件所在目录id
     */
    open fun addNewList(locatedFolders: List<String>):Long {
        val tot = folderMapper.selectCount(null) ?: 0
        return (folderMapper.selectList(
            QueryWrapper<FolderInfo>()
                .orderByAsc("depth")
                .`in`("name", locatedFolders)
        ) ?: emptyList()).let { existFolders ->
            FileUtils.listNewFolders(existFolders, locatedFolders, tot)
        }.also { newFolders ->
            this.saveOrUpdateBatch(newFolders, newFolders.size)
        }.let {
            if(it.size > 0) {
                logger.info { "added ${it.size - 1} folders, update 1 folder" }
                it.last().id
            } else {
                0
            }
        }
    }

    /**
     * 获取根目录
     * @return 根目录
     */
    open fun listRoot(): List<FolderInfo> {
        return folderMapper.selectList(
            QueryWrapper<FolderInfo>()
                .eq("depth", 1)
        ) ?: emptyList()
    }

    /**
     * 根据id获取文件夹
     * @param id 文件夹id
     * @return 文件夹
     */
    open fun getById(id: Long): FolderInfo? {
        return folderMapper.selectById(id)
    }

    /**
     * 根据ids批量获取文件夹
     * @param ids 文件夹id
     * @return 文件夹列表
     */
    open fun getListByIds(ids: MutableList<Long>): List<FolderInfo> {
        require(ids.isNotEmpty()) { "ids is empty" }
        return folderMapper.selectList(
            QueryWrapper<FolderInfo>()
                .`in`("id", ids)
        ) ?: emptyList()
    }

    /**
     * 根据路径获取末尾文件夹
     * @param parts 文件夹路径
     * @return 叶子文件夹
     * @throws IllegalPathException 路径错误
     */
    open fun getLeafFolderByPath(parts: List<String>): FolderInfo {
        val folders = folderMapper.selectList(
            QueryWrapper<FolderInfo>()
                .`in`("name", parts)
                .orderByAsc("depth")
        ) ?: emptyList()
        for(i in 1 until folders.size) {
            require(folders[i - 1].subfolders?.contains(folders[i].id) ?: false) {
                IllegalPathException("路径非链式")
            }
        }
        return folders.last()
    }
}