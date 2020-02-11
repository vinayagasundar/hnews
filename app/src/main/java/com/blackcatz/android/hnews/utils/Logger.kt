package com.blackcatz.android.hnews.utils

import timber.log.Timber

private typealias logString = () -> String

fun logD(tag: String = "Debug", message: logString) {
    Timber.tag(tag)
    Timber.d(message())
}

fun logI(tag: String = "Info", message: logString) {
    Timber.tag(tag)
    Timber.d(message())
}