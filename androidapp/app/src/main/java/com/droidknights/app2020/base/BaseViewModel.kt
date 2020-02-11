package com.droidknights.app2020.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.example.lifecycle.LifecycleDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel(lifecycleDelegate: LifecycleDelegate) : ViewModel(),
    LifecycleDelegate by lifecycleDelegate {

    private val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}