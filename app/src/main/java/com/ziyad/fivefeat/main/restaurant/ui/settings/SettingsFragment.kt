package com.ziyad.fivefeat.main.restaurant.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ziyad.fivefeat.auth.LandingActivity
import com.ziyad.fivefeat.databinding.FragmentSettingsBinding
import com.ziyad.fivefeat.utils.loadImage


class SettingsFragment : Fragment() {

    private lateinit var restaurantsSettingsViewModel: RestaurantsSettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        restaurantsSettingsViewModel =
            ViewModelProvider(this)[RestaurantsSettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        restaurantsSettingsViewModel.name.observe(viewLifecycleOwner) {
            binding.tvProfileName.text = it.displayName
            if (it.photoUrl != null)
                binding.ivProfile.loadImage(it.photoUrl.toString())
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap(it.uid, BarcodeFormat.QR_CODE, 400, 400)
                binding.ivBarcode.setImageBitmap(bitmap)
            } catch (e: Exception) {
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener {
            restaurantsSettingsViewModel.logout()
            startActivity(Intent(requireContext(), LandingActivity::class.java))
            activity?.finishAffinity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}