package hr.algebra.formula1data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import hr.algebra.formula1data.util.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "ALARM RECEIVED")
        context?.let {
            NotificationHelper.showNotification(it)
        }
    }
}
