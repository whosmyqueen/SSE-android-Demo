package retrofit2


/**
 * 事件
 * @author zhengzhihui1.vendor
 * @date 2024/12/12
 * @constructor 创建[Event]
 * @param [id] id
 * @param [type] 类型
 * @param [data] 数据
 * @param [thr] 用力推
 */
data class Event(val id: String?, val type: String?, val data: String, val thr: Throwable? = null) {
    val failed: Boolean = thr != null
}