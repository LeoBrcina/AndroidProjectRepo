package hr.algebra.formula1data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.algebra.formula1data.model.roomentity.DriverEntity

@Dao
interface DriverDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(driver: DriverEntity)

    @Query("SELECT * FROM driver_details WHERE driverId = :driverId")
    suspend fun getDriver(driverId: String): DriverEntity?

    @Query("DELETE FROM driver_details")
    suspend fun deleteAll()
}