package noteedit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderInfo {
    private Long id;

    private String folderName;

    private Long father;
    @TableField(exist = false)
    private List<Long> children;
    @TableField("children")
    private String childrenJson;
    private Integer depth;
}
