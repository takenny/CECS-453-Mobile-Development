package com.example.codequiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean)