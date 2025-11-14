package hr.algebra.formula1data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.algebra.formula1data.model.RaceResult
import hr.algebra.formula1data.repository.F1Repository
import kotlinx.coroutines.launch

class RaceDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = F1Repository(application)

    private val _raceResults = MutableLiveData<List<RaceResult>>()
    val raceResults: LiveData<List<RaceResult>> get() = _raceResults

    private val _raceNameAndDate = MutableLiveData<String>()
    val raceNameAndDate: LiveData<String> get() = _raceNameAndDate

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadRaceResults(year: String, round: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val cached = repository.getCachedRaceResults(year, round)
            if (cached != null) {
                _raceResults.value = cached.results
                _raceNameAndDate.value = "${cached.raceName} (${cached.date})"
            }

            try {
                val data = repository.getRaceResultsWithInfo(year, round)
                if (data != null) {
                    _raceResults.value = data.results
                    _raceNameAndDate.value = "${data.raceName} (${data.date})"
                } else if (cached == null) {
                    _raceNameAndDate.value = "No race results available."
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (cached == null) {
                    _error.value = "Failed to load race results."
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}