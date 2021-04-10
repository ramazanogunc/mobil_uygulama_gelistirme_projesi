package com.cbu.mobil_dersi_projesi.data.repository

import com.cbu.mobil_dersi_projesi.data.local.MekanDao
import com.cbu.mobil_dersi_projesi.data.model.Mekan

class MekanRepository(private val mekanDao: MekanDao) {

    suspend fun insert(mekan: Mekan) = mekanDao.insert(mekan)

    suspend fun update(mekan: Mekan) = mekanDao.update(mekan)

    fun get(mekanId: Int) = mekanDao.get(mekanId)

    fun getAllByUserId(userId: Int) = mekanDao.getAllByUserId(userId)

    suspend fun delete(mekanId: Int) = mekanDao.delete(mekanId)

    fun getAll() = mekanDao.getAll()
}