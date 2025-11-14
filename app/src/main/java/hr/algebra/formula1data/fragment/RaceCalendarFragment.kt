package hr.algebra.formula1data.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.formula1data.R
import hr.algebra.formula1data.adapter.RaceCalendarAdapter
import hr.algebra.formula1data.viewmodel.RaceCalendarViewModel
import hr.algebra.formula1data.viewmodel.SeasonViewModel

class RaceCalendarFragment : Fragment() {

    private val viewModel: RaceCalendarViewModel by viewModels()
    private val seasonViewModel: SeasonViewModel by activityViewModels()

    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RaceCalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_race_calendar, container, false)

        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.textViewError)
        recyclerView = view.findViewById(R.id.recyclerViewRaceCalendar)

        adapter = RaceCalendarAdapter { race ->
            val action = RaceCalendarFragmentDirections
                .actionRaceCalendarFragmentToRaceDetailsFragment(race.round)
            findNavController().navigate(action)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        observeViewModel()

        seasonViewModel.selectedYear.observe(viewLifecycleOwner) { year ->
            viewModel.loadRaceSchedule(year)
        }

        return view
    }

    private fun observeViewModel() {
        viewModel.raceSchedule.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            errorTextView.visibility = if (error != null) View.VISIBLE else View.GONE
            errorTextView.text = error ?: ""
        }
    }
}