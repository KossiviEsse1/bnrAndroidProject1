package com.bignerdranch.android.geoquiz2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz2.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener { view: View ->
            processButtonClick(true)
        }
        binding.falseButton.setOnClickListener { view: View ->
            processButtonClick(false)
        }

        updateQuestion()

        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        binding.nextButton.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.prevButton.setOnClickListener { view: View ->
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        binding.questionTextView.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }
    }

    private fun processButtonClick(value: Boolean) {
        if (!quizViewModel.isAnswered()) {
            checkAnswer(value)
            quizViewModel.answer()
            quizViewModel.updateAnswer(value)
            quizViewModel.incrementCount()
        }
        if (quizViewModel.testCompleted()) {
            gradeTest()
        }
    }

    private fun gradeTest() {
        val percentage: Double = quizViewModel.getTestPercentage()
        Toast.makeText(this, "Percentage: ${Math.round(percentage)}%", Toast.LENGTH_LONG).show()
    }

    override fun onStart() { super.onStart()
        Log.d(TAG, "onStart() called") }
    override fun onResume() { super.onResume()
        Log.d(TAG, "onResume() called") }
    override fun onPause() { super.onPause()
        Log.d(TAG, "onPause() called") }
    override fun onStop() { super.onStop()
        Log.d(TAG, "onStop() called") }
    override fun onDestroy() { super.onDestroy()
        Log.d(TAG, "onDestroy() called") }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == quizViewModel.currentQuestionAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}