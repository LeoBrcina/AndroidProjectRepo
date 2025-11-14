package hr.algebra.formula1data.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.algebra.formula1data.model.roomentity.DriverStandingEntity

@Dao
interface DriverStandingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(driverStandings: List<DriverStandingEntity>)

    @Query("SELECT * FROM driver_standings WHERE season = :season")
    suspend fun getStandingsForSeason(season: String): List<DriverStandingEntity>

    @Query("DELETE FROM driver_standings WHERE season = :season")
    suspend fun deleteStandingsForSeason(season: String)

    @Query("SELECT * FROM driver_standings")
    fun getAllCursor(): Cursor
}