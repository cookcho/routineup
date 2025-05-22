package com.routineup.ui.routine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.routineup.data.model.Routine
import com.routineup.data.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _routines = MutableLiveData<List<Routine>>()
    val routines: LiveData<List<Routine>> = _routines
    
    private val _routine = MutableLiveData<Routine>()
    val routine: LiveData<Routine> = _routine
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    init {
        loadRoutines()
    }
    
    fun loadRoutines() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                routineRepository.getAllRoutines().collect { routineList ->
                    _routines.value = routineList
                }
            } catch (e: Exception) {
                _errorMessage.value = "루틴을 불러오는 중 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun getRoutineById(routineId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = routineRepository.getRoutineById(routineId)
                if (result != null) {
                    _routine.value = result
                } else {
                    _errorMessage.value = "해당 루틴을 찾을 수 없습니다."
                }
            } catch (e: Exception) {
                _errorMessage.value = "루틴을 불러오는 중 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun createRoutine(
        name: String,
        iconResId: Int,
        time: LocalTime,
        repeatDays: Set<DayOfWeek>,
        notificationMinutesBefore: Int = 0,
        memo: String = ""
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val routine = Routine(
                    name = name,
                    iconResId = iconResId,
                    time = time,
                    repeatDays = repeatDays,
                    notificationMinutesBefore = notificationMinutesBefore,
                    memo = memo
                )
                routineRepository.insertRoutine(routine)
                loadRoutines()
            } catch (e: Exception) {
                _errorMessage.value = "루틴을 생성하는 중 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateRoutine(routine: Routine) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                routineRepository.updateRoutine(routine)
                loadRoutines()
            } catch (e: Exception) {
                _errorMessage.value = "루틴을 수정하는 중 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                routineRepository.deleteRoutine(routine)
                loadRoutines()
            } catch (e: Exception) {
                _errorMessage.value = "루틴을 삭제하는 중 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateRoutineCompletionStatus(routineId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                routineRepository.updateRoutineCompletionStatus(routineId, isCompleted)
                loadRoutines()
            } catch (e: Exception) {
                _errorMessage.value = "루틴 완료 상태를 업데이트하는 중 오류가 발생했습니다: ${e.message}"
            }
        }
    }
}
