package nl.totowka.bridge.utils.callback

import nl.totowka.bridge.domain.model.ProfileEntity

interface UserClickListener {
    fun onLike(user: ProfileEntity)
    fun onUnlike(user: ProfileEntity)
    fun onOpenUser(user: ProfileEntity)
}