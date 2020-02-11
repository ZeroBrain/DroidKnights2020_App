package com.example.lifecycle


interface LifecycleObserver {
    fun onInit() = Unit
    fun onVisible() = Unit
    fun onInvisible() = Unit
    fun onDeinit() = Unit
}