package com.example.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @RequiresApi(Build.VERSION_CODES.P)
    @get:Rule
    val baselineRule = BaselineProfileRule()

    @RequiresApi(Build.VERSION_CODES.P)
    @Test
    fun generateBaselineProfile() = baselineRule.collect(
        packageName = "com.example.todoapplication",
        includeInStartupProfile = true,
        profileBlock = {
            // Run your app here, and perform the actions you want
            // to capture in the baseline profile
            startActivityAndWait() // library itself captures the startup time
        },
    )
}
