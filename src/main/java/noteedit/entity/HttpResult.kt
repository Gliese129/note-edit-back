package noteedit.entity

data class HttpResult(var code: Int, // 状态码
                 var msg: String = "", // 提示信息
                 ) {
    var data: Any? = null // 响应数据

    constructor(code: Int, msg: String, data: Any) : this(code, msg) {
        this.data = data
    }
}