package com.example.pula.data.model.network.api

import com.example.pula.data.model.SurveyResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/v1/interview")
    suspend fun getAllSurvey(): Response<SurveyResponse>

}

