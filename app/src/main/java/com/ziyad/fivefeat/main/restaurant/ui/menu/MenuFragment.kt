package com.ziyad.fivefeat.main.restaurant.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ziyad.fivefeat.databinding.FragmentMenuRestaurantBinding
import com.ziyad.fivefeat.main.restaurant.add.AddMenuActivity
import com.ziyad.fivefeat.main.restaurant.ui.menu.adapter.MenuRestaurantAdapter

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuRestaurantBinding? = null
    private val restaurantMenuViewModel:RestaurantMenuViewModel by viewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restaurantMenuViewModel.restaurant.observe(viewLifecycleOwner) { restaurant ->
            if (restaurant != null) {
                binding.apply {
                    rvMenu.apply {
                        adapter = MenuRestaurantAdapter(
                            restaurant.menuList
                        ) { menu ->
                            restaurantMenuViewModel.deleteMenu(menu)
                        }
                        setHasFixedSize(true)
                    }
                    btnAdd.setOnClickListener {
                        startActivity(Intent(requireContext(), AddMenuActivity::class.java))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        restaurantMenuViewModel.getRestaurant()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}