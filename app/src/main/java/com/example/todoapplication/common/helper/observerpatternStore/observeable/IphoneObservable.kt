package com.example.todoapplication.common.helper.observerpatternStore.observeable

import com.example.todoapplication.common.helper.observerpatternStore.observer.NotificationAlertObserver

class IphoneObservable<T>: StockObservable<T> {

    private var stockCount = 0;
    private val observersList = mutableListOf<T>()

    override fun addObserver(observer: T) {
        observersList.add(observer)
    }

    override fun removeObserver(observer: T) {
        observersList.remove(observer)
    }

    override fun notifySubscribers() {
        observersList.forEach {
            if(it is NotificationAlertObserver){
                it.update()
            }
        }
    }

    override fun setCount(countAdded: Int) {
        if(stockCount == 0)
            notifySubscribers()
        stockCount += countAdded
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }
}