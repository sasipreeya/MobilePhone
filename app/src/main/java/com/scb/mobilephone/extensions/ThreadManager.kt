package com.scb.mobilephone.extensions

import android.os.Handler
import android.os.HandlerThread

class ThreadManager(threadName: String) : HandlerThread(threadName) {

    // Room does not allow operations on the main thread as it can makes your UI laggy.

    private lateinit var mWorkerHandler: Handler

    override fun onLooperPrepared() {
        super.onLooperPrepared()

        mWorkerHandler = Handler(looper)
    }

    fun postTask(task: Runnable) {
        mWorkerHandler.post(task)
    }

}