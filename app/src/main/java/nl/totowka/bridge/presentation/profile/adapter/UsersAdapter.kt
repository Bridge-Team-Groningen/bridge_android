package nl.totowka.bridge.presentation.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.like.LikeButton
import com.like.OnLikeListener
import nl.totowka.bridge.R
import nl.totowka.bridge.databinding.HolderUserBinding
import nl.totowka.bridge.domain.model.ProfileEntity
import nl.totowka.bridge.utils.Common.bind
import nl.totowka.bridge.utils.Common.getInitials
import nl.totowka.bridge.utils.Common.glideFactory
import nl.totowka.bridge.utils.Common.setGone
import nl.totowka.bridge.utils.Common.setVisible
import nl.totowka.bridge.utils.callback.UserClickListener

class UsersAdapter(
    var users: ArrayList<ProfileEntity>,
    private val listener: UserClickListener
) : RecyclerView.Adapter<UserViewHolder>() {

    private lateinit var binding: HolderUserBinding

    fun updateData(users: ArrayList<ProfileEntity>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        binding = HolderUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}

/**
 * Holder to show the events
 */
class UserViewHolder(private val binding: HolderUserBinding, private val listener: UserClickListener) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        profile: ProfileEntity
    ) {
        val context = itemView.context
        binding.name.bind(profile.name)
        binding.subtitle.bind(
            context.getString(
                R.string.user_subtitle,
                profile.interestList,
                profile.city
            )
        )
        binding.like.isLiked = profile.isLiked ?: false
        binding.root.setOnClickListener {listener.onOpenUser(profile)}
        binding.like.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                listener.onLike(profile)
                likeButton?.isLiked = true
            }

            override fun unLiked(likeButton: LikeButton?) {
                listener.onUnlike(profile)
                likeButton?.isLiked = false
            }

        })
        if (!profile.profilePicture.isNullOrBlank()) {
            Glide.with(itemView.context)
                .load(profile.profilePicture)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade(glideFactory()).crossFade(100))
                .into(binding.avatar)
            binding.avatarName.setGone()
        } else {
            binding.avatar.setImageDrawable(null)
            binding.avatarName.setVisible()
            binding.avatarName.text = profile.name?.getInitials()
        }
    }
}