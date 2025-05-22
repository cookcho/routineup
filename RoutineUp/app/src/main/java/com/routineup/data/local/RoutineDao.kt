package com.routineup.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.routineup.data.model.Routine
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines ORDER BY time ASC")
    fun getAllRoutines(): Flow<List<Routine>>
    
    @Query("SELECT * FROM routines WHERE id = :routineId")
    suspend fun getRoutineById(routineId: Long): Routine?
    
    @Query("SELECT * FROM routines WHERE time >= :startTime AND time <= :endTime ORDER BY time ASC")
    fun getRoutinesByTimeRange(startTime: Long, endTime: Long): Flow<List<Routine>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine): Long
    
    @Update
    suspend fun updateRoutine(routine: Routine)
    
    @Delete
    suspend fun deleteRoutine(routine: Routine)
    
    @Query("UPDATE routines SET isCompleted = :isCompleted WHERE id = :routineId")
    suspend fun updateRoutineCompletionStatus(routineId: Long, isCompleted: Boolean)
    
    @Query("UPDATE routines SET completionCount = completionCount + 1 WHERE id = :routineId")
    suspend fun incrementCompletionCount(routineId: Long)
}
