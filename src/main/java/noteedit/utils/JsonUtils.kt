package noteedit.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.org.apache.bcel.internal.classfile.JavaClass
import kotlin.reflect.KClass

/**
 * 封装了Jackson的转换方法
 */
object JsonUtils {
    fun objectToJson(obj: Any?): String? {
        val mapper = ObjectMapper()
        var json: String? = null
        try {
            json = mapper.writeValueAsString(obj)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
        return json
    }

    inline fun <reified T> jsonToObject(json: String): T? {
        val mapper = ObjectMapper()
        var obj: T? = null
        try {
            obj = mapper.readValue(json, T::class.java)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
        return obj
    }
}