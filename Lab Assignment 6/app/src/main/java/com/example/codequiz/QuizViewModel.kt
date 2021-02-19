package com.example.codequiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

    class QuizViewModel : ViewModel() {

     var currentIndex = 0
        var isCheater = false
        private val questionBank = listOf(
            Question(R.string.question_increment, true),
            //YOU CODE to create four more questions
            Question(R.string.question_increment2, true),
            Question(R.string.question_boolean, true),
            Question(R.string.question_double, false),
            Question(R.string.question_mod, true)
        )

        val currentQuestionAnswer: Boolean
            get() = questionBank[currentIndex].answer

        val currentQuestionText: Int
            get() = questionBank[currentIndex].textResId

        fun moveToNext() {
            currentIndex = (currentIndex + 1) % questionBank.size
        }
    }
