package nl.totowka.bridge.utils.callback

import nl.totowka.bridge.domain.model.ProfileEntity

interface LikeListener {
    fun onLike(user: ProfileEntity)
    fun onUnlike(user: ProfileEntity)
}