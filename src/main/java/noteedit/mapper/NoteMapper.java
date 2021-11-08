package com.gliese.noteedit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gliese.noteedit.entity.Note;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoteMapper extends BaseMapper<Note> {
}
