package com.todo.common.helper.observerpattern

import com.todo.common.helper.observerpattern.observeable.PostObservable
import com.todo.common.helper.observerpattern.observer.EmailProfileAlertObserver
import com.todo.common.helper.observerpattern.observer.NotifyProfileObserver
import com.todo.common.helper.observerpattern.observer.SMSProfileAlertObserver

class Profile {

    private val postObservable = PostObservable<NotifyProfileObserver>()

    private val observer1: NotifyProfileObserver =
        EmailProfileAlertObserver("a@gmail.com")
    private val observer2: NotifyProfileObserver =
        SMSProfileAlertObserver("8923749878")

    fun addObserver() {
        postObservable.addObserver(observer1)
        postObservable.addObserver(observer2)
    }

    fun likeContent() {
        postObservable.increaseLikeCount()
    }
}
