package com.example.sessionapp.data.repository

import com.example.sessionapp.data.AppDatabase
import com.example.sessionapp.data.model.Session

class SessionRepository(private val db: AppDatabase) {

    suspend fun saveSession(session: Session) {
        db.sessionDao().insert(session)
    }

    suspend fun getSession(sessionId: String): Session? {
        return db.sessionDao().getSessionById(sessionId)
    }
}
