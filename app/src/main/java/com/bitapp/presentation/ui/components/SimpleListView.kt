package com.bitapp.presentation.ui.components

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitapp.R
import com.bitapp.presentation.ui.adapters.ItemClickListener
import com.bitapp.presentation.ui.adapters.SimpleListAdapter
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class SimpleListView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle), KoinComponent, ItemClickListener<String> {

    private val mAdapter by inject<SimpleListAdapter> {
        parametersOf(this)
    }
    private lateinit var callback: (String) -> Unit

    init {
        initViews()
    }

    private fun initViews() {
        layoutManager = GridLayoutManager(context, SPAN_COUNT)
        setHasFixedSize(true)

        //mAdapter.stateRestorationPolicy = Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        adapter = mAdapter
    }

    fun build(callback: (String) -> Unit, @LayoutRes layout: Int = R.layout.list_item_text, block: (SimpleListView.() -> Unit)? = null) {
        when {
            layout != R.layout.list_item_text -> mAdapter.itemLayout = layout
        }
        this.callback = callback
        block?.let {
            this.apply(it)
        }
    }

    fun submitList(list: List<String>) {
        mAdapter.submitList(list)
    }

    override fun onItemSelected(item: String) {
        callback(item)
    }

    companion object {
        const val SPAN_COUNT = 1
    }
}