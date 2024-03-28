package com.example.todoapplication.common.helper.observerpattern.observer

import android.util.Log

class EmailReactionAlertObserver constructor(
    email: String
) : NotificationReactionAlertObserver {

    private var email: String? = email

    override fun update() {
        sendMail(email)
    }

    private fun sendMail(email: String?) {
        Log.d("sendMail", "HI! Email Sent $email")
    }
}
