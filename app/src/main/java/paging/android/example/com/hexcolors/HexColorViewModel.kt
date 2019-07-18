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

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData

/**
 * A simple ViewModel that provides a paged list of delicious HexColors.
 */
@Suppress("MemberVisibilityCanBePrivate")
class HexColorViewModel(app: Application) : AndroidViewModel(app) {
    val dao = HexColorDb.get(app).hexColorDao()

    val defaultSortKey get() = sortMap.first().first

    val sortMap = listOf(
            "by HSL Hue" to dao.allHexColorsByHslH(),
            "by HSL Saturation" to dao.allHexColorsByHslS(),
            "by HSL Lightness" to dao.allHexColorsByHslL(),
            "by LAB Lightness" to dao.allHexColorsByLabL(),
            "by LAB A Axis" to dao.allHexColorsByLabA(),
            "by LAB B Axis" to dao.allHexColorsByLabB(),
            "by Random 1" to dao.allHexColorsByOrder1(),
            "by Random 2" to dao.allHexColorsByOrder2(),
            "by Hex Value" to dao.allHexColorsByHex()
    )

    fun getSortKey(sortKey: String? = defaultSortKey): String {
        return sortMap.find { it.first == sortKey }?.first ?: defaultSortKey
    }

    fun getLiveData(sortKey: String): LiveData<PagedList<HexColor>> {
        val factory = sortMap.find { it.first == sortKey }!!.second
        return factory.toLiveData(PAGING_CONFIG)
    }

    fun startOver(context: Context) {
        HexColorDb.startOver(context)
    }

    fun addMore(context: Context) {
        HexColorDb.addMore(context)
    }

    fun remove(hexColor: HexColor) = Executors.ioThread {
        dao.delete(hexColor)
    }

    companion object {

        const val ADD_MORE = "Add More"

        const val START_OVER = "Start Over"

        val PAGING_CONFIG = Config(
                /**
                 * A good page size is a value that fills at least a screen worth of content on a large
                 * device so the User is unlikely to see a null item.
                 * You can play with this constant to observe the paging behavior.
                 * <p>
                 * It's possible to vary this with list device size, but often unnecessary, unless a
                 * user scrolling on a large device is expected to scroll through items more quickly
                 * than a small device, such as when the large device uses a grid layout of items.
                 */
                pageSize = 20,

                /**
                 * If placeholders are enabled, PagedList will report the full size but some items might
                 * be null in onBind method (PagedListAdapter triggers a rebind when data is loaded).
                 * <p>
                 * If placeholders are disabled, onBind will never receive null but as more pages are
                 * loaded, the scrollbars will jitter as new pages are loaded. You should probably
                 * disable scrollbars if you disable placeholders.
                 */
                enablePlaceholders = true,

                /**
                 * Maximum number of items a PagedList should hold in memory at once.
                 * <p>
                 * This number triggers the PagedList to start dropping distant pages as more are loaded.
                 */
                maxSize = 200)
    }
}
