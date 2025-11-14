package hr.algebra.formula1data.model.response

import hr.algebra.formula1data.model.RaceResult

data class RaceResultResponse(
    val MRData: RaceResultMRData
)

data class RaceResultMRData(
    val RaceTable: RaceResultTable
)

data class RaceResultTable(
    val Races: List<RaceWithResults>
)

data class RaceWithResults(
    val season: String,
    val round: String,
    val raceName: String,
    val date: String,
    val Results: List<RaceResult>
)