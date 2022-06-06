package nl.totowka.bridge.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import nl.totowka.bridge.R
import java.text.SimpleDateFormat
import java.util.*


/**
 * Object to work with common methods during UI work.
 */
object Common {
    /**
     * Checking the Internet connection.
     *
     * @param context app's context
     */
    fun isConnectedInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    /**
     * Making [View] to [View.GONE] mode
     */
    fun View.setGone() {
        this.visibility = View.GONE
    }

    /**
     * Making [View] to [View.VISIBLE] mode
     */
    fun View.setVisible() {
        visibility = View.VISIBLE
    }

    /**
     * Making [View] to [View.INVISIBLE] mode
     */
    fun View.setInvisible() {
        visibility = View.INVISIBLE
    }

    /**
     * Getting [Color] from [ColorRes]
     */
    fun Context.color(@ColorRes colorId: Int) = this.resources.getColor(colorId)

    /**
     * Getting [String] from [StringRes]
     */
    fun Context.string(@StringRes resId: Int) = this.resources.getString(resId)

    fun EditText.isEmpty() = this.text.toString().isEmpty()
    fun TextInputLayout.isNotEmpty() = this.editText?.text.toString().isNotEmpty()
    fun TextInputLayout.isEmpty() = this.editText?.text.toString().isEmpty()
    fun EditText.text() = this.text.toString()

    fun FragmentActivity.setNavBottomVisibility(isVisible: Boolean) =
        this.findViewById<BottomNavigationView>(
            R.id.bottom_nav
        ).apply { this.isVisible = isVisible }

    fun Date.toCoolString(): String = SimpleDateFormat("dd MMM yyyy HH:mm").format(this)

    fun String?.getInitials(): String {
        if (this == null) return ""
        return split(" ").take(2).map { if (it.isNotEmpty()) it[0] else "" }.joinToString(separator = "")
    }

    fun TextView.bind(text: CharSequence?) {
        if (text.isNullOrBlank()) {
            this.setGone()
        } else {
            this.text = text
            this.setVisible()
        }
    }

    fun glideFactory() = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
}