package com.huseyinuslu.unscrambleapponmyown.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huseyinuslu.unscrambleapponmyown.ui.main.data.AllOfWords

class MainViewModel : ViewModel() {

    private val _countOfWords : MutableLiveData<Int> = MutableLiveData(0)
    val countOfWords : LiveData<Int> = _countOfWords

    private val _score : MutableLiveData<Int> = MutableLiveData(0)
    val score : LiveData<Int> = _score

    private var currentWord : String = ""

    private var _currentUnscrambledWord : MutableLiveData<String> = MutableLiveData<String>("")
    val currentUnscrambledWord : LiveData<String> get() = _currentUnscrambledWord

    private val listOfWordsUsed : MutableList<String> = mutableListOf()

    private val wordList = AllOfWords.list

    fun getNewUnscrambledWord() {
       currentWord = wordList.random()

        val charEdWord = currentWord.toCharArray()
        charEdWord.shuffle()

        while(currentWord == String(charEdWord)){
            charEdWord.shuffle()
        }

        if(listOfWordsUsed.contains(currentWord)){
            getNewUnscrambledWord()
        }else{
            _currentUnscrambledWord.value = String(charEdWord)
            listOfWordsUsed.add(currentWord)
            _countOfWords.value = _countOfWords.value!!.inc()
        }
    }

    fun areThereAnyWordsLeft() : Boolean {
        return AllOfWords.MAX_OF_NO_WORDS > countOfWords.value!!
        }

    fun isAnswerCorrect(answer : String) : Boolean {
       return currentWord == answer
    }

    fun restartGame(){

    }

    }

