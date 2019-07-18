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

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Database Access Object for the HexColor database.
 */
@Dao
interface HexColorDao {
    /**
     * Room knows how to return a LivePagedListProvider, from which we can get a LiveData and serve
     * it back to UI via ViewModel.
     */
    @Query("SELECT * FROM HexColor ORDER BY hex COLLATE NOCASE ASC")
    fun allHexColorsByHex(): DataSource.Factory<Int, HexColor>

    @Query("SELECT * FROM HexColor ORDER BY hslH DESC")
    fun allHexColorsByHslH(): DataSource.Factory<Int, HexColor>

    @Query("SELECT * FROM HexColor ORDER BY hslS DESC")
    fun allHexColorsByHslS(): DataSource.Factory<Int, HexColor>

    @Query("SELECT * FROM HexColor ORDER BY hslL DESC")
    fun allHexColorsByHslL(): DataSource.Factory<Int, HexColor>

    @Query("SELECT * FROM HexColor ORDER BY labL DESC")
    fun allHexColorsByLabL(): DataSource.Factory<Int, HexColor>

    @Query("SELECT * FROM HexColor ORDER BY labA DESC")
    fun allHexColorsByLabA(): DataSource.Factory<Int, HexColor>

    @Query("SELECT * FROM HexColor ORDER BY labB DESC")
    fun allHexColorsByLabB(): DataSource.Factory<Int, HexColor>

    @Query("SELECT * FROM HexColor ORDER BY order1 DESC")
    fun allHexColorsByOrder1(): DataSource.Factory<Int, HexColor>

    @Query("SELECT * FROM HexColor ORDER BY order2 DESC")
    fun allHexColorsByOrder2(): DataSource.Factory<Int, HexColor>

    @Insert
    fun insert(hexColors: List<HexColor>)

    @Insert
    fun insert(hexColor: HexColor)

    @Delete
    fun delete(hexColor: HexColor)
}