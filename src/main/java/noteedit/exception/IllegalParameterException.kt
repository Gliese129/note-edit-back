package noteedit.exception

class IllegalParameterException(msg: String) :
    ApiException(ApiExceptionCode.ILLEGAL_PARAMETER.code, msg)