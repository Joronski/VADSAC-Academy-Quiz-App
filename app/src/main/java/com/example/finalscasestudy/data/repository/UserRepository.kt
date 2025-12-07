package com.example.finalscasestudy.data.repository

import com.example.finalscasestudy.data.local.dao.UserDao
import com.example.finalscasestudy.data.local.entities.User

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: User): Boolean {
        val existingUser = userDao.getUserByEmail(user.email)
        return if (existingUser == null) {
            userDao.insertUser(user)
            true
        } else {
            false
        }
    }

    suspend fun loginUser(email: String, password: String): User? {
        return userDao.getUserByEmailAndPassword(email, password)
    }
}
