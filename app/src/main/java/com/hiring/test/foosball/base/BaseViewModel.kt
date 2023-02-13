package com.hiring.test.foosball.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    val loadingLiveData = MutableLiveData(false)

    protected open fun startLoading(longTermOperationStarts: Boolean = false) {
        loadingLiveData.postValue(true)
    }

    protected open fun stopLoading(longTermOperationStops: Boolean = false) {
        loadingLiveData.postValue(false)
    }
}
