package nl.totowka.bridge.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import androidx.annotation.StringRes

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
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
     * Getting [String] from [StringRes]
     */
    fun Context.string(@StringRes resId: Int) = this.resources.getString(resId)
}