package hr.algebra.formula1data.model.response

import hr.algebra.formula1data.model.Driver

data class DriverResponse(
    val MRData: DriverMRData
)

data class DriverMRData(
    val DriverTable: DriverTable
)

data class DriverTable(
    val Drivers: List<Driver>
)