package hr.algebra.formula1data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.formula1data.R
import hr.algebra.formula1data.model.RaceResult

class RaceResultAdapter(private val results: List<RaceResult>) :
    RecyclerView.Adapter<RaceResultAdapter.RaceResultViewHolder>() {

    class RaceResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewResult: TextView = view.findViewById(R.id.textViewRaceResultItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_race_result, parent, false)
        return RaceResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: RaceResultViewHolder, position: Int) {
        val result = results[position]
        val driverName = result.driver?.let { "${it.givenName} ${it.familyName}" } ?: "Unknown Driver"
        val constructorName = result.constructor?.name ?: "Unknown Team"
        val time = result.raceTime?.time ?: result.status

        holder.textViewResult.text = "${position + 1}. $driverName - $constructorName (${time})"
    }

    override fun getItemCount(): Int = results.size
}