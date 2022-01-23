package noteedit

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@MapperScan("noteedit.mapper")
open class NoteEditApplication

fun main(args: Array<String>) {
    runApplication<NoteEditApplication>(*args)
}