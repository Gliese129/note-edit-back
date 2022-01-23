package noteedit.exception

class NameAlreadyExistsException(msg: String) :
    ApiException(ApiExceptionCode.NAME_ALREADY_EXISTS.code, msg)