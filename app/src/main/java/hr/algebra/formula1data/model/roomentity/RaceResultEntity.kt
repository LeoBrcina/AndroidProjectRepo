package hr.algebra.formula1data.model.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "race_results")
data class RaceResultEntity(
    @PrimaryKey val round: String,
    val season: String,
    val raceName: String,
    val date: String,
    val resultsJson: String
)