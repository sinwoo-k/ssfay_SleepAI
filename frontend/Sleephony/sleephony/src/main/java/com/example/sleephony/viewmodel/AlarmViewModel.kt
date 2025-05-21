package com.example.sleephony.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlarmViewModel:ViewModel() {
    private var _alarmType = MutableLiveData("")
    val alarmType : LiveData<String> get() = _alarmType

    private var _bedMeridiem = MutableLiveData("")
    val bedMeridiem: LiveData<String> get() = _bedMeridiem

    private  var _bedHour = MutableLiveData("")
    val bedHour: LiveData<String> get() = _bedHour

    private  var _bedMinute = MutableLiveData("")
    val bedMinute:LiveData<String> get() = _bedMinute

    fun bedUpDate(meridiem:String, hour:String, minute:String) {
        _bedMeridiem.value = meridiem
        _bedHour.value = hour
        _bedMinute.value = minute

    }

    fun alarmTypeUpdate(type: String) {
        _alarmType.value = type
    }


    private var _wakeUpMeridiem = MutableLiveData("")
    val wakeUpMeridiem: LiveData<String> get() = _wakeUpMeridiem

    private var _wakeUpHour = MutableLiveData("")
    val wakeUpHour: LiveData<String> get() = _wakeUpHour

    private var _wakeUpMinute = MutableLiveData("")
    val wakeUpMinute: LiveData<String> get() = _wakeUpMinute

    fun wakeUpUpdate(meridiem:String, hour:String, minute:String) {
        _wakeUpMeridiem.value = meridiem
        _wakeUpHour.value = hour
        _wakeUpMinute.value = minute
    }
}