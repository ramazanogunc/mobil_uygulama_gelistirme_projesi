package com.cbu.mobil_dersi_projesi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cbu.mobil_dersi_projesi.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE email=:email AND password=:password")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM user WHERE userId=:userId")
    suspend fun getByUserId(userId: Int): User

    @Insert
    suspend fun register(user: User)

    @Update
    suspend fun update(user: User)

    @Query("DELETE FROM user WHERE userId=:userId")
    suspend fun delete(userId: Int)
}