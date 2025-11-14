package hr.algebra.formula1data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.formula1data.R
import hr.algebra.formula1data.model.roomentity.DriverStandingEntity

class DriverStandingsAdapter(
    private val onDriverClick: (driverId: String) -> Unit
) : ListAdapter<DriverStandingEntity, DriverStandingsAdapter.DriverStandingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverStandingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_driver_standing, parent, false)
        return DriverStandingViewHolder(view)
    }

    override fun onBindViewHolder(holder: DriverStandingViewHolder, position: Int) {
        holder.bind(getItem(position), onDriverClick)
    }

    class DriverStandingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val positionTextView: TextView = itemView.findViewById(R.id.textViewPosition)
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        private val teamTextView: TextView = itemView.findViewById(R.id.textViewTeam)
        private val pointsTextView: TextView = itemView.findViewById(R.id.textViewPoints)

        fun bind(standing: DriverStandingEntity, onClick: (String) -> Unit) {
            positionTextView.text = standing.position
            nameTextView.text = "${standing.givenName} ${standing.familyName}"
            teamTextView.text = standing.constructorName
            pointsTextView.text = "${standing.points} pts"

            itemView.setOnClickListener {
                onClick(standing.driverId)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DriverStandingEntity>() {
            override fun areItemsTheSame(oldItem: DriverStandingEntity, newItem: DriverStandingEntity): Boolean {
                return oldItem.driverId == newItem.driverId
            }

            override fun areContentsTheSame(oldItem: DriverStandingEntity, newItem: DriverStandingEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}