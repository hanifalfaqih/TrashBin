package id.hanifalfaqih.reuseit.data.model

import com.google.gson.annotations.SerializedName

data class Sampah(
    @SerializedName("jenis_sampah")
    val jenisSampah: String,
    @SerializedName("kategori_sampah")
    val kategoriSampah: String,
    @SerializedName("cara_pengolahan")
    val caraPengolahan: String
)
