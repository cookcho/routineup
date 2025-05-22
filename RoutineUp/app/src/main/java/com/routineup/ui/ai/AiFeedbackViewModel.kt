package com.routineup.ui.ai

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.routineup.data.model.AiFeedback
import com.routineup.data.model.WeeklyReport
import com.routineup.data.repository.AiFeedbackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiFeedbackViewModel @Inject constructor(
    private val aiFeedbackRepository: AiFeedbackRepository
) : ViewModel() {

    private val _feedback = MutableLiveData<AiFeedback?>()
    val feedback: LiveData<AiFeedback?> = _feedback
    
    private val _weeklyReport = MutableLiveData<WeeklyReport?>()
    val weeklyReport: LiveData<WeeklyReport?> = _weeklyReport
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    init {
        loadFeedback()
        loadWeeklyReport()
    }
    
    private fun loadFeedback() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                aiFeedbackRepository.currentFeedback.collect { feedbackData ->
                    _feedback.value = feedbackData
                }
            } catch (e: Exception) {
                _errorMessage.value = "AI 피드백을 불러오는 중 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadWeeklyReport() {
        viewModelScope.launch {
            try {
                aiFeedbackRepository.weeklyReport.collect { reportData ->
                    _weeklyReport.value = reportData
                }
            } catch (e: Exception) {
                _errorMessage.value = "주간 리포트를 불러오는 중 오류가 발생했습니다: ${e.message}"
            }
        }
    }
    
    fun generateDailyFeedback(completedRoutines: Int, totalRoutines: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                aiFeedbackRepository.generateDailyFeedback(completedRoutines, totalRoutines)
            } catch (e: Exception) {
                _errorMessage.value = "일일 피드백을 생성하는 중 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun generateWeeklyReport(
        weeklyCompletionRate: Float,
        previousWeekCompletionRate: Float,
        mostCompletedRoutine: String?,
        leastCompletedRoutine: String?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                aiFeedbackRepository.generateWeeklyReport(
                    weeklyCompletionRate,
                    previousWeekCompletionRate,
                    mostCompletedRoutine,
                    leastCompletedRoutine
                )
            } catch (e: Exception) {
                _errorMessage.value = "주간 리포트를 생성하는 중 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
