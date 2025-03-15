package id.hanifalfaqih.reuseit.data.repository.impl

import android.graphics.Bitmap
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import id.hanifalfaqih.reuseit.data.repository.GeminiRepository

class GeminiRepositoryImpl: GeminiRepository {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyARXiyT5mCIueR3ZyK123_ncfq5dllXUO8",
        generationConfig = generationConfig {
            responseMimeType = "application/json"
        }
    )


    override suspend fun generateResult(image: Bitmap): String {
        val inputContent = content {
            image(image)
            text("""
                Identifikasi sebuah gambar, kemudian tampilkan data jenis sampah, informasi sampah, dan cara pengolahan sampahnya ke dalam JSON Object:
                {'jenis_sampah': string, 'kategori_sampah': string, 'cara_pengolahan': string}
            """.trimIndent())
//            text("ada input gambar seperti berikut, nantinya tampilkan 3 data teks, jenis sampah, informasi sampah, dan cara pengolahan sampahnya. tidak perlu terlalu detail yang penting mencakup 3 aspek tadi")
        }

        val response = generativeModel.generateContent(inputContent)
        return response.text.toString()
    }
}