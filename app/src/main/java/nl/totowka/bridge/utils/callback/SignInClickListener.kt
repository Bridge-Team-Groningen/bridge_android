package nl.totowka.bridge.utils.callback

import nl.totowka.bridge.domain.model.EventEntity

interface SignInClickListener {
    fun onClick(event: EventEntity, position: Int)
}