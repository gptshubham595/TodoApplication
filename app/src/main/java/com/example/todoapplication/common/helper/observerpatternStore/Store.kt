package com.example.todoapplication.common.helper.observerpatternStore

import com.example.todoapplication.common.helper.observerpatternStore.observeable.IphoneObservable
import com.example.todoapplication.common.helper.observerpatternStore.observer.EmailAlertObserver
import com.example.todoapplication.common.helper.observerpatternStore.observer.NotificationAlertObserver

class Store {

    private val iPhoneObservable = IphoneObservable<NotificationAlertObserver>()

    private val observer1: NotificationAlertObserver = EmailAlertObserver("a@gmail.com")
    private val observer2: NotificationAlertObserver = EmailAlertObserver("b@gmail.com")

    fun addObserver() {
        iPhoneObservable.addObserver(observer1)
        iPhoneObservable.addObserver(observer2)
    }

    fun stockEmpty() {
        iPhoneObservable.setCount(0)
    }
}
