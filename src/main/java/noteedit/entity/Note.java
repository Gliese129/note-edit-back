package com.gliese.noteedit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long fileId;
    @TableField(value = "text")
    private String note;

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", fileId=" + fileId +
                ", note='" + (note.length() < 20 ? note.length() : (note.substring(0, 20) + "...")) + '\'' +
                '}';
    }
}
