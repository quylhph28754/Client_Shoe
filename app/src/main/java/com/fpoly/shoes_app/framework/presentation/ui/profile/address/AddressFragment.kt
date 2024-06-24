package com.fpoly.shoes_app.framework.presentation.ui.profile.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentAddressBinding
import com.fpoly.shoes_app.framework.adapter.AddressAdapter
import com.fpoly.shoes_app.framework.domain.model.profile.AddressDetail
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class AddressFragment : Fragment() {
    private var _binding: FragmentAddressBinding? = null
    private var listAddressDetails =
        listOf(
            AddressDetail("Home", "Ba Đình,Hà Nội", 21.039430738632642, 105.83454867959188),
            AddressDetail(
                "Company",
                "Số 9 P. Trần Bình, Mai Dịch, Cầu Giấy, Hà Nội",
                21.033714620270327,
                105.77855956451597
            ),
            AddressDetail(
                "Keangnam Landmark Tower 72",
                "Khu E6, Phạm Hùng, Mễ Trì, Cầu Giấy, Hà Nội",
                21.017714275852267,
                105.78457284113405
            ),
            AddressDetail(
                "National Convention Center",
                "57 Phạm Hùng, Mễ Trì, Nam Từ Liêm, Hà Nội",
                21.007833539027175,
                105.78816173220403
            ),
        )
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        setupRecyclerView()
        return binding!!.root
    }

    private fun setupRecyclerView() {
        val addressAdapter = AddressAdapter(listAddressDetails) { address ->
            val bundle = Bundle().apply {
                    putParcelable("address", address)

            }
            findNavController().navigate(R.id.addressDetailsFragment, bundle)
        }
        binding!!.recycViewAddress.layoutManager = LinearLayoutManager(requireContext())
        binding!!.recycViewAddress.adapter = addressAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }
}