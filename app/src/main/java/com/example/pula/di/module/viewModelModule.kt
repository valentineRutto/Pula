package com.example.pula.di.module

import com.example.pula.viewmodel.SurveyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SurveyViewModel(get())
    }
}
