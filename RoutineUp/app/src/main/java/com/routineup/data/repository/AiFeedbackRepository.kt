package com.routineup.data.repository

import com.routineup.data.model.AiFeedback
import com.routineup.data.model.WeeklyReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiFeedbackRepository @Inject constructor() {
    
    private val _currentFeedback = MutableStateFlow<AiFeedback?>(null)
    val currentFeedback: Flow<AiFeedback?> = _currentFeedback.asStateFlow()
    
    private val _weeklyReport = MutableStateFlow<WeeklyReport?>(null)
    val weeklyReport: Flow<WeeklyReport?> = _weeklyReport.asStateFlow()
    
    suspend fun generateDailyFeedback(completedRoutines: Int, totalRoutines: Int) {
        val completionRate = if (totalRoutines > 0) completedRoutines.toFloat() / totalRoutines else 0f
        
        val title = when {
            completionRate >= 0.9f -> "오늘 정말 잘 하셨어요!"
            completionRate >= 0.7f -> "오늘 루틴을 잘 지키셨네요"
            completionRate >= 0.5f -> "오늘 루틴 절반 이상 완료했어요"
            completionRate > 0f -> "오늘 일부 루틴을 완료했어요"
            else -> "내일은 더 잘할 수 있어요!"
        }
        
        val content = when {
            completionRate >= 0.9f -> "오늘 루틴을 거의 모두 완료하셨네요! 정말 대단해요. 이런 습관이 쌓이면 놀라운 변화가 찾아올 거예요."
            completionRate >= 0.7f -> "오늘 대부분의 루틴을 완료하셨어요. 꾸준히 하다 보면 어느새 습관이 되어 있을 거예요."
            completionRate >= 0.5f -> "오늘 루틴의 절반 이상을 완료했어요. 조금씩 꾸준히 하는 것이 중요해요!"
            completionRate > 0f -> "오늘 일부 루틴을 완료했어요. 내일은 더 많은 루틴을 완료할 수 있도록 응원할게요!"
            else -> "오늘은 루틴을 완료하지 못했지만, 내일은 더 잘할 수 있어요! 작은 것부터 시작해보는 건 어떨까요?"
        }
        
        val suggestion = when {
            completionRate >= 0.9f -> "이대로 계속 유지해보세요! 보상으로 자신에게 작은 선물을 해보는 건 어떨까요?"
            completionRate >= 0.7f -> "완료하지 못한 루틴이 있다면, 시간대를 조정해보는 건 어떨까요?"
            completionRate >= 0.5f -> "루틴을 더 쉽게 지키기 위해 알림 시간을 조정해보세요."
            completionRate > 0f -> "가장 중요한 루틴 하나에 집중해보는 건 어떨까요?"
            else -> "내일은 가장 쉬운 루틴 하나부터 시작해보세요!"
        }
        
        _currentFeedback.value = AiFeedback(
            title = title,
            content = content,
            suggestion = suggestion
        )
    }
    
    suspend fun generateWeeklyReport(
        weeklyCompletionRate: Float,
        previousWeekCompletionRate: Float,
        mostCompletedRoutine: String?,
        leastCompletedRoutine: String?
    ) {
        val today = LocalDate.now()
        val weekStart = today.minusDays(today.dayOfWeek.value - 1L)
        val weekEnd = weekStart.plusDays(6)
        
        val improvementRate = weeklyCompletionRate - previousWeekCompletionRate
        
        val report = WeeklyReport(
            weekStartDate = weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            weekEndDate = weekEnd.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            completionRate = weeklyCompletionRate,
            improvementRate = improvementRate,
            mostCompletedRoutine = mostCompletedRoutine,
            leastCompletedRoutine = leastCompletedRoutine
        )
        
        _weeklyReport.value = report
        
        // 주간 리포트를 포함한 피드백 생성
        val title = when {
            improvementRate > 0.1f -> "이번 주 정말 성장했어요!"
            improvementRate > 0f -> "이번 주 조금 더 발전했어요"
            improvementRate == 0f -> "이번 주 루틴 분석"
            else -> "다음 주는 더 잘할 수 있어요"
        }
        
        val content = buildString {
            append("이번 주 루틴 완료율은 ${(weeklyCompletionRate * 100).toInt()}%로 ")
            when {
                improvementRate > 0 -> append("지난주보다 ${(improvementRate * 100).toInt()}% 향상되었어요! ")
                improvementRate == 0f -> append("지난주와 동일해요. ")
                else -> append("지난주보다 ${(-improvementRate * 100).toInt()}% 감소했어요. ")
            }
            
            mostCompletedRoutine?.let {
                append("가장 잘 지킨 루틴은 '$it'이었어요. ")
            }
            
            leastCompletedRoutine?.let {
                append("'$it' 루틴은 조금 더 신경 써보면 좋을 것 같아요.")
            }
        }
        
        val suggestion = when {
            weeklyCompletionRate < 0.3f -> "다음 주에는 루틴 수를 줄이고 꼭 지킬 수 있는 것에 집중해보세요."
            weeklyCompletionRate < 0.5f -> "루틴을 지키기 어려운 시간대가 있다면, 시간을 조정해보는 건 어떨까요?"
            weeklyCompletionRate < 0.7f -> "루틴 알림을 활용하면 더 잘 지킬 수 있을 거예요."
            else -> "정말 잘하고 계세요! 이대로 계속 유지해보세요."
        }
        
        _currentFeedback.value = AiFeedback(
            title = title,
            content = content,
            suggestion = suggestion,
            weeklyReport = report
        )
    }
}
