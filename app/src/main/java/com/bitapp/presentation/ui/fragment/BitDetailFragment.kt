package com.bitapp.presentation.ui.fragment

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.bitapp.R
import com.bitapp.presentation.BitActivity
import com.bitapp.presentation.ui.adapters.OrderBookAdapter
import com.bitapp.presentation.ui.adapters.TradesAdapter
import com.bitapp.presentation.ui.utils.*
import com.bitapp.presentation.ui.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_bit_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat

private const val TAG = "BitDetailFragment"
class BitDetailFragment : Fragment(R.layout.fragment_bit_detail) {
    private val args: BitDetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModel()

    private lateinit var orderBookBidAdapter: OrderBookAdapter
    private lateinit var orderBookAskAdapter: OrderBookAdapter
    private lateinit var tradeAdapter: TradesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeData()
        viewModel.subscribeToTradingPair(args.tradingPair)
    }

    override fun onStop() {
        viewModel.unSubscribeFromChannels()
        super.onStop()
    }

    private fun initViews() {
        activity?.let { activity ->
            (activity as BitActivity).setSupportActionBar(toolbarChild)
            activity.title = args.tradingPair
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbarChild.setNavigationOnClickListener { activity.onBackPressed() }
        }

        orderBookBidAdapter = OrderBookAdapter()
        order_book_bid_list.adapter = orderBookBidAdapter

        orderBookAskAdapter = OrderBookAdapter()
        order_book_ask_list.adapter = orderBookAskAdapter

        tradeAdapter = TradesAdapter()
        trades_list.adapter = tradeAdapter
    }

    private fun subscribeData() {
        viewModel.ticker.observe(viewLifecycleOwner) { ticker ->
            Log.d(TAG, "subscribeData: $ticker")
            latestPrice.text = getTicketFormat(R.string.last_price, ticker.lastPrice)

            volume.text = getTicketFormat(R.string.volume, ticker.volume)
            low.text = getTicketFormat(R.string.low, ticker.low)
            high.text = getTicketFormat(R.string.high, ticker.high)

            val dailyChange = ticker.dailyChange.toDouble()
            change.text = getTicketFormat(R.string.change, ticker.dailyChange, if (dailyChange > 0.0) R.color.colorPrimary else R.color.colorAccent)
        }

        viewModel.orderBooks.observe(viewLifecycleOwner) { orderBookList ->
            orderBookBidAdapter.submitList(getOrderBookBidList(orderBookList))
            orderBookAskAdapter.submitList(getOrderBookAskList(orderBookList))
        }
        viewModel.trades.observe(viewLifecycleOwner) {
            tradeAdapter.submitList(getTradeList(it))
        }
    }

    private fun getTicketFormat(headingRes: Int, value: String, color: Int = R.color.colorPrimaryText): SpannableStringBuilder {
        val decimalFormat = DecimalFormat(getString(R.string.number_format))
        return Formatter.buildMultipleSpannedString(
                requireContext(),
                resources.getString(R.string.ticker_format),
                SpanWord(resources.getString(headingRes)),
                SpanWord(decimalFormat.format(value.toDouble()), color = color)
        )
    }
}
