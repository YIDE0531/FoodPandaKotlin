package com.nuu.foodpanda

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.nuu.foodpanda.databinding.ActivityWelcomeBinding
import com.nuu.foodpanda.util.SystemUtility

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showAnimate()
    }

    private fun showAnimate(){
        val mSet = AnimatorSet()
        val anim = ObjectAnimator.ofFloat(binding.imvIcon, "rotation", 0f, -45f, 0f)
        mSet.playTogether(anim)
        mSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val anim2 = ObjectAnimator.ofFloat(binding.imvIcon, "rotation", 0f, 45f, 0f)
                anim2.duration = 1000
                anim2.start()
            }
        })
        mSet.duration = 1000
        mSet.start()
        countDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            override fun onTick(millisUntilFinished: Long) {}
        }.start()
    }

    override fun onStop() {
        super.onStop()
        countDownTimer?.let {   //null判斷
            countDownTimer?.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        countDownTimer?.let {
            countDownTimer?.start()
        }
    }
}