package com.todo.common.helper.observerpatternStore.observeable

interface StockObservable<T> {
    fun addObserver(observer: T)
    fun removeObserver(observer: T)
    fun notifySubscribers()

    fun setCount(count: Int)
    fun getCount(): Int
}
