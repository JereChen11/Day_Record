package com.day.record.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.day.record.MainActivity
import com.day.record.databinding.ActivitySplashBinding
import com.day.record.utils.SpUtils
import com.day.record.utils.Utils
import java.util.*

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        val currentLanguage = Locale.getDefault().language
        Log.e("jctest", "onCreate: currentLanguage = $currentLanguage")
        if (currentLanguage != SpUtils.getInstance().getLanguage()) {
            SpUtils.getInstance().setLanguage(currentLanguage)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            kotlin.run {
                gotoMainActivity()
            }
        }, 1000)

    }

    override fun onResume() {
        super.onResume()
        Utils.updateLocale(this, Locale(SpUtils.getInstance().getLanguage()))
    }

    private fun gotoMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}