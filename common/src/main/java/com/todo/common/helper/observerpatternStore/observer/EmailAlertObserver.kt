package com.todo.common.helper.observerpatternStore.observer

import android.util.Log

class EmailAlertObserver constructor(
    email: String
) : NotificationAlertObserver {

    private var email: String? = email

    override fun update() {
        sendMail(email)
    }

    private fun sendMail(email: String?) {
        Log.d("sendMail", "HI! Email Sent $email")
    }
}
