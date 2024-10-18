package com.bignerdranch.android.geoquiz2

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val ANSWER_CHECK_STATE = "ANSWER_CHECK_STATE"
const val ANSWERS = "ANSWERS"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val questionBank = listOf<Question>(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private var answerCheck: MutableList<Boolean>
        get() = savedStateHandle[ANSWER_CHECK_STATE] ?: mutableListOf<Boolean>(false, false, false, false, false, false)
        set(value) = savedStateHandle.set(ANSWER_CHECK_STATE, value)

    private var answers: MutableList<Boolean>
        get() = savedStateHandle[ANSWERS] ?: mutableListOf<Boolean>(false, false, false, false, false, false)
        set(value) = savedStateHandle.set(ANSWERS, value)

    private var currentIndex: Int
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    private var count = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun isAnswered(): Boolean {
        return answerCheck[currentIndex]
    }

    fun answer() {
        answerCheck[currentIndex] = true;
    }

    fun updateAnswer(value: Boolean) {
        answers[currentIndex] = value
    }

    fun testCompleted(): Boolean {
        return count == questionBank.size
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        if (currentIndex - 1 >= 0) {
            currentIndex -= 1
        } else {
            currentIndex = questionBank.size - 1
        }
    }

    fun incrementCount() {
        count++
    }

    fun getTestPercentage(): Double {
        var correctAnswers = 0;
        questionBank.forEachIndexed { index, question ->
            if (question.answer == answers[index]) {
                correctAnswers++
            }
        }
        val percentage: Double = (correctAnswers.toDouble()/questionBank.size.toDouble()) * 100.0
        return percentage
    }
}