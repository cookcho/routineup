package com.routineup.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.routineup.databinding.ActivityOnboardingBinding
import com.routineup.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViewPager()
        setupButtons()
    }
    
    private fun setupViewPager() {
        onboardingAdapter = OnboardingAdapter(this)
        binding.viewpager.adapter = onboardingAdapter
        
        TabLayoutMediator(binding.tabIndicator, binding.viewpager) { _, _ -> }.attach()
        
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonsVisibility(position)
            }
        })
    }
    
    private fun setupButtons() {
        binding.tvSkip.setOnClickListener {
            navigateToMainActivity()
        }
        
        binding.btnNext.setOnClickListener {
            val currentPosition = binding.viewpager.currentItem
            if (currentPosition < onboardingAdapter.itemCount - 1) {
                binding.viewpager.currentItem = currentPosition + 1
            } else {
                navigateToMainActivity()
            }
        }
        
        binding.btnPrevious.setOnClickListener {
            val currentPosition = binding.viewpager.currentItem
            if (currentPosition > 0) {
                binding.viewpager.currentItem = currentPosition - 1
            }
        }
    }
    
    private fun updateButtonsVisibility(position: Int) {
        binding.btnPrevious.visibility = if (position > 0) View.VISIBLE else View.INVISIBLE
        
        if (position == onboardingAdapter.itemCount - 1) {
            binding.btnNext.text = "시작하기"
        } else {
            binding.btnNext.text = "다음"
        }
    }
    
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
