package com.example.observablescomparison

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val _liveData = MutableLiveData("Hello world")
    val liveData: LiveData<String> = _liveData

    //for one time event
    //hot flow
    //state flow holds value
    //Смотрите на StateFlow как на изменяемую переменную, на изменения которой можно подписаться.
    //Его последнее значение всегда доступно, и, фактически, последнее значение —
    //единственное, что важно для observers.
    //after rotation will emit flow
    private val _stateFlow = MutableStateFlow("Hello world")
    val stateFlow: StateFlow<String> = _stateFlow

    //also hot flow
    //also for one-time event, but if will not be emitted after activity recreation
    //SharedFlow — это легковесный широковещательный event bus
    //Он имеет параметры для настройки, такие как количество старых событий, которые нужно сохранить
    //и воспроизвести для новых подписчиков
    //after rotation will not emit flow
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow: SharedFlow<String> = _sharedFlow.asSharedFlow()

    fun triggerLiveData() {
        _liveData.value = "Live data"
    }

    fun triggerStateFlow() {
        _stateFlow.value = "State flow ${Random.nextInt(100)}"
    }

    //cold (need collector)
    //flow does not hold value, it only do smth and emit
    //flow will not save value after recreation
    fun triggerFlow(): Flow<String> {
        return flow {
            repeat(5) {
                emit("item $it")
                delay(3000L)
            }
        }
    }

    fun triggerSharedFlow() {
        viewModelScope.launch {
            _sharedFlow.emit("Shared flow")
        }
    }
}