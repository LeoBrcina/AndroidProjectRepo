package hr.algebra.formula1data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.algebra.formula1data.model.roomentity.ConstructorEntity

@Dao
interface ConstructorDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(constructor: ConstructorEntity)

    @Query("SELECT * FROM constructor_details WHERE constructorId = :constructorId")
    suspend fun getConstructor(constructorId: String): ConstructorEntity?

    @Query("DELETE FROM constructor_details")
    suspend fun deleteAll()
}