package com.day.record.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.day.record.R
import com.day.record.ui.calendar.CalendarActivity
import com.day.record.ui.day.DayTaskFragment
import com.day.record.ui.year.YearTaskFragment
import kotlinx.android.synthetic.main.app_bar_main.*

/**
 * @author Jere
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.day_task)

        bottomNavView.apply {
            selectedItemId = R.id.bottomNavDayTask
            setOnNavigationItemSelectedListener {
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