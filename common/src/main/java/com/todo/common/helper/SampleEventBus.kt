package com.todo.common.helper

data class MessageEvent(val message: String)
data class UpdateEvent(val data: Int)

class SampleEventBus<T> {
    private val subscribers = mutableMapOf<Class<T>, MutableList<(T) -> Unit>>()

    fun register(eventType: Class<T>, subscriber: (T) -> Unit) {
        subscribers.getOrPut(eventType, { mutableListOf() }).add(subscriber)
    }

    fun unregister(eventType: Class<T>, subscriber: (T) -> Unit) {
        subscribers[eventType]?.remove(subscriber)
    }

    fun post(event: T & Any) {
        val eventType = event::class.java
        subscribers[eventType]?.forEach { subscriber ->
            subscriber(event)
        }
    }
}
