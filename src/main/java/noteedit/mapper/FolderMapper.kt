package noteedit.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import noteedit.entity.FolderInfo
import org.apache.ibatis.annotations.Mapper

@Mapper
interface FolderMapper: BaseMapper<FolderInfo>