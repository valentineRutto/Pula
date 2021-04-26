package com.example.pula.data

import com.example.pula.data.model.SurveyResponse
import com.example.pula.data.model.network.NetworkResult
import com.example.pula.data.model.network.api.ApiHelper
import com.example.pula.data.model.network.safeApiCall
import kotlinx.coroutines.Dispatchers


class SurveyRepository(private val apiHelper: ApiHelper) {

    suspend fun getSurvey(): NetworkResult<SurveyResponse?> {
        return safeApiCall(Dispatchers.IO){
            apiHelper.getSurvey().body()
        }
    }
}