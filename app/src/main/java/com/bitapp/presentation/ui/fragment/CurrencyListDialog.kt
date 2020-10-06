package com.bitapp.presentation.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.bitapp.R
import com.bitapp.presentation.ui.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.dialog_currency_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CurrencyListDialog : BottomSheetDialogFragment() {
    private val tradePairViewModel by sharedViewModel<ListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialog = super.onCreateDialog(savedInstanceState)
        mDialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal = d.findViewById<View>(
                    R.id.design_bottom_sheet
            )
            val bottomSheetBehavior =
                    BottomSheetBehavior.from<View?>(
                            bottomSheetInternal!!
                    )
            bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_EXPANDED
        }
        return mDialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_currency_list, container, false)
    }

    companion object {
        const val REQUEST_KEY = "list-request"
        const val RESULT_KEY = "list-result"
    }

    private fun initView() {
        simpleListView.build(::onItemSelected) {
            layoutManager = GridLayoutManager(context, 3)
        }

    }

    private fun subscribeData() {
        tradePairViewModel.currencyList.observe(viewLifecycleOwner) {
            simpleListView.submitList(it)
        }
    }

    private fun onItemSelected(currency: String) {
        tradePairViewModel.currencyList.value?.let {
            setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY to currency))
            dismiss()
        }
    }
}