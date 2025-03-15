package id.hanifalfaqih.reuseit.ui.home.course

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.hanifalfaqih.reuseit.data.repository.impl.CourseRepositoryImpl
import id.hanifalfaqih.reuseit.databinding.FragmentCourseBinding
import id.hanifalfaqih.reuseit.helper.GenericViewModelFactory
import id.hanifalfaqih.reuseit.network.ApiConfig
import id.hanifalfaqih.reuseit.ui.detailcontent.DetailContentActivity
import id.hanifalfaqih.reuseit.ui.detailcontent.DetailContentActivity.Companion.CONTENT_ID

class CourseFragment : Fragment() {

    private var _binding: FragmentCourseBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var courseViewModel: CourseViewModel

    private lateinit var courseAdapter: CourseAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCourseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = ApiConfig.getApiService()
        val courseRepository = CourseRepositoryImpl(apiService)
        val courseRepositoryFactory = GenericViewModelFactory {
            CourseViewModel(courseRepository)
        }

        courseViewModel = ViewModelProvider(this, courseRepositoryFactory)[CourseViewModel::class.java]

        courseViewModel.getTop5CourseContent()

        courseAdapter = CourseAdapter { courseId ->
            intentToDetailContent(courseId)
        }

        binding.tvShowAllCourses.setOnClickListener {
            startActivity(Intent(requireContext(), ListCourseActivity::class.java))
        }

        observeData()
        initRecyclerView()

    }

    private fun observeData() {
        courseViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvTopListCourses.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.rvTopListCourses.visibility = View.VISIBLE
            }
        }

        courseViewModel.listTop5CourseContent.observe(viewLifecycleOwner) { listTopCourse ->
            courseAdapter.submitList(listTopCourse)
        }
    }

    private fun initRecyclerView() {
        binding.rvTopListCourses.apply {
            layoutManager = LinearLayoutManager(context)
            binding.rvTopListCourses.adapter = courseAdapter
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