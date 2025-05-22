package com.routineup.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.routineup.R
import com.routineup.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
        setupListeners()
    }
    
    private fun setupUI() {
        // 현재 날짜 표시
        val dateFormat = SimpleDateFormat("yyyy년 M월 d일 E요일", Locale.KOREA)
        binding.tvDate.text = dateFormat.format(Date())
        
        // AI 메시지 설정
        binding.tvAiMessage.text = "오늘도 루틴으로 성장하는 하루를 만들어봐요!"
        
        // 레벨 정보 설정
        binding.tvCurrentLevel.text = "현재 레벨: 3"
        binding.progressLevel.progress = 65
    }
    
    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_routine_creation)
        }
        
        binding.btnAddRoutine.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_routine_creation)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
