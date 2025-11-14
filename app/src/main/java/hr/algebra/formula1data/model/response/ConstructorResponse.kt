package hr.algebra.formula1data.model.response

import hr.algebra.formula1data.model.Constructor

data class ConstructorResponse(
    val MRData: ConstructorMRData
)

data class ConstructorMRData(
    val ConstructorTable: ConstructorTable
)

data class ConstructorTable(
    val Constructors: List<Constructor>
)