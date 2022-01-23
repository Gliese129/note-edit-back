package noteedit.entity

import com.baomidou.mybatisplus.annotation.TableField
import noteedit.utils.JsonUtils

data class FolderInfo(
    var id: Long = 0, // 编号
    var name: String = "", // 文件夹名称
    var depth: Int = 0, // 层级
) {
    @TableField("father")
    var fatherId: Long = 0 // 父级编号
    @TableField(exist = false)
    var subfolders: MutableList<Long>? = null // 子文件夹
    @TableField("children")
    var subfolderJson: String? = "" // 子文件夹(json)
        get() = JsonUtils.objectToJson(subfolders)
        set(value) {
            if (value?.isNotBlank() == true) {
                subfolders = JsonUtils.jsonToObject<MutableList<Long>>(value)
                field = value
            }
        }
}