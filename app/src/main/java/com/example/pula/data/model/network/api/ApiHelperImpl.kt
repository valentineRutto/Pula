package com.example.pula.data.model.network.api


class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun getSurvey() = apiService.getAllSurvey()


}
