package hr.algebra.formula1data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.formula1data.R
import hr.algebra.formula1data.model.Race

class RaceCalendarAdapter(
    private val onRaceClick: (Race) -> Unit
) : ListAdapter<Race, RaceCalendarAdapter.RaceViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_race, parent, false)
        return RaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: RaceViewHolder, position: Int) {
        holder.bind(getItem(position), onRaceClick)
    }

    class RaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roundTextView: TextView = itemView.findViewById(R.id.textViewRound)
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewRaceName)
        private val dateTextView: TextView = itemView.findViewById(R.id.textViewRaceDate)

        fun bind(race: Race, onClick: (Race) -> Unit) {
            roundTextView.text = "Round ${race.round}"
            nameTextView.text = race.raceName
            dateTextView.text = race.date

            itemView.setOnClickListener { onClick(race) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Race>() {
            override fun areItemsTheSame(oldItem: Race, newItem: Race): Boolean {
                return oldItem.round == newItem.round && oldItem.raceName == newItem.raceName
            }

            override fun areContentsTheSame(oldItem: Race, newItem: Race): Boolean {
                return oldItem == newItem
            }
        }
    }
}