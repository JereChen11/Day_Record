package com.day.record.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.day.record.R
import com.day.record.databinding.ActivityMainBinding
import com.day.record.ui.calendar.CalendarActivity
import com.day.record.ui.day.DayTaskFragment
import com.day.record.ui.year.YearTaskFragment

/**
 * @author Jere
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includeView.apply {
            setSupportActionBar(toolbar)
            toolbar.title = getString(R.string.day_task)

            bottomNavView.apply {
                selectedItemId = R.id.bottomNavDayTask
                setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.bottomNavDayTask -> {
                            toolbar.title = getString(R.string.day_task)
                            supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                replace<DayTaskFragment>(R.id.fcv)
                            }
                            true
                        }
                        R.id.bottomNavYearTask -> {
                            toolbar.title = getString(R.string.year_task)
                            supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                replace<YearTaskFragment>(R.id.fcv)
                            }
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        scheduleAlarm()

    }

    private fun scheduleAlarm() {
        Log.e("MainActivity", "testAlarm: ")
        val alarmIntent = Intent(this@MainActivity, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.TITLE_KEY, getString(R.string.app_name))
            putExtra(AlarmReceiver.CONTENT_KEY, getString(R.string.check_today))
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
        }

        /**
         * 测试结果
         * 在 1 个小时后唤醒设备并触发闹钟，此后每 15 分钟触发一次
         * 测试机型：Google Pixel 3a XL, Android 12
         *
         * 1. 触发PendingIntent，退出到后台，不杀死进程，手机不关锁，可以收到通知。
         * 2. 触发PendingIntent，直接杀死APP进程，手机不关锁，可以收到通知。
         * 3. 触发PendingIntent，退出到后台，不杀死进程，手机关锁，可以收到通知。
         * 4. 触发PendingIntent，直接杀死APP进程，手机关锁，可以收到通知。测了即使是一个小时后触发也是可收到通知
         */
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 3600 * 1000,//test for 1h
            AlarmManager.INTERVAL_DAY,//隔天提示
            alarmPendingIntent
        )

        //Toast.makeText(this, "send testAlarm", Toast.LENGTH_LONG).show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.calendarItem -> {
                startActivity(Intent(this, CalendarActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}