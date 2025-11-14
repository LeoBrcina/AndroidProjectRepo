package hr.algebra.formula1data.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.formula1data.R
import hr.algebra.formula1data.adapter.RaceResultAdapter
import hr.algebra.formula1data.viewmodel.RaceDetailsViewModel
import hr.algebra.formula1data.viewmodel.SeasonViewModel

class RaceDetailsFragment : Fragment() {

    private lateinit var textViewRaceHeader: TextView
    private lateinit var recyclerViewRaceResults: RecyclerView
    private lateinit var viewModel: RaceDetailsViewModel

    private val seasonViewModel: SeasonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_race_details, container, false)

        textViewRaceHeader = view.findViewById(R.id.textViewRaceDetails)
        recyclerViewRaceResults = view.findViewById(R.id.recyclerViewRaceResults)
        recyclerViewRaceResults.layoutManager = LinearLayoutManager(requireContext())

        val round = arguments?.getString("round") ?: return view

        viewModel = ViewModelProvider(this)[RaceDetailsViewModel::class.java]

        observeViewModel()

        seasonViewModel.selectedYear.observe(viewLifecycleOwner) { year ->
            viewModel.loadRaceResults(year, round)
        }

        return view
    }

    private fun observeViewModel() {
        viewModel.raceNameAndDate.observe(viewLifecycleOwner) { nameAndDate ->
            textViewRaceHeader.text = nameAndDate
        }

        viewModel.raceResults.observe(viewLifecycleOwner) { results ->
            recyclerViewRaceResults.adapter = RaceResultAdapter(results)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                textViewRaceHeader.text = it
            }
        }
    }
}