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

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

/**
 * Data class that represents our items.
 */
@Entity
data class HexColor(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val hex: String,
        val hslH: Float = toHsl(hex)[0],
        val hslS: Float = toHsl(hex)[1],
        val hslL: Float = toHsl(hex)[2],
        val labL: Double = toLab(hex)[0],
        val labA: Double = toLab(hex)[1],
        val labB: Double = toLab(hex)[2],
        val order1: Int = Random.nextInt(4096),
        val order2: Int = Random.nextInt(4096)
) {
    constructor(hex: String) : this(0, hex)
    constructor(hex: Int) : this("%06x".format(hex))

    companion object {
        fun toHsl(hex: String): FloatArray {
            val color = Color.parseColor("#$hex")
            val hsl = FloatArray(3)
            ColorUtils.colorToHSL(color, hsl)
            return hsl
        }

        fun toLab(hex: String): DoubleArray {
            val color = Color.parseColor("#$hex")
            val lab = DoubleArray(3)
            ColorUtils.colorToLAB(color, lab)
            return lab
        }
    }
}