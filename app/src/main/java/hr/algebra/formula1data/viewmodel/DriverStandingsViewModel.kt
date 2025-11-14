package hr.algebra.formula1data.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import hr.algebra.formula1data.model.roomentity.DriverStandingEntity
import hr.algebra.formula1data.repository.F1Repository
import kotlinx.coroutines.launch

class DriverStandingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = F1Repository(application)

    private val _driverStandings = MutableLiveData<List<DriverStandingEntity>>()
    val driverStandings: LiveData<List<DriverStandingEntity>> get() = _driverStandings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var currentYear: String? = null

    fun loadDriverStandings(year: String) {
        if (year == currentYear && !_driverStandings.value.isNullOrEmpty()) return
        currentYear = year

        Log.i("DriverStandings", "Loading driver standings for year: $year")
        _error.value = null
        _isLoading.value = true
        _driverStandings.value = emptyList()

        viewModelScope.launch {
            try {
                val cached = repository.getDriverStandings(year)
                if (cached.isNotEmpty() && cached.size > 5) {
                    _driverStandings.value = cached
                    Log.i("DriverStandings", "Loaded from cache, size = ${cached.size}")
                } else {
                    Log.i("DriverStandings", "Cache empty or partial, fetching fresh...")
                    val fresh = repository.getDriverStandings(year)
                    if (fresh.isNotEmpty()) {
                        _driverStandings.value = fresh
                        Log.i("DriverStandings", "Loaded from API, size = ${fresh.size}")
                    } else {
                        _error.value = "Driver Standings are not available for this season."
                        Log.w("DriverStandings", "API returned empty data")
                    }
                }
            } catch (e: Exception) {
                Log.e("DriverStandings", "Error loading data: ${e.localizedMessage}")
                _error.value = "Failed to load driver standings."
            } finally {
                _isLoading.value = false
            }
        }
    }
}