package fr.vitesse.android.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {

    @Query("SELECT * FROM candidates")
    fun getAll(): Flow<List<Candidate>>

    @Query("SELECT * FROM candidates WHERE id = :candidateId")
    fun getCandidateFlowById(candidateId: Int): Flow<Candidate>

    @Upsert
    suspend fun upsert(candidate: Candidate)

    @Query("SELECT * FROM candidates WHERE id = :candidateId")
    suspend fun getCandidateById(candidateId: Int): Candidate

    @Delete
    suspend fun delete(candidate: Candidate)
}
