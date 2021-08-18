package com.day.record.ui.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.day.record.databinding.ActivitySplashBinding
import com.day.record.ui.MainActivity
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

        binding.logoIv.apply {
            scaleX = 0F
            scaleY = 0F
            alpha = 0F
            animate().scaleX(1F)
                .scaleY(1F)
                .alpha(1F)
                .setDuration(1500)
                .setInterpolator(BounceInterpolator())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        gotoMainActivity()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        gotoMainActivity()
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
                .start()
        }

        binding.nameTv.apply {
            scaleX = 0F
            scaleY = 0F
            animate().scaleX(1F)
                .scaleY(1F)
                .setDuration(1200)
                .setInterpolator(AccelerateInterpolator())
                .start()
        }

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