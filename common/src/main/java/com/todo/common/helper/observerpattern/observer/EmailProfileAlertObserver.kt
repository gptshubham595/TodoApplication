package com.todo.common.helper.observerpattern.observer

import android.util.Log

class EmailProfileAlertObserver constructor(
    email: String
) : NotifyProfileObserver {

    private var email: String? = email

    override fun update() {
        sendMail(email)
    }

    private fun sendMail(email: String?) {
        Log.d("sendMail", "HI! Email Sent $email")
    }
}
