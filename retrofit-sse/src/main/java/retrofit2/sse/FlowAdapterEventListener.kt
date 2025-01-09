package retrofit2.sse

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import retrofit2.Event
import retrofit2.SseEvent

/**
 * 流适配器事件侦听器
 * @author zhengzhihui1.vendor
 * @date 2024/12/12
 * @constructor 创建[FlowAdapterEventListener]
 * @param [channel] 通道
 */
class FlowAdapterEventListener(
    val channel: Channel<Event>,
) : EventSourceListener() {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onClosed(eventSource: EventSource) {
        super.onClosed(eventSource)
    }

    override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
        scope.launch {
            channel.send(Event(id, type, data))
        }
        super.onEvent(eventSource, id, type, data)
    }

    override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
        scope.launch {
            channel.send(Event(null, null, response?.body()?.string() ?: "", t))
            channel.close(t)
        }
        super.onFailure(eventSource, t, response)
    }

    override fun onOpen(eventSource: EventSource, response: Response) {
        super.onOpen(eventSource, response)
    }
}