package hr.algebra.formula1data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.algebra.formula1data.model.roomentity.RaceScheduleEntity

@Dao
interface RaceScheduleDao {

    @Query("SELECT * FROM race_schedule WHERE season = :season")
    suspend fun getRaceSchedule(season: String): List<RaceScheduleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(races: List<RaceScheduleEntity>)

    @Query("DELETE FROM race_schedule WHERE season = :season")
    suspend fun deleteRaceScheduleForSeason(season: String)
}