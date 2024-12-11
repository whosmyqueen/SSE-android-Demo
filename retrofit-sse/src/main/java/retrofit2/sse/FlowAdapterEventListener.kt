package retrofit2.sse

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import retrofit2.Event
import retrofit2.SseEvent

/**
 * Classname: FlowAdapterEventListener </p>
 * Created by Leobert on 2023/11/27.
 */
class FlowAdapterEventListener(
    val channel: Channel<SseEvent>,
) : EventSourceListener() {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onClosed(eventSource: EventSource) {
        super.onClosed(eventSource)
    }

    override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
        super.onEvent(eventSource, id, type, data)
        scope.launch {
            channel.send(SseEvent(event = Event(id, type, data), eventSource))
        }
    }

    override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
        super.onFailure(eventSource, t, response)
        scope.launch {
            channel.send(
                SseEvent(event = Event(null, null, "", t), eventSource)
            )
            channel.close(t)
        }
    }

    override fun onOpen(eventSource: EventSource, response: Response) {
        super.onOpen(eventSource, response)
    }
}