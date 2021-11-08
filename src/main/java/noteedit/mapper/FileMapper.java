package com.gliese.noteedit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gliese.noteedit.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<FileInfo> {
}
