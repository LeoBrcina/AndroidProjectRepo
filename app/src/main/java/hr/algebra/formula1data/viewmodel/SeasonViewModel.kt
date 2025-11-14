package hr.algebra.formula1data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SeasonViewModel : ViewModel() {

    private val _selectedYear = MutableLiveData<String>("2021")
    val selectedYear: LiveData<String> get() = _selectedYear

    fun setSelectedYear(year: String) {
        _selectedYear.value = year
    }
}