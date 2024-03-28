package com.example.todoapplication.common.helper.observerpattern.observeable

interface IPostObservable<T> {
    fun addObserver(observer: T)
    fun removeObserver(observer: T)
    fun notifySubscribers()

    fun increaseLikeCount()
    fun getLikeCount(): Int
}
