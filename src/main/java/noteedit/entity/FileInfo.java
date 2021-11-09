package noteedit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String fileName;
    private String fileType;
    @TableField(exist = false)
    private List<String> folders;
    private String realPath;
    private Long folderId;

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", folders=" + folders +
                ", realPath='" + realPath + '\'' +
                ", folderId=" + folderId +
                '}';
    }

    /**
     * 根据路劲分离文件信息
     * @param path
     */
    public FileInfo(String path) {
        String[] pathPart = path.split("[/\\\\]");
        int length = pathPart.length;
        String filename = pathPart[length - 1];
        List<String> folders = new ArrayList<>();
        for(String part: pathPart) {
            if(!part.equals(""))
                folders.add(part);
        }

        folders.remove(filename);
        String temp[] = filename.split("\\.");

        this.fileType = temp[temp.length - 1];
        this.fileName = temp[0];
        this.folders = folders;
    }
}
