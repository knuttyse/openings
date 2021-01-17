package com.example.myapplication.boardscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BoardViewModel(initialState: AbstractBoardState) : ViewModel() {
    private val state = MutableLiveData(initialState)

    val liveData = state as LiveData<AbstractBoardState>

    fun transform(f: (AbstractBoardState) -> AbstractBoardState): AbstractBoardState {
        val newState = f(state.value!!)
        state.value = newState
        return newState
    }

}

class BoardViewModelFactory(private val initialState: AbstractBoardState) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BoardViewModel(initialState) as T
    }
}