package hr.algebra.formula1data.model

import com.google.gson.annotations.SerializedName

data class DriverStanding(
    @SerializedName("position")
    val position: String,

    @SerializedName("positionText")
    val positionText: String,

    @SerializedName("points")
    val points: String,

    @SerializedName("wins")
    val wins: String,

    @SerializedName("Driver")
    val driver: Driver?,

    @SerializedName("Constructors")
    val constructors: List<Constructor>? = null
)