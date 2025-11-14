package hr.algebra.formula1data.model.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "race_schedule")
data class RaceScheduleEntity(
    @PrimaryKey val id: String,
    val round: String,
    val season: String,
    val raceName: String,
    val date: String,
    val time: String?,
    val circuitId: String,
    val circuitName: String,
    val url: String,
    val locality: String,
    val country: String
)
