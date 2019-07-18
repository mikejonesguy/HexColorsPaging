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

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Shows a list of HexColors, with swipe-to-delete, and an input field at the top to add.
 * <p>
 * HexColors are stored in a database, so swipes and additions edit the database directly, and the UI
 * is updated automatically using paging components.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: HexColorAdapter
    private var lastLiveList: LiveData<PagedList<HexColor>>? = null

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(HexColorViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create adapter for the RecyclerView
        adapter = HexColorAdapter()
        hexColorList.adapter = adapter

        // Subscribe the adapter to the ViewModel, so the items in the adapter are refreshed
        // when the list changes
        setDataSource(viewModel.getSortKey())

        //initSwipeToDelete()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        viewModel.sortMap.forEach {
            menu?.add(it.first)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        setDataSource(item?.title.toString())
        return super.onOptionsItemSelected(item)
    }

    private fun setDataSource(sortKey: String = viewModel.getSortKey()) {
        supportActionBar?.title = "Sort Colors $sortKey"
        val source = viewModel.getLiveData(sortKey)
        lastLiveList?.removeObservers(this)
        lastLiveList = source
        lastLiveList?.observe(this, Observer { adapter.submitList(it) })
    }

    private fun initSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            // enable the items to swipe to the left or right
            override fun getMovementFlags(recyclerView: RecyclerView,
                                          viewHolder: RecyclerView.ViewHolder): Int =
                    makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean = false

            // When an item is swiped, remove the item via the view model. The list item will be
            // automatically removed in response, because the adapter is observing the live list.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as HexColorViewHolder).hexColor?.let {
                    viewModel.remove(it)
                }
            }
        }).attachToRecyclerView(hexColorList)
    }
}
