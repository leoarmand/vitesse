package fr.vitesse.android.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(candidates: List<Candidate>)

    @Query("SELECT * FROM candidates")
    fun getAll(): Flow<List<Candidate>>

    @Query("SELECT * FROM candidates WHERE id = :candidateId")
    suspend fun getCandidateById(candidateId: Long): Candidate

    @Insert
    suspend fun insert(candidate: Candidate)

    @Upsert
    suspend fun upsert(candidate: Candidate)

    @Delete
    suspend fun delete(candidate: Candidate)
}
