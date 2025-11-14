package hr.algebra.formula1data.api

import hr.algebra.formula1data.model.*
import hr.algebra.formula1data.model.response.*
import retrofit2.http.GET
import retrofit2.http.Path

interface ErgastApiService {

    @GET("{year}/driverStandings.json")
    suspend fun getDriverStandings(@Path("year") year: String): StandingsResponse

    @GET("{year}/constructorStandings.json")
    suspend fun getConstructorStandings(@Path("year") year: String): StandingsResponse

    @GET("{year}.json")
    suspend fun getRaceSchedule(@Path("year") year: String): RaceResponse

    @GET("{year}/{round}/results.json")
    suspend fun getRaceResults(
        @Path("year") year: String,
        @Path("round") round: String
    ): RaceResultResponse

    @GET("drivers/{driverId}.json")
    suspend fun getDriver(@Path("driverId") driverId: String): DriverResponse

    @GET("constructors/{constructorId}.json")
    suspend fun getConstructor(@Path("constructorId") constructorId: String): ConstructorResponse
}