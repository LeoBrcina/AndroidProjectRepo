package hr.algebra.formula1data.model

import com.google.gson.annotations.SerializedName

data class RaceResult(
    val number: String,
    val position: String,
    val positionText: String,
    val points: String,
    @SerializedName("Driver")
    val driver: Driver?,
    @SerializedName("Constructor")
    val constructor: Constructor?,
    val grid: String,
    val laps: String,
    val status: String,
    @SerializedName("Time")
    val raceTime: Time?,
    val fastestLap: FastestLap?
)