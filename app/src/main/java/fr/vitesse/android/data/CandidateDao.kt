package fr.vitesse.android.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CandidateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(candidates: List<Candidate>)

    @Query("SELECT * FROM candidates")
    suspend fun getAll(): List<Candidate>

    @Query("SELECT * FROM candidates WHERE id = :candidateId")
    suspend fun getCandidateById(candidateId: Long): Candidate

    @Insert
    suspend fun insert(candidate: Candidate)

    @Delete
    suspend fun delete(candidate: Candidate)
}
