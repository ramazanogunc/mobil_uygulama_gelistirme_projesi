package com.cbu.mobil_dersi_projesi.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cbu.mobil_dersi_projesi.model.Mekan

@Dao
interface MekanDao {

    @Insert
    suspend fun insert(mekan: Mekan)

    @Update
    suspend fun update(mekan: Mekan)

    @Query("SELECT * FROM mekan WHERE mekanId=:mekanId")
    fun get(mekanId: Int): LiveData<Mekan>

    @Query("SELECT * FROM mekan WHERE userId=:userId")
    fun getAllByUserId(userId: Int): LiveData<List<Mekan>>

    @Query("DELETE FROM mekan WHERE mekanId=:mekanId")
    suspend fun delete(mekanId: Int)

    @Query("SELECT * FROM mekan")
    fun getAll(): LiveData<List<Mekan>>
}