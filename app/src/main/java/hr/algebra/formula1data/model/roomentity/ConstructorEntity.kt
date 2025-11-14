package hr.algebra.formula1data.model.roomentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "constructor_details")
data class ConstructorEntity(
    @PrimaryKey val constructorId: String,
    val name: String,
    val nationality: String,
    val url: String
)