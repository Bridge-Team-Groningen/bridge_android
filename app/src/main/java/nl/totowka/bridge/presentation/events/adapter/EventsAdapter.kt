package nl.totowka.bridge.presentation.events.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import nl.totowka.bridge.R
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.utils.callback.EventClickListener


/**
 * Adapter to show events in recyclerview
 */
class EventsAdapter(
    private var events: ArrayList<EventEntity>,
    private var clickListener: EventClickListener
) : RecyclerView.Adapter<EventViewHolder>() {

    fun updateData(events: ArrayList<EventEntity>) {
        this.events.clear()
        this.events.addAll(events)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventViewHolder(inflater.inflate(R.layout.holder_event, parent, false))
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position], clickListener)
    }

    override fun getItemCount(): Int {
        return events.size
    }
}

/**
 * Holder to show the events
 */
class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.image)
    private val placeTextView: TextView = itemView.findViewById(R.id.place)
    private val timeTextView: TextView = itemView.findViewById(R.id.time)
    private val peopleTextView: TextView = itemView.findViewById(R.id.people_amount)

    fun bind(event: EventEntity, clickListener: EventClickListener) {
        val context = itemView.context
        itemView.setOnClickListener { clickListener.onClick(event) }
//        Glide.with(context)
//            .load(context.resources.getDrawable(event.image, null))
//            .circleCrop()
//            .into(imageView)
        placeTextView.text = event.location
        timeTextView.text = event.date.toString()
        peopleTextView.text = context.getString(R.string.people, event.noOfParticipants)
    }
}