package noteedit.exception

enum class ApiExceptionCode(val code: Int, val msg: String) {
    NAME_ALREADY_EXISTS(400, "File Already Exists"),
    ILLEGAL_PATH(400, "Illegal Path"),
    FILE_NOT_FOUND(500, "File Not Found"),
    ILLEGAL_PARAMETER(400, "Illegal Parameter"),
}