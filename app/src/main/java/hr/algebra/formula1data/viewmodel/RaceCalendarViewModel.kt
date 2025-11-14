package hr.algebra.formula1data.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import hr.algebra.formula1data.model.*
import hr.algebra.formula1data.repository.F1Repository
import kotlinx.coroutines.launch

class RaceCalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = F1Repository(application)

    private val _raceSchedule = MutableLiveData<List<Race>>()
    val raceSchedule: LiveData<List<Race>> get() = _raceSchedule

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var currentYear: String? = null

    fun loadRaceSchedule(year: String) {
        if (year == currentYear && !_raceSchedule.value.isNullOrEmpty()) return
        currentYear = year

        Log.i("RaceCalendar", "Loading race schedule for year: $year")
        _error.value = null
        _isLoading.value = true
        _raceSchedule.value = emptyList()

        viewModelScope.launch {
            try {
                val result = repository.getRaceSchedule(year)
                if (result.isEmpty()) {
                    _error.value = "Race Schedule is not available for this season."
                    Log.w("RaceCalendar", "No race schedule found for year: $year")
                } else {
                    val races = result.map {
                        Race(
                            season = it.season,
                            round = it.round,
                            raceName = it.raceName,
                            date = it.date,
                            time = it.time,
                            circuit = Circuit(
                                circuitId = it.circuitId,
                                url = it.url,
                                circuitName = it.circuitName,
                                location = Location(
                                    locality = it.locality,
                                    country = it.country
                                )
                            )
                        )
                    }
                    _raceSchedule.value = races
                    Log.i("RaceCalendar", "Race schedule loaded, count = ${races.size}")
                }
            } catch (e: Exception) {
                Log.e("RaceCalendar", "Error loading race schedule: ${e.localizedMessage}")
                _error.value = "Failed to load race schedule."
            } finally {
                _isLoading.value = false
            }
        }
    }
}