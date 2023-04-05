package com.ziyad.fivefeat.main.user.ui.order.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ziyad.fivefeat.databinding.FragmentTabbedBinding
import com.ziyad.fivefeat.main.user.order.OrderActivity
import com.ziyad.fivefeat.main.user.ui.order.adapter.OrderHistoryAdapter
import com.ziyad.fivefeat.model.Order
import java.util.ArrayList

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentTabbedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java].apply {
            setState(arguments?.getString(ORDER_STATE) ?: "NONE")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTabbedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("TEZZ","TTT")
        pageViewModel.apply {
//            loadRealtime()
            orders.observe(viewLifecycleOwner) { orderList ->
                binding.apply {
                    rvOrderHistory.apply {
                        adapter = OrderHistoryAdapter(
                            orderList.filter {
                                (arguments?.getString(ORDER_STATE) ?: "NONE").contains(it.state)
                            } as ArrayList<Order>
                        ) { order ->
                            startActivity(
                                Intent(
                                    requireContext(),
                                    OrderActivity::class.java
                                ).putExtra("ORDER", order)
                            )
                        }
                        setHasFixedSize(true)
                    }
                }
            }
        }

    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"
        const val ORDER_STATE = "order_state"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(orderState: String): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putString(ORDER_STATE, orderState)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}