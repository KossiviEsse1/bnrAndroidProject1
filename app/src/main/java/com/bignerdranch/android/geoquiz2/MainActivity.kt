package com.bignerdranch.android.geoquiz2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bignerdranch.android.geoquiz2.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val questionBank = listOf<Question>(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private val answerCheck = mutableListOf<Boolean>(false, false, false, false, false, false)
    private val answers = mutableListOf<Boolean>(false, false, false, false, false, false)

    private var currentIndex = 0
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener { view: View ->
            if (!answerCheck[currentIndex]) {
                checkAnswer(true)
                answerCheck[currentIndex] = true;
                answers[currentIndex] = true;
                count++
            }
            if (count == questionBank.size) {
                gradeTest(questionBank, answers)
            }
        }
        binding.falseButton.setOnClickListener { view: View ->
            if (!answerCheck[currentIndex]) {
                checkAnswer(false)
                answerCheck[currentIndex] = true;
                count++
            }
            if (count == questionBank.size) {
                gradeTest(questionBank, answers)
            }
        }

        updateQuestion()


        binding.nextButton.setOnClickListener { view: View ->
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        binding.prevButton.setOnClickListener { view: View ->
            if (currentIndex - 1 >= 0) {
                currentIndex -= 1
            } else {
                currentIndex = questionBank.size - 1
            }
            updateQuestion()
        }

        binding.questionTextView.setOnClickListener { view: View ->
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
    }

    private fun gradeTest(questions: List<Question>, answers: List<Boolean>) {
        var correctAnswers = 0;
        questions.forEachIndexed{index, question ->
            if (question.answer == answers[index]) {
                correctAnswers++
            }
        }
        val percentage: Double = (correctAnswers.toDouble()/questions.size.toDouble()) * 100.0
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
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (userAnswer == questionBank[currentIndex].answer) {
            Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
        }
    }
}