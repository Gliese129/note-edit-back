package noteedit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import noteedit.entity.Note;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoteMapper extends BaseMapper<Note> {
}
