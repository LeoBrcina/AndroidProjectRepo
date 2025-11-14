package hr.algebra.formula1data.model

data class RaceResultsWithInfo(
    val raceName: String,
    val date: String,
    val results: List<RaceResult>
)