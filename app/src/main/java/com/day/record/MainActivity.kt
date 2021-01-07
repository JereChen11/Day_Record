package com.day.record

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.day.record.ui.calendar.CalendarActivity
import com.day.record.ui.day.DayTaskFragment
import com.day.record.ui.year.YearTaskFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

/**
 * @author Jere
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.vector_drawable_menu_on)
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isOpen) {
                drawerLayout.close()
            } else {
                drawerLayout.open()
            }
        }

        replaceFragment(DayTaskFragment(), false)
        bottomNavView.selectedItemId = R.id.bottomNavDayTask
        bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottomNavDayTask -> {
                    toolbar.title = getString(R.string.day_task)
                    replaceFragment(DayTaskFragment(), true)
                    true
                }
                R.id.bottomNavYearTask -> {
                    toolbar.title = getString(R.string.year_task)
                    replaceFragment(YearTaskFragment(), true)
                    true
                }
                else -> false
            }
        }

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    toolbar.title = "nav_home"
                }
                R.id.navLanguage -> {
                    toolbar.title = "navLanguage"
                }
                R.id.navSettings -> {
                    toolbar.title = "navSettings"
                }
            }
            drawerLayout.close()
            false
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

    private fun replaceFragment(newFragment: Fragment, isNeedAddToBackStack: Boolean) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.navHostFragment, newFragment)
        if (isNeedAddToBackStack) {
            ft.addToBackStack(null)
        }
        ft.commit()
    }
}