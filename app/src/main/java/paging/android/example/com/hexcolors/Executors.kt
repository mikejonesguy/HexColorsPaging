/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package paging.android.example.com.hexcolors

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Suppress("MemberVisibilityCanBePrivate")
object Executors {

    private val UI = MainThreadExecutor()
    private val IO = Executors.newSingleThreadExecutor()

    fun isMainThread() = Looper.myLooper() == Looper.getMainLooper()

    fun uiThread(action: () -> Unit) {
        if (isMainThread()) action.invoke()
        else UI.execute( Runnable(action) )
    }

    fun uiThread(command: Runnable) {
        if (isMainThread()) command.run()
        else UI.execute(command)
    }

    fun ioThread(action: () -> Unit) {
        IO.execute(action)
    }

    class MainThreadExecutor: Executor {
        private val handler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }

}