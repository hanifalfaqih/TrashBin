package id.hanifalfaqih.reuseit.data.repository

import android.graphics.Bitmap

interface GeminiRepository {

    suspend fun generateResult(image: Bitmap): String

}