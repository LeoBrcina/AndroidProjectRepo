package hr.algebra.formula1data.repository

import android.content.Context
import com.google.gson.Gson
import hr.algebra.formula1data.api.ErgastApiClient
import hr.algebra.formula1data.model.*
import hr.algebra.formula1data.model.roomentity.*
import hr.algebra.formula1data.provider.F1DatabaseProvider

class F1Repository(context: Context) {

    private val api = ErgastApiClient.api
    private val db = F1DatabaseProvider.getDatabase(context)

    private val driverDao = db.driverStandingDao()
    private val constructorDao = db.constructorStandingDao()
    private val raceScheduleDao = db.raceScheduleDao()
    private val driverDetailsDao = db.driverDetailsDao()
    private val constructorDetailsDao = db.constructorDetailsDao()
    private val raceDetailsDao = db.raceResultDao()

    private val gson = Gson()

    suspend fun getDriverStandings(year: String): List<DriverStandingEntity> {
        val cached = driverDao.getStandingsForSeason(year)
        if (cached.isNotEmpty()) return cached

        return try {
            val response = api.getDriverStandings(year)
            val standings = response.MRData.StandingsTable.StandingsLists.firstOrNull()?.DriverStandings

            val entities = standings?.mapNotNull {
                val driver = it.driver
                val constructor = it.constructors?.firstOrNull()
                if (driver != null && constructor != null) {
                    DriverStandingEntity(
                        id = "${year}_${driver.driverId}",
                        driverId = driver.driverId,
                        position = it.position,
                        points = it.points,
                        wins = it.wins,
                        givenName = driver.givenName,
                        familyName = driver.familyName,
                        constructorName = constructor.name,
                        season = year
                    )
                } else null
            } ?: emptyList()

            driverDao.insertAll(entities)
            entities
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getConstructorStandings(year: String): List<ConstructorStandingEntity> {
        val cached = constructorDao.getStandingsForSeason(year)
        if (cached.isNotEmpty()) return cached

        return try {
            val response = api.getConstructorStandings(year)
            val standings = response.MRData.StandingsTable.StandingsLists.firstOrNull()?.ConstructorStandings

            val entities = standings?.map {
                ConstructorStandingEntity(
                    id = "${year}_${it.constructor.constructorId}",
                    constructorId = it.constructor.constructorId,
                    position = it.position,
                    points = it.points,
                    wins = it.wins,
                    name = it.constructor.name,
                    nationality = it.constructor.nationality,
                    season = year
                )
            } ?: emptyList()

            constructorDao.insertAll(entities)
            entities
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getRaceSchedule(year: String): List<RaceScheduleEntity> {
        val cached = raceScheduleDao.getRaceSchedule(year)
        if (cached.isNotEmpty()) return cached

        return try {
            val response = api.getRaceSchedule(year)
            val races = response.MRData.RaceTable.Races

            val entities = races.mapNotNull { race ->
                val circuit = race.circuit
                val location = circuit?.location
                if (circuit != null && location != null) {
                    RaceScheduleEntity(
                        id = "${race.season}_${race.round}",
                        round = race.round,
                        season = race.season,
                        raceName = race.raceName,
                        date = race.date,
                        time = race.time,
                        circuitId = circuit.circuitId,
                        circuitName = circuit.circuitName,
                        url = circuit.url,
                        locality = location.locality,
                        country = location.country
                    )
                } else null
            }

            raceScheduleDao.insertAll(entities)
            entities
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getDriverInfo(driverId: String): Driver? {
        val cached = driverDetailsDao.getDriver(driverId)
        if (cached != null) {
            return Driver(
                driverId = cached.driverId,
                permanentNumber = cached.permanentNumber,
                code = cached.code,
                url = cached.url,
                givenName = cached.givenName,
                familyName = cached.familyName,
                dateOfBirth = cached.dateOfBirth,
                nationality = cached.nationality
            )
        }

        return try {
            val response = api.getDriver(driverId)
            val driver = response.MRData.DriverTable.Drivers.firstOrNull()
            driver?.let {
                driverDetailsDao.insert(
                    DriverEntity(
                        driverId = it.driverId,
                        permanentNumber = it.permanentNumber,
                        code = it.code,
                        url = it.url,
                        givenName = it.givenName,
                        familyName = it.familyName,
                        dateOfBirth = it.dateOfBirth,
                        nationality = it.nationality
                    )
                )
            }
            driver
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getConstructorInfo(constructorId: String): Constructor? {
        val cached = constructorDetailsDao.getConstructor(constructorId)
        if (cached != null) {
            return Constructor(
                constructorId = cached.constructorId,
                url = cached.url,
                name = cached.name,
                nationality = cached.nationality
            )
        }

        return try {
            val response = api.getConstructor(constructorId)
            val constructor = response.MRData.ConstructorTable.Constructors.firstOrNull()
            constructor?.let {
                constructorDetailsDao.insert(
                    ConstructorEntity(
                        constructorId = it.constructorId,
                        url = it.url,
                        name = it.name,
                        nationality = it.nationality
                    )
                )
            }
            constructor
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getRaceResultsWithInfo(season: String, round: String): RaceResultsWithInfo? {
        val cached = raceDetailsDao.getRaceResult(season, round)
        if (cached != null) {
            val results = gson.fromJson(
                cached.resultsJson,
                Array<RaceResult>::class.java
            ).toList()
            return RaceResultsWithInfo(
                raceName = cached.raceName,
                date = cached.date,
                results = results
            )
        }

        return try {
            val response = api.getRaceResults(season, round)
            val race = response.MRData.RaceTable.Races.firstOrNull()
            val results = race?.Results
            if (results != null) {
                val entity = RaceResultEntity(
                    round = round,
                    season = season,
                    raceName = race.raceName,
                    date = race.date,
                    resultsJson = gson.toJson(results)
                )
                raceDetailsDao.insert(entity)

                RaceResultsWithInfo(
                    raceName = race.raceName,
                    date = race.date,
                    results = results
                )
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getCachedRaceResults(season: String, round: String): RaceResultsWithInfo? {
        val cached = raceDetailsDao.getRaceResult(season, round)
        return if (cached != null) {
            val results = gson.fromJson(
                cached.resultsJson,
                Array<RaceResult>::class.java
            ).toList()
            RaceResultsWithInfo(
                raceName = cached.raceName,
                date = cached.date,
                results = results
            )
        } else null
    }
}
