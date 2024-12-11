package retrofit2

import okhttp3.sse.EventSource

/**
 * 上交所事件
 * @author zhengzhihui1.vendor
 * @date 2024/12/11
 * @constructor 创建[SseEvent]
 * @param [event] 事件
 * @param [eventSource] 事件源
 */
data class SseEvent(var event: Event, var eventSource: EventSource)