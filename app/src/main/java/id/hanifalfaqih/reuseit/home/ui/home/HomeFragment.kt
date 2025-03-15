package id.hanifalfaqih.reuseit.home.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.hanifalfaqih.reuseit.data.repository.impl.CourseRepositoryImpl
import id.hanifalfaqih.reuseit.data.repository.impl.DIYRepositoryImpl
import id.hanifalfaqih.reuseit.databinding.FragmentHomeBinding
import id.hanifalfaqih.reuseit.helper.GenericViewModelFactory
import id.hanifalfaqih.reuseit.network.ApiConfig
import id.hanifalfaqih.reuseit.ui.detailcontent.DetailContentActivity
import id.hanifalfaqih.reuseit.ui.detailcontent.DetailContentActivity.Companion.CONTENT_ID
import id.hanifalfaqih.reuseit.ui.home.course.CourseViewModel
import id.hanifalfaqih.reuseit.ui.home.diy.DIYAdapter
import id.hanifalfaqih.reuseit.ui.home.diy.DIYViewModel
import id.hanifalfaqih.reuseit.ui.home.diy.ListDIYActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var diyViewModel: DIYViewModel

    private lateinit var diyAdapter: DIYAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = ApiConfig.getApiService()
        val diyRepository = DIYRepositoryImpl(apiService)
        val diyRepositoryFactory = GenericViewModelFactory {
            DIYViewModel(diyRepository)
        }

        diyViewModel = ViewModelProvider(this, diyRepositoryFactory)[DIYViewModel::class.java]

        diyViewModel.getTop5DIYContent()

        diyAdapter = DIYAdapter { diyId ->
            intentToDetailContent(diyId)
        }

        binding.tvShowAllDiy.setOnClickListener {
            startActivity(Intent(requireContext(), ListDIYActivity::class.java))
        }

        observeData()
        initRecyclerView()
    }

    private fun observeData() {
        diyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvTopListDiy.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.rvTopListDiy.visibility = View.VISIBLE
            }
        }

        diyViewModel.listTop5DIYContent.observe(viewLifecycleOwner) { listTopDiy ->
            diyAdapter.submitList(listTopDiy)
        }
    }

    private fun initRecyclerView() {
        binding.rvTopListDiy.apply {
            layoutManager = LinearLayoutManager(context)
            binding.rvTopListDiy.adapter = diyAdapter
        }
    }

    private fun intentToDetailContent(id: Int) {
        val intent = Intent(requireContext(), DetailContentActivity::class.java)
        intent.putExtra(CONTENT_ID, id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}