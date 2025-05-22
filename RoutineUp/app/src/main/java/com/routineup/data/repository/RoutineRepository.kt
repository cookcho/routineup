package com.routineup.data.repository

import com.routineup.data.local.RoutineDao
import com.routineup.data.model.Routine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoutineRepository @Inject constructor(
    private val routineDao: RoutineDao
) {
    fun getAllRoutines(): Flow<List<Routine>> {
        return routineDao.getAllRoutines()
    }
    
    suspend fun getRoutineById(routineId: Long): Routine? {
        return routineDao.getRoutineById(routineId)
    }
    
    fun getRoutinesByTimeRange(startTime: Long, endTime: Long): Flow<List<Routine>> {
        return routineDao.getRoutinesByTimeRange(startTime, endTime)
    }
    
    suspend fun insertRoutine(routine: Routine): Long {
        return routineDao.insertRoutine(routine)
    }
    
    suspend fun updateRoutine(routine: Routine) {
        routineDao.updateRoutine(routine)
    }
    
    suspend fun deleteRoutine(routine: Routine) {
        routineDao.deleteRoutine(routine)
    }
    
    suspend fun updateRoutineCompletionStatus(routineId: Long, isCompleted: Boolean) {
        routineDao.updateRoutineCompletionStatus(routineId, isCompleted)
        if (isCompleted) {
            routineDao.incrementCompletionCount(routineId)
        }
    }
}
