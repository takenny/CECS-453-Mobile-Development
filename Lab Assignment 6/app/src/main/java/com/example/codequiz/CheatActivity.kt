package com.example.codequiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

private const val EXTRA_ANSWER_IS_TRUE =
    "com.example.codequiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.codequiz.answer_shown"

class CheatActivity : AppCompatActivity() {

    private lateinit var KTanswerTextView: TextView
    private lateinit var KTshowAnswerButton: Button

    private var KTanswerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        KTanswerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        KTanswerTextView = findViewById(R.id.answer_text_view)
        KTshowAnswerButton = findViewById(R.id.show_answer_button)
        KTshowAnswerButton.setOnClickListener {
            val answerText = when {
                KTanswerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            KTanswerTextView.setText(answerText)
            setAnswerShownResult(true)
        }

    }
    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }

}