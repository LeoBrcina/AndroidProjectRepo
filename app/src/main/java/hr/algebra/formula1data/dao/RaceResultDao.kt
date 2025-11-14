package hr.algebra.formula1data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.algebra.formula1data.model.roomentity.RaceResultEntity

@Dao
interface RaceResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: RaceResultEntity)

    @Query("SELECT * FROM race_results WHERE season = :season AND round = :round")
    suspend fun getRaceResult(season: String, round: String): RaceResultEntity?

    @Query("DELETE FROM race_results")
    suspend fun deleteAll()
}