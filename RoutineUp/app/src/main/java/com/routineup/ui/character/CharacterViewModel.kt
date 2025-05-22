package com.routineup.ui.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.routineup.data.model.Character
import com.routineup.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _character = MutableLiveData<Character>()
    val character: LiveData<Character> = _character
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    init {
        loadCharacter()
    }
    
    private fun loadCharacter() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                characterRepository.character.collect { characterData ->
                    _character.value = characterData
                }
            } catch (e: Exception) {
                _errorMessage.value = "캐릭터 정보를 불러오는 중 오류가 발생했습니다: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun addExperience(exp: Int) {
        viewModelScope.launch {
            try {
                characterRepository.addExperience(exp)
            } catch (e: Exception) {
                _errorMessage.value = "경험치를 추가하는 중 오류가 발생했습니다: ${e.message}"
            }
        }
    }
    
    fun incrementCompletedRoutines() {
        viewModelScope.launch {
            try {
                characterRepository.incrementCompletedRoutines()
            } catch (e: Exception) {
                _errorMessage.value = "완료된 루틴 수를 업데이트하는 중 오류가 발생했습니다: ${e.message}"
            }
        }
    }
    
    fun incrementConsecutiveDays() {
        viewModelScope.launch {
            try {
                characterRepository.incrementConsecutiveDays()
            } catch (e: Exception) {
                _errorMessage.value = "연속 일수를 업데이트하는 중 오류가 발생했습니다: ${e.message}"
            }
        }
    }
    
    fun resetConsecutiveDays() {
        viewModelScope.launch {
            try {
                characterRepository.resetConsecutiveDays()
            } catch (e: Exception) {
                _errorMessage.value = "연속 일수를 초기화하는 중 오류가 발생했습니다: ${e.message}"
            }
        }
    }
}
