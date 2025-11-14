package hr.algebra.formula1data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import hr.algebra.formula1data.repository.F1Repository
import hr.algebra.formula1data.util.NotificationHelper

class SeasonSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repository = F1Repository(context)
    private val sharedPreferences = context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)

    override suspend fun doWork(): Result {
        val lastYear = sharedPreferences.getInt("last_synced_year", 1949)
        val nextYear = lastYear + 1

        if (nextYear > 2024) {
            Log.d("SeasonSyncWorker", "All seasons synced.")
            return Result.success()
        }

        return try {
            Log.d("SeasonSyncWorker", "Syncing season $nextYear")

            repository.getDriverStandings(nextYear.toString())
            repository.getConstructorStandings(nextYear.toString())
            repository.getRaceSchedule(nextYear.toString())

            sharedPreferences.edit().putInt("last_synced_year", nextYear).apply()

            NotificationHelper.showSyncNotification(applicationContext, "Season $nextYear synced", nextYear.toString())
            Result.success()
        } catch (e: Exception) {
            Log.e("SeasonSyncWorker", "Failed to sync season $nextYear", e)
            Result.retry()
        }
    }
}
