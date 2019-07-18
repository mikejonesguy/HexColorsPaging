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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlin.random.Random

/**
 * Singleton database object. Note that for a real app, you should probably use a Dependency
 * Injection framework or Service Locator to create the singleton database.
 */
@Database(entities = [HexColor::class], version = 1)
abstract class HexColorDb : RoomDatabase() {
    abstract fun hexColorDao(): HexColorDao

    companion object {
        private var instance: HexColorDb? = null
        @Synchronized
        fun get(context: Context): HexColorDb {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        HexColorDb::class.java, "HexColorDatabase")
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                addMore(context.applicationContext)
                            }
                        }).build()
            }
            return instance!!
        }

        /**
         * fill database with list of hexColors
         */
        private fun fillInDb(context: Context) {
            val colors = mutableListOf<HexColor>()
            val stepSize = 0x10101
            for (hex in 0x000000 until 0xFFFFFF step stepSize) {
                colors.add(HexColor(hex + Random.nextInt(stepSize - 1)))
            }

            get(context).hexColorDao().insert(colors)
        }

        fun addMore(context: Context) {
            Executors.ioThread {
                fillInDb(context)
            }
        }

        fun startOver(context: Context) {
            Executors.ioThread {
                get(context).clearAllTables()
                fillInDb(context)
            }
        }
    }
}
