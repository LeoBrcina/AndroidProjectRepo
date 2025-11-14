package hr.algebra.formula1data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.algebra.formula1data.model.roomentity.ConstructorStandingEntity

@Dao
interface ConstructorStandingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(constructorStandings: List<ConstructorStandingEntity>)

    @Query("SELECT * FROM constructor_standings WHERE season = :season")
    suspend fun getStandingsForSeason(season: String): List<ConstructorStandingEntity>

    @Query("DELETE FROM constructor_standings WHERE season = :season")
    suspend fun deleteStandingsForSeason(season: String)
}