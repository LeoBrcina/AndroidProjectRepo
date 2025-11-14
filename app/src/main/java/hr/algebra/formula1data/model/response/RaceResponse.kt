package hr.algebra.formula1data.model.response

import hr.algebra.formula1data.model.Race

data class RaceResponse(
    val MRData: RaceMRData
)

data class RaceMRData(
    val RaceTable: RaceTable
)

data class RaceTable(
    val Races: List<Race>
)