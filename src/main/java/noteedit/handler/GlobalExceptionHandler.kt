package noteedit.handler

import mu.KotlinLogging
import noteedit.entity.HttpResult
import noteedit.exception.ApiException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
open class GlobalExceptionHandler {
    private val logger = KotlinLogging.logger {}

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(request: HttpServletRequest, ex: Exception): HttpResult {
        return if(ex is ApiException) {
            logger.warn (ex.message, ex)
            HttpResult(ex.code, ex.message ?: "")
        } else {
            logger.error (ex.message, ex)
            HttpResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.message ?: "")
        }
    }
}