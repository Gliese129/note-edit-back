package com.gliese.noteedit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gliese.noteedit.entity.Note;
import com.gliese.noteedit.mapper.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    @Autowired
    private NoteMapper noteMapper;

    public void saveNote(Note note) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", note.getFileId());
        Note getNote = noteMapper.selectOne(queryWrapper);
        if(getNote == null) { // * 新笔记
            noteMapper.insert(note);
        } else { // * 更新笔记
            note.setId(getNote.getId());
            noteMapper.updateById(note);
        }
    }

    public Note getNoteByFile(Long fileId) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        return noteMapper.selectOne(queryWrapper);
    }
}
