package com.swizel.android.whereintheworld.utils

import timber.log.Timber

object ConsoleLogger {

    fun init(
        prefix: String = "",
    ) {
        // Add spacer to prefix if a prefix has been specified.
        val paddedPrefix = if (prefix.isNotEmpty()) "$prefix " else prefix

        Timber.plant(
            object : Timber.DebugTree() {
                /**
                 * Override log to add a global tag for all Timber statements.
                 */
                override fun log(
                    priority: Int,
                    tag: String?,
                    message: String,
                    t: Throwable?,
                ) {
                    super.log(priority, paddedPrefix, message, t)
                }
            },
        )
    }

    @JvmStatic
    fun i(
        message: String,
    ) {
        Timber.i(message)
    }

    @JvmStatic
    fun d(
        message: String,
    ) {
        Timber.d(message)
    }

    @JvmStatic
    fun v(
        message: String,
    ) {
        Timber.v(message)
    }

    @JvmStatic
    fun w(
        message: String,
    ) {
        Timber.w(message)
    }

    @JvmStatic
    fun e(
        message: String,
    ) {
        Timber.e(message)
    }

    @JvmStatic
    fun e(
        cause: Throwable,
    ) {
        Timber.e(cause)
    }
}
