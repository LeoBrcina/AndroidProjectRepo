package hr.algebra.formula1data.model

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("locality")
    val locality: String,
    @SerializedName("country")
    val country: String
)