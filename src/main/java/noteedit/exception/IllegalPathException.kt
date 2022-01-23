package noteedit.exception

class IllegalPathException(msg: String) :
    ApiException(ApiExceptionCode.ILLEGAL_PATH.code, msg)