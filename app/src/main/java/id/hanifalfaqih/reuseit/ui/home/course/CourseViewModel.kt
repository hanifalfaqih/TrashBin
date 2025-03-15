package id.hanifalfaqih.reuseit.ui.home.course

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.data.repository.CourseRepository
import kotlinx.coroutines.launch

class CourseViewModel(private val repository: CourseRepository): ViewModel() {

    private var _listAllCourseContent = MutableLiveData<List<Content>?>()
    val listAllCourseContent get() = _listAllCourseContent

    private var _listTop5CourseContent = MutableLiveData<List<Content>?>()
    val listTop5CourseContent get() = _listTop5CourseContent

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage get() = _errorMessage

    fun getAllCourseContent() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = false
                val response = repository.getAllCourseContent()
                if (response.isSuccessful) {
                    val responseData = response.body()
                    responseData?.let {
                        _listAllCourseContent.value = it.data
                    }
                } else {
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }

    fun getTop5CourseContent() {
        _isLoading.value = true
        Log.d(CourseViewModel::class.java.simpleName, "GET TOP 5 COURSE CONTENT")
        viewModelScope.launch {
            try {
                _isLoading.value = false
                val response = repository.getTop5CourseContent()
                if (response.isSuccessful) {
                    val responseData = response.body()
                    responseData?.let {
                        _listTop5CourseContent.value = it.data
                    }
                } else {
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Exception: ${e.message}"
            }
        }
    }
}