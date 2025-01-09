package retrofit2.sse

import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.sse.RealEventSource
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener


object EventSourcesExt {
    @JvmStatic
    fun createFactory(client: OkHttpClient): EventSource.Factory {
        return EventSource.Factory { request, listener ->
            val actualRequest =
                if (request.header("Accept") == null) {
                    request.newBuilder().addHeader("Accept", "text/event-stream").build()
                } else {
                    request
                }

            RealEventSourceExt(actualRequest, listener).apply {
                connect(client)
            }
        }
    }

    @JvmStatic
    fun processResponse(response: Response, listener: EventSourceListener) {
        val eventSource = RealEventSourceExt(response.request, listener)
        eventSource.processResponse(response)
    }
}
