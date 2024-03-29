package com.todo.common.helper.observerpattern

import com.todo.common.helper.observerpattern.observeable.PostObservable
import com.todo.common.helper.observerpattern.observer.EmailReactionAlertObserver
import com.todo.common.helper.observerpattern.observer.NotificationReactionAlertObserver

class Profile {

    private val postObservable = PostObservable<NotificationReactionAlertObserver>()

    private val observer1: NotificationReactionAlertObserver =
        EmailReactionAlertObserver("a@gmail.com")
    private val observer2: NotificationReactionAlertObserver =
        EmailReactionAlertObserver("b@gmail.com")

    fun addObserver() {
        postObservable.addObserver(observer1)
        postObservable.addObserver(observer2)
    }

    fun likeContent() {
        postObservable.increaseLikeCount()
    }
}
