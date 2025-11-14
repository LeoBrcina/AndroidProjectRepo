package hr.algebra.formula1data.model.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_details")
data class DriverEntity(
    @PrimaryKey val driverId: String,
    val givenName: String,
    val familyName: String,
    val permanentNumber: String?,
    val code: String?,
    val dateOfBirth: String,
    val nationality: String,
    val url: String
)