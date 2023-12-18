package com.example.benchmark

import android.os.Build
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.PowerMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingLegacyMetric
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleMacroStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun startup() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        benchmarkRule.measureRepeated(
            packageName = "com.example.todoapplication",
            metrics = listOf(
                StartupTimingMetric(),
                PowerMetric(PowerMetric.Type.Battery()),
                PowerMetric(PowerMetric.Type.Power()),
                PowerMetric(PowerMetric.Type.Energy())
            ),
            iterations = 5,
            startupMode = StartupMode.COLD
        ) {
            pressHome(50000)
            startActivityAndWait()
        }
    } else {

    }
}