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

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple ViewHolder that can bind a HexColor item. It also accepts null items since the data may
 * not have been fetched before it is bound.
 */
class HexColorViewHolder(parent :ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.hex_color_item, parent, false)) {

    private val nameView = itemView.findViewById<TextView>(R.id.name)
    var hexColor : HexColor? = null

    /**
     * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
     * ViewHolder when Item is loaded.
     */
    @SuppressLint("SetTextI18n")
    fun bindTo(hexColor : HexColor?) {
        //android.util.Log.d("hex","COLOR: ${hexColor?.hex}")
        this.hexColor = hexColor
        nameView.text = "0x${hexColor?.hex ?: "??????"}"
        val colorInt = Color.parseColor("#${hexColor?.hex ?: "dddddd"}")
        itemView.setBackgroundColor(colorInt)
    }

}