package noteedit.exception

class FileNotFoundException(msg: String) :
    ApiException(ApiExceptionCode.FILE_NOT_FOUND.code, msg)