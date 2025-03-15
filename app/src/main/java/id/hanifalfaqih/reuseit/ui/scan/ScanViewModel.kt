package id.hanifalfaqih.reuseit.ui.scan

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import id.hanifalfaqih.reuseit.data.model.Sampah
import id.hanifalfaqih.reuseit.data.repository.GeminiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanViewModel(private val repository: GeminiRepository) : ViewModel() {

    private var _resultScanContent = MutableLiveData<Sampah>()
    val resultScanContent: LiveData<Sampah> = _resultScanContent

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError



    fun getGenerateResult(image: Bitmap) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val gson = Gson()
                val response = repository.generateResult(image)
                withContext(Dispatchers.Main) {
                    _isLoading.value = false

                    val responseToGson = gson.fromJson(response, Sampah::class.java)

                    _resultScanContent.value = responseToGson
                    Log.d(ScanViewModel::class.simpleName, response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _messageError.value = "Gagal mendapatkan hasil"
                }
            }
        }
    }
}