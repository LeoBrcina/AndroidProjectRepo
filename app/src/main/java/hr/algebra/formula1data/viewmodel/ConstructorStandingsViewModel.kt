package hr.algebra.formula1data.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import hr.algebra.formula1data.model.roomentity.ConstructorStandingEntity
import hr.algebra.formula1data.repository.F1Repository
import kotlinx.coroutines.launch

class ConstructorStandingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = F1Repository(application)

    private val _constructorStandings = MutableLiveData<List<ConstructorStandingEntity>>()
    val constructorStandings: LiveData<List<ConstructorStandingEntity>> get() = _constructorStandings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var currentYear: String? = null

    fun loadConstructorStandings(year: String) {
        if (year == currentYear && !_constructorStandings.value.isNullOrEmpty()) return
        currentYear = year

        Log.i("ConstructorStandings", "Loading constructor standings for year: $year")
        _error.value = null
        _isLoading.value = true
        _constructorStandings.value = emptyList()

        viewModelScope.launch {
            try {
                val result = repository.getConstructorStandings(year)
                if (result.isEmpty()) {
                    _error.value = "Constructor Standings are not available for this season."
                    Log.w("ConstructorStandings", "No standings found for year: $year")
                } else {
                    _constructorStandings.value = result
                    Log.i("ConstructorStandings", "Standings loaded, size = ${result.size}")
                }
            } catch (e: Exception) {
                Log.e("ConstructorStandings", "Error loading data: ${e.localizedMessage}")
                _error.value = "Failed to load constructor standings."
            } finally {
                _isLoading.value = false
            }
        }
    }
}