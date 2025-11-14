package hr.algebra.formula1data.model

import com.google.gson.annotations.SerializedName

data class Race(
    val season: String,
    val round: String,
    val raceName: String,
    val date: String,
    val time: String?,
    @SerializedName("Circuit")
    val circuit: Circuit
)