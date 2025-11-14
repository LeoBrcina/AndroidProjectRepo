package hr.algebra.formula1data.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import hr.algebra.formula1data.R
import hr.algebra.formula1data.repository.F1Repository
import kotlinx.coroutines.launch

class ConstructorDetailsFragment : Fragment() {

    private lateinit var textViewName: TextView
    private lateinit var textViewNationality: TextView
    private lateinit var textViewConstructorId: TextView
    private val repository by lazy { F1Repository(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_constructor_details, container, false)

        textViewName = view.findViewById(R.id.textViewConstructorName)
        textViewNationality = view.findViewById(R.id.textViewConstructorNationality)
        textViewConstructorId = view.findViewById(R.id.textViewConstructorId)

        val constructorId = arguments?.getString("constructorId") ?: return view
        loadConstructorDetails(constructorId)

        return view
    }

    private fun loadConstructorDetails(constructorId: String) {
        lifecycleScope.launch {
            val constructor = repository.getConstructorInfo(constructorId)

            constructor?.let {
                textViewName.text = "Name: ${it.name}"
                textViewNationality.text = "Nationality: ${it.nationality}"
                textViewConstructorId.text = "ID: ${it.constructorId}"
            }
        }
    }
}