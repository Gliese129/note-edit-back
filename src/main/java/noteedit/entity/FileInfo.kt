package noteedit.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId

data class FileInfo (
    var name: String = "", // 文件名
    var type: String = "", // 文件类型
    var folderId: Long = 0, // 所在文件夹id
) {
    @TableId(type = IdType.AUTO)
    var id: Long = 0 // 主键
    @TableField(exist = false)
    var folders = mutableListOf<String>() // 所在文件夹
    var realPath = "" // 文件路径

    /**
     * 根据给定路径创建文件信息
     * @param path 文件路径
     */
    constructor(path: String) : this() {
        val parts = path.split(Regex("[/\\\\]"))
        val (fileName, fileType) = parts.last().split(Regex("\\."))
        this.folders = parts.dropLast(1).filter { it.isNotEmpty() && it.isNotBlank() }.toMutableList()
        this.name = fileName
        this.type = fileType
    }

    constructor(id: Long, name: String, type: String, folderId: Long) : this(name, type, folderId) {
        this.id = id
    }
}