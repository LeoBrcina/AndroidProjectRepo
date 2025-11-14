package hr.algebra.formula1data.activity

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import hr.algebra.formula1data.R
import hr.algebra.formula1data.receiver.ConnectivityReceiver
import hr.algebra.formula1data.receiver.AlarmReceiver
import hr.algebra.formula1data.viewmodel.SeasonViewModel
import hr.algebra.formula1data.worker.SeasonSyncWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var themeSwitchButton: ImageButton
    private lateinit var seasonYearsTextView: TextView
    private lateinit var connectivityReceiver: ConnectivityReceiver
    private val seasons = (1950..2024).map { it.toString() }

    private val seasonViewModel: SeasonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        NavigationUI.setupWithNavController(bottomNav, navController)

        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)

        themeSwitchButton = findViewById(R.id.themeSwitchButton)
        updateThemeIcon()

        themeSwitchButton.setOnClickListener {
            toggleTheme()
        }

        seasonYearsTextView = findViewById(R.id.seasonYears)
        seasonYearsTextView.setOnClickListener {
            showYearDialog()
        }

        // 👇 Handle season sync from notification
        val syncedYear = intent.getStringExtra("synced_year")
        syncedYear?.let {
            seasonViewModel.setSelectedYear(it)
        }

        // Observe and update toolbar text
        seasonViewModel.selectedYear.observe(this) { year ->
            seasonYearsTextView.text = "$year"
        }

        requestPermissions()
        scheduleRepeatingAlarm()
        scheduleSeasonSyncWorker()
    }

    override fun onResume() {
        super.onResume()
        connectivityReceiver = ConnectivityReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectivityReceiver)
    }

    private fun scheduleRepeatingAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                return
            }
        }

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val triggerAtMillis = SystemClock.elapsedRealtime() + 1 * 60 * 1000L
        val intervalMillis = 60 * 60 * 1000L
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            intervalMillis,
            pendingIntent
        )
    }

    private fun scheduleSeasonSyncWorker() {
        val syncRequest = PeriodicWorkRequestBuilder<SeasonSyncWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "season_sync_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }

    private fun requestPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_NETWORK_STATE)
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1002)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1002) {
            if (grantResults.isNotEmpty()) {
                for (i in permissions.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "${permissions[i]} permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun toggleTheme() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun updateThemeIcon() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            themeSwitchButton.setImageResource(R.drawable.ic_dark_mode)
        } else {
            themeSwitchButton.setImageResource(R.drawable.ic_light_mode)
        }
    }

    private fun showYearDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Year")
        builder.setItems(seasons.toTypedArray()) { _, which ->
            val selectedYear = seasons[which]
            seasonViewModel.setSelectedYear(selectedYear)
        }
        builder.create().show()
    }
}
