package noteedit.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import noteedit.entity.Note
import noteedit.mapper.NoteMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class NoteService {
    @Autowired
    private lateinit var noteMapper: NoteMapper

    open fun save(note: Note): Boolean {
        return noteMapper.selectOne(
            QueryWrapper<Note>().eq("file_id", note.fileId)
        )?.let {
            // update
            note.id = it.id
            noteMapper.updateById(note) == 1
        } ?: (noteMapper.insert(note) == 1) // insert
    }

    open fun getByFileId(fileId: Long): Note? {
        return noteMapper.selectOne(
            QueryWrapper<Note>().eq("file_id", fileId)
        )
    }
}