package hr.algebra.formula1data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

class ConnectivityReceiver : BroadcastReceiver() {

    private var isFirstBroadcast = true
    private var wasOffline = false

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return

        val online = isOnline(context)

        if (isFirstBroadcast) {
            isFirstBroadcast = false
            wasOffline = !online

            if (wasOffline) {
                Toast.makeText(
                    context,
                    "Offline mode: Only previously viewed F1 data is available.",
                    Toast.LENGTH_LONG
                ).show()
            }

            return
        }

        if (online && wasOffline) {
            Toast.makeText(
                context,
                "Back online: All F1 data is available.",
                Toast.LENGTH_LONG
            ).show()
            wasOffline = false
        } else if (!online) {
            Toast.makeText(
                context,
                "Offline mode: Only previously viewed F1 data is available.",
                Toast.LENGTH_LONG
            ).show()
            wasOffline = true
        }
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}
