package com.example.sleephony.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WearMessageViewModel @Inject constructor() : ViewModel(){
    private var _alarmType = MutableLiveData("")
    val alarmType : LiveData<String> = _alarmType

    private var _wakeUpTime = MutableLiveData("")
    val wakeUpTime : LiveData<String> = _wakeUpTime

    private var _bedTime = MutableLiveData("")
    val bedTime : LiveData<String> = _bedTime

    fun update(alarmType:String, wakeUpTime:String, bedTime:String) {
        _alarmType.value = alarmType
        _wakeUpTime.value = wakeUpTime
        _bedTime.value = bedTime
    }

}