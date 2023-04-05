package com.ziyad.fivefeat.main.restaurant.ui.order.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ziyad.fivefeat.databinding.FragmentTabbedBinding
import com.ziyad.fivefeat.main.restaurant.order.OrderRestaurantActivity
import com.ziyad.fivefeat.main.restaurant.ui.order.adapter.OrderHistoryRestaurantAdapter
import com.ziyad.fivefeat.model.Order

/**
 * A placeholder fragment containing a simple view.
 */
class RestaurantPlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: RestaurantPageViewModel
    private var _binding: FragmentTabbedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[RestaurantPageViewModel::class.java].apply {
            setState(arguments?.getString(ORDER_STATE) ?: "NONE")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTabbedBinding.inflate(inflater, container, false)
        val root = binding.root
        pageViewModel.apply {
            orders.observe(viewLifecycleOwner) { orderList ->
                binding.apply {
                    rvOrderHistory.apply {
                        adapter = OrderHistoryRestaurantAdapter(
                            orderList.filter {
                                (arguments?.getString(ORDER_STATE) ?: "QUEUE").contains(it.state)
                            } as ArrayList<Order>
                        ) { order ->
                            startActivity(
                                Intent(
                                    requireContext(),
                                    OrderRestaurantActivity::class.java
                                ).putExtra("ORDER", order)
                            )
                        }
                        setHasFixedSize(true)
                    }
                }
            }
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        pageViewModel.apply {
            clearList()
            loadData()
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
        fun newInstance(orderState: String): RestaurantPlaceholderFragment {
            return RestaurantPlaceholderFragment().apply {
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