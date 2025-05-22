package com.routineup.data.repository

import com.routineup.data.model.Character
import com.routineup.data.model.CharacterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor() {
    
    private val _character = MutableStateFlow(Character())
    val character: Flow<Character> = _character.asStateFlow()
    
    suspend fun addExperience(exp: Int) {
        val currentCharacter = _character.value
        var newExp = currentCharacter.exp + exp
        var newLevel = currentCharacter.level
        var newMaxExp = currentCharacter.maxExp
        
        // 레벨업 처리
        while (newExp >= newMaxExp) {
            newExp -= newMaxExp
            newLevel++
            newMaxExp = calculateMaxExp(newLevel)
        }
        
        // 캐릭터 타입 업데이트
        val newCharacterType = determineCharacterType(newLevel, currentCharacter.totalCompletedRoutines)
        
        _character.value = currentCharacter.copy(
            level = newLevel,
            exp = newExp,
            maxExp = newMaxExp,
            characterType = newCharacterType
        )
    }
    
    suspend fun incrementCompletedRoutines() {
        val currentCharacter = _character.value
        val newTotal = currentCharacter.totalCompletedRoutines + 1
        
        _character.value = currentCharacter.copy(
            totalCompletedRoutines = newTotal,
            characterType = determineCharacterType(currentCharacter.level, newTotal)
        )
    }
    
    suspend fun incrementConsecutiveDays() {
        val currentCharacter = _character.value
        _character.value = currentCharacter.copy(
            consecutiveDays = currentCharacter.consecutiveDays + 1
        )
    }
    
    suspend fun resetConsecutiveDays() {
        val currentCharacter = _character.value
        _character.value = currentCharacter.copy(
            consecutiveDays = 0
        )
    }
    
    private fun calculateMaxExp(level: Int): Int {
        // 레벨이 올라갈수록 필요 경험치 증가
        return 100 + (level - 1) * 20
    }
    
    private fun determineCharacterType(level: Int, totalCompletedRoutines: Int): CharacterType {
        return when {
            level >= 8 -> CharacterType.SUPER_RICH
            level >= 6 -> CharacterType.NIGHT_OWL
            level >= 4 -> CharacterType.EARLY_BIRD
            level >= 2 -> CharacterType.SAVER
            else -> CharacterType.BEGINNER
        }
    }
}
