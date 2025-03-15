package id.hanifalfaqih.reuseit.ui.scan.resultscan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.hanifalfaqih.reuseit.data.repository.impl.GeminiRepositoryImpl
import id.hanifalfaqih.reuseit.databinding.FragmentResultScanDialogBinding
import id.hanifalfaqih.reuseit.helper.GenericViewModelFactory
import id.hanifalfaqih.reuseit.ui.scan.ScanViewModel

class ResultScanFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentResultScanDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: ScanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentResultScanDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val geminiRepository = GeminiRepositoryImpl()
        val geminiRepositoryFactory = GenericViewModelFactory {
            ScanViewModel(geminiRepository)
        }
        viewModel = ViewModelProvider(requireActivity(), geminiRepositoryFactory)[ScanViewModel::class.java]

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.contentLoading.visibility = View.VISIBLE
                binding.textGeneratedContent.visibility = View.GONE
            } else {
                binding.contentLoading.visibility = View.GONE
                binding.textGeneratedContent.visibility = View.VISIBLE
            }
        }

        viewModel.resultScanContent.observe(viewLifecycleOwner) { result ->
            Log.d(ResultScanFragment::class.java.simpleName, result.toString())
            binding.tvTypeWaste.text = result.jenisSampah
            binding.tvCategoryWaste.text = result.kategoriSampah
            binding.tvHowProcessWaste.text = result.caraPengolahan
        }

    }


    companion object {

        // TODO: Customize parameters
//        fun newInstance(content: String? = null, isLoading: Boolean): ResultScanFragment =
//            ResultScanFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_RESULT_CONTENT, content)
//                    putBoolean(ARG_IS_LOADING, isLoading)
//                }
//            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}