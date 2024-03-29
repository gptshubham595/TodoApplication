package com.todo.common.helper

data class MessageEvent(val message: String)
data class UpdateEvent(val data: Int)

class SampleEventBus {
    private val subscribers = mutableMapOf<Class<*>, MutableList<(Any) -> Unit>>()

    fun <T : Any> register(eventType: Class<T>, subscriber: (T) -> Unit) {
        subscribers.getOrPut(eventType, { mutableListOf() }).add(subscriber as (Any) -> Unit)
    }

    fun <T : Any> unregister(eventType: Class<T>, subscriber: (T) -> Unit) {
        subscribers[eventType]?.remove(subscriber)
    }

    fun <T : Any> post(event: T) {
        val eventType = event.javaClass
        subscribers[eventType]?.forEach { subscriber ->
            subscriber(event)
        }
    }
}
