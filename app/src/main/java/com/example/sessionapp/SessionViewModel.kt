package com.example.sessionapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sessionapp.data.model.Session
import com.example.sessionapp.data.repository.SessionRepository
import kotlinx.coroutines.launch

class SessionViewModel(private val repository: SessionRepository) : ViewModel() {

    fun saveSession(session: Session) {
        viewModelScope.launch {
            repository.saveSession(session)
        }
    }



    fun getSession(sessionId: String) {
        viewModelScope.launch {
            val session = repository.getSession(sessionId)
            // Use this session data for UI
        }
    }
}

