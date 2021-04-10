package com.cbu.mobil_dersi_projesi.data.repository

import com.cbu.mobil_dersi_projesi.data.local.UserDao
import com.cbu.mobil_dersi_projesi.data.model.User

class UserRepository(private val userDao: UserDao) {

    suspend fun login(email: String, password: String) = userDao.login(email, password)

    suspend fun register(users: User) = userDao.register(users)

    suspend fun delete(userId: Int) = userDao.delete(userId)

    suspend fun update(user: User) = userDao.update(user)

    suspend fun getByUserId(userId: Int) = userDao.getByUserId(userId)
}