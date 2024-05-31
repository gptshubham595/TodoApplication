package com.todo.common.helper.observerpattern.observer

import android.util.Log

class SMSProfileAlertObserver constructor(
    mobile: String
) : NotifyProfileObserver {

    private var mobile: String? = mobile

    override fun update() {
        sendSMS(mobile)
    }

    private fun sendSMS(mobile: String?) {
        Log.d("sendMail", "HI! Email Sent $mobile")
    }
}
