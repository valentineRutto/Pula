package com.example.pula.data.model

import androidx.room.Entity

@Entity
data class Survey (
    val answer: String,val QuestionId: Int
        )