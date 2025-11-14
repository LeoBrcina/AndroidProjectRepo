package hr.algebra.formula1data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.algebra.formula1data.dao.*
import hr.algebra.formula1data.model.roomentity.*

@Database(
    entities = [
        DriverStandingEntity::class,
        ConstructorStandingEntity::class,
        RaceScheduleEntity::class,
        DriverEntity::class,
        ConstructorEntity::class,
        RaceResultEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class F1Database : RoomDatabase() {
    abstract fun driverStandingDao(): DriverStandingDao
    abstract fun constructorStandingDao(): ConstructorStandingDao
    abstract fun raceScheduleDao(): RaceScheduleDao
    abstract fun driverDetailsDao(): DriverDetailsDao
    abstract fun constructorDetailsDao(): ConstructorDetailsDao
    abstract fun raceResultDao(): RaceResultDao
}