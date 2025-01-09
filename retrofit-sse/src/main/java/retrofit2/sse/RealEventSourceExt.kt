package retrofit2.sse

import okhttp3.Call
import okhttp3.Callback
import okhttp3.EventListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.EMPTY_RESPONSE
import okhttp3.internal.connection.RealCall
import okhttp3.internal.sse.ServerSentEventReader
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import java.io.IOException

class RealEventSourceExt(
    private val request: Request,
    private val listener: EventSourceListener
) : EventSource, ServerSentEventReader.Callback, Callback {
    private lateinit var call: RealCall

    fun connect(client: OkHttpClient) {
        val client = client.newBuilder()
            .eventListener(EventListener.NONE)
            .build()
        call = client.newCall(request) as RealCall
        call.enqueue(this)
    }

    override fun onResponse(call: Call, response: Response) {
        processResponse(response)
    }

    fun processResponse(response: Response) {
        if (!response.isSuccessful) {
            listener.onFailure(this, null, response)
            return
        }
        response.use {
            val body = response.body!!

            if (!body.isEventStream()) {
                listener.onFailure(this,
                    IllegalStateException("Invalid content-type: ${body.contentType()}"), response)
                return
            }

            // This is a long-lived response. Cancel full-call timeouts.
            call.timeoutEarlyExit()

            // Replace the body with an empty one so the callbacks can't see real data.
            val response = response.newBuilder()
                .body(EMPTY_RESPONSE)
                .build()

            val reader = ServerSentEventReader(body.source(), this)
            try {
                listener.onOpen(this, response)
                while (reader.processNextEvent()) {
                }
            } catch (e: Exception) {
                listener.onFailure(this, e, response)
                return
            }
            listener.onClosed(this)
        }
    }

    private fun ResponseBody.isEventStream(): Boolean {
        val contentType = contentType() ?: return false
        return contentType.type == "text" && contentType.subtype == "event-stream"
    }

    override fun onFailure(call: Call, e: IOException) {
        listener.onFailure(this, e, null)
    }

    override fun request(): Request = request

    override fun cancel() {
        call.cancel()
    }

    override fun onEvent(id: String?, type: String?, data: String) {
        listener.onEvent(this, id, type, data)
    }

    override fun onRetryChange(timeMs: Long) {
        // Ignored. We do not auto-retry.
    }
}
