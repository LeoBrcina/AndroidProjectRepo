package hr.algebra.formula1data.model.response

import hr.algebra.formula1data.model.ConstructorStanding
import hr.algebra.formula1data.model.DriverStanding

data class StandingsResponse(
    val MRData: StandingsMRData
)

data class StandingsMRData(
    val StandingsTable: StandingsTable
)

data class StandingsTable(
    val StandingsLists: List<StandingsList>
)

data class StandingsList(
    val DriverStandings: List<DriverStanding>?,
    val ConstructorStandings: List<ConstructorStanding>?
)