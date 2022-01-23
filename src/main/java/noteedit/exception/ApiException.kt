package noteedit.exception


open class ApiException(val code: Int, message: String) :
    RuntimeException(message)