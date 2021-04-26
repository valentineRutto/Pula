package com.example.pula.di.module

import com.example.pula.data.SurveyRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        SurveyRepository(get())
    }
}