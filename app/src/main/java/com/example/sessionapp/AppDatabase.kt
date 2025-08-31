package com.example.sessionapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sessionapp.data.dao.SessionDao
import com.example.sessionapp.data.model.Session

@Database(entities = [Session::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}
