package noteedit.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId

class Note(
    @TableId(type = IdType.AUTO)
    var id: Long = 0, // 主键
    var fileId: Long = 0, // 对应文件id
    @TableField("text")
    var note: String = "" // 笔记内容
) {
    override fun toString(): String {
        return "Note(id=$id, fileId=$fileId, note='${note.let {
            if (it.length > 10) it.substring(0, 10) + "..." else it
        }}')"
    }
}