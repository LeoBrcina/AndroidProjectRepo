package hr.algebra.formula1data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.formula1data.R
import hr.algebra.formula1data.model.roomentity.ConstructorStandingEntity

class ConstructorStandingsAdapter(
    private val onConstructorClick: (String) -> Unit
) : ListAdapter<ConstructorStandingEntity, ConstructorStandingsAdapter.ConstructorViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstructorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_constructor_standings, parent, false)
        return ConstructorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConstructorViewHolder, position: Int) {
        holder.bind(getItem(position), onConstructorClick)
    }

    class ConstructorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val positionTextView: TextView = itemView.findViewById(R.id.textViewPosition)
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        private val nationalityTextView: TextView = itemView.findViewById(R.id.textViewNationality)
        private val pointsTextView: TextView = itemView.findViewById(R.id.textViewPoints)

        fun bind(standing: ConstructorStandingEntity, onClick: (String) -> Unit) {
            positionTextView.text = standing.position
            nameTextView.text = standing.name
            nationalityTextView.text = standing.nationality
            pointsTextView.text = "${standing.points} pts"

            itemView.setOnClickListener {
                onClick(standing.constructorId)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ConstructorStandingEntity>() {
            override fun areItemsTheSame(oldItem: ConstructorStandingEntity, newItem: ConstructorStandingEntity): Boolean {
                return oldItem.constructorId == newItem.constructorId
            }

            override fun areContentsTheSame(oldItem: ConstructorStandingEntity, newItem: ConstructorStandingEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}