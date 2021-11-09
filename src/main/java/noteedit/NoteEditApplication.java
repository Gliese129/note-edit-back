package noteedit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("noteedit.mapper")
public class NoteEditApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteEditApplication.class, args);
    }

}
