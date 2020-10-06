package com.bitapp.presentation.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.bitapp.R
import com.bitapp.domain.entities.BaseSubscribe
import com.bitapp.presentation.BitActivity
import com.bitapp.presentation.ui.viewmodel.DetailViewModel
import com.bitapp.presentation.ui.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_bit_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BitListFragment : Fragment(R.layout.fragment_bit_list) {
    private val tradePairViewModel by sharedViewModel<ListViewModel>()

    private lateinit var tradingPairs: List<String>
    private var selectedCurrency: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeData()
    }

    private fun initViews() {
        activity?.let {
            (it as BitActivity).setSupportActionBar(toolbarChild)
            activity?.title = requireContext().getString(R.string.app_name)
        }
        mSimpleListView.build(::goToDetails)
        tieCurrency.isEnabled = false
        tieCurrency.setOnClickListener {
            val action = BitListFragmentDirections.actionOpenCurrencyList()
            findNavController().navigate(action)
        }
        setFragmentResultListener(CurrencyListDialog.REQUEST_KEY) { _, bundle ->
            val selectedCurrency = bundle.getString(CurrencyListDialog.RESULT_KEY)
            onCurrencySelected(selectedCurrency)
        }
    }

    private fun subscribeData() {
        tradePairViewModel.tradingPairs.observe(viewLifecycleOwner) {
            tradingPairs = it
            onCurrencySelected(selectedCurrency)
        }

        tradePairViewModel.currencyList.observe(viewLifecycleOwner) { currencyList ->
            tieCurrency.isEnabled = true
            onCurrencySelected(
                    selectedCurrency ?: currencyList.find { it == "BTC" } ?: currencyList.first()
            )
        }
    }

    private fun onCurrencySelected(currency: String?) {
        selectedCurrency = currency
        tieCurrency.setText(selectedCurrency)
        if (::tradingPairs.isInitialized) {
            mSimpleListView.submitList(selectedCurrency?.let { _currency ->
                tradingPairs.filter { it.contains(_currency) }
            } ?: tradingPairs)
        }
    }

    private fun goToDetails(tradingPair: String) {
        val action = BitListFragmentDirections.actionListToDetails(tradingPair)
        findNavController().navigate(action)
    }
}