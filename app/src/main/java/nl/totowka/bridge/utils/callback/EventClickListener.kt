package nl.totowka.bridge.utils.callback

import nl.totowka.bridge.domain.model.EventEntity

/**
 * Интерфейс для работы с нажатием на элемент RecyclerView
 */
interface EventClickListener {
    fun onClick(word: EventEntity)
}