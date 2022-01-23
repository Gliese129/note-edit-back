package noteedit.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import noteedit.entity.FileInfo
import org.apache.ibatis.annotations.Mapper

@Mapper
interface FileMapper: BaseMapper<FileInfo>