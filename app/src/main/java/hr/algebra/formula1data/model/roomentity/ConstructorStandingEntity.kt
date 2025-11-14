package hr.algebra.formula1data.model.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "constructor_standings")
data class ConstructorStandingEntity(
    @PrimaryKey val id: String,
    val constructorId: String,
    val position: String,
    val points: String,
    val wins: String,
    val name: String,
    val nationality: String,
    val season: String
)
