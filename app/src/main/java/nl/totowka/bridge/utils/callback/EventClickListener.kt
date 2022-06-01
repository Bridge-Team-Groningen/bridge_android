package nl.totowka.bridge.utils.callback

import nl.totowka.bridge.domain.model.EventEntity

/**
 * Interface to interact with onClick event for RecyclerView.
 */
interface EventClickListener {
    fun onClick(word: EventEntity, position: Int)
}