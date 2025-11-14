package hr.algebra.formula1data.model.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_standings")
data class DriverStandingEntity(
    @PrimaryKey val id: String,
    val driverId: String,
    val position: String,
    val points: String,
    val wins: String,
    val givenName: String,
    val familyName: String,
    val constructorName: String,
    val season: String
)
