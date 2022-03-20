package nl.totowka.bridge.presentation.trending.adapter

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import nl.totowka.bridge.R
import nl.totowka.bridge.domain.model.EventEntity
import nl.totowka.bridge.utils.callback.EventClickListener
import java.io.IOException
import java.net.URI

/**
 * Адаптер для отображения слов в BottomSheet
 */
class TrendingEventsAdapter(
    private var events: List<EventEntity>,
    private var clickListener: EventClickListener
) : RecyclerView.Adapter<EventViewHolder>() {

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
 * Holder для отображения смысла слова
 */
class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.image)
    private val placeTextView: TextView = itemView.findViewById(R.id.place)
    private val timeTextView: TextView = itemView.findViewById(R.id.time)
    private val peopleTextView: TextView = itemView.findViewById(R.id.people_amount)

    fun bind(event: EventEntity, clickListener: EventClickListener) {
        val context = itemView.context
        itemView.setOnClickListener { clickListener.onClick(event) }
        Glide.with(context)
            .load(context.resources.getDrawable(event.image, null))
            .circleCrop()
            .into(imageView)
        placeTextView.text = event.place
        timeTextView.text = event.time
        peopleTextView.text = context.getString(R.string.people, event.people)
    }
}