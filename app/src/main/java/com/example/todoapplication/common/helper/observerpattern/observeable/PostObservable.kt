package com.example.todoapplication.common.helper.observerpattern.observeable

import com.example.todoapplication.common.helper.observerpatternStore.observer.NotificationAlertObserver

class PostObservable<T> : IPostObservable<T> {

    private var likeCount = 0;
    private val observersList = mutableListOf<T>()

    override fun addObserver(observer: T) {
        observersList.add(observer)
    }

    override fun removeObserver(observer: T) {
        observersList.remove(observer)
    }

    override fun notifySubscribers() {
        observersList.forEach {
            if (it is NotificationAlertObserver) {
                it.update()
            }
        }
    }

    override fun getLikeCount(): Int {
        return likeCount
    }

    override fun increaseLikeCount() {
        likeCount++
        notifySubscribers()
    }
}