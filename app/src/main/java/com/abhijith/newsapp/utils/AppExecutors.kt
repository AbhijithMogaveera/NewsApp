package com.abhijith.newsapp.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors
/**
 * Constructor for instantiating singleton AppExecutor class
 *
 * @param diskIO     Disk IO operations
 * @param mainThread Main Thread operations
 */ private constructor(val diskIO: Executor, val mainThread: Executor) {

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {
        private val LOCK = Any()
        private var sInstance: AppExecutors? = null

        /**
         * A singleton class instantiation
         *
         * @return [AppExecutors]
         */
        val instance: AppExecutors?
            get() {
                if (sInstance == null) {
                    synchronized(LOCK) {
                        sInstance = AppExecutors(
                            Executors.newSingleThreadExecutor(),
                            MainThreadExecutor()
                        )
                    }
                }
                return sInstance
            }
    }
}