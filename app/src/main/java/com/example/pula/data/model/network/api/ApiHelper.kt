package com.example.pula.data.model.network.api

import com.example.pula.data.model.SurveyResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun getSurvey(): Response<SurveyResponse>
}