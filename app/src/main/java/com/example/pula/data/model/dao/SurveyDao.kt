package com.example.pula.data.model.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.pula.data.model.Survey

@Dao
interface SurveyDao: BaseDao<Survey>
{
    @Query("SELECT * FROM Survey")
    suspend fun getSurveyQuestions()
}