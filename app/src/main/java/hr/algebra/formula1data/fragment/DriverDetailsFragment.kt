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

class DriverDetailsFragment : Fragment() {

    private lateinit var textViewName: TextView
    private lateinit var textViewNumber: TextView
    private lateinit var textViewCode: TextView
    private lateinit var textViewBirth: TextView
    private lateinit var textViewNationality: TextView

    private lateinit var repository: F1Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_driver_details, container, false)

        textViewName = view.findViewById(R.id.textViewDriverName)
        textViewNumber = view.findViewById(R.id.textViewDriverNumber)
        textViewCode = view.findViewById(R.id.textViewDriverCode)
        textViewBirth = view.findViewById(R.id.textViewDriverBirth)
        textViewNationality = view.findViewById(R.id.textViewDriverNationality)

        repository = F1Repository(requireContext())

        val driverId = arguments?.getString("driverId") ?: return view

        loadDriverDetails(driverId)

        return view
    }

    private fun loadDriverDetails(driverId: String) {
        lifecycleScope.launch {
            try {
                val driver = repository.getDriverInfo(driverId)

                driver?.let {
                    textViewName.text = "${it.givenName} ${it.familyName}"
                    textViewNumber.text = "Number: ${it.permanentNumber ?: "N/A"}"
                    textViewCode.text = "Code: ${it.code ?: "N/A"}"
                    textViewBirth.text = "Born: ${it.dateOfBirth}"
                    textViewNationality.text = "Nationality: ${it.nationality}"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}