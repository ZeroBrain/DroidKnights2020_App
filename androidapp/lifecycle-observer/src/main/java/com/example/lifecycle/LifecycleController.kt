package com.example.lifecycle

import kotlin.coroutines.CoroutineContext


class LifecycleController(private val coroutineContext: CoroutineContext) : LifecycleDelegate {
    private val observers = mutableListOf<LifecycleObserver>()
    private var currentState: LifecycleState = Unknown

    override fun init(body: suspend () -> Unit) {
        val initToDeinit = initToDeinit(coroutineContext, body)
        observers += initToDeinit
        when (currentState) {
            Init -> initToDeinit.onInit()
            Deinit -> initToDeinit.onDeinit()
        }
    }

    override fun visible(body: suspend () -> Unit) {
        val visibleToInvisible = visibleToInvisible(coroutineContext, body)
        observers += visibleToInvisible
        when (currentState) {
            Visible -> visibleToInvisible.onVisible()
            Invisible -> visibleToInvisible.onInvisible()
        }
    }

    fun onInit() {
        currentState = Init
        observers.forEach { it.onInit() }
    }

    fun onVisible() {
        currentState = Visible
        observers.forEach { it.onVisible() }
    }

    fun onInvisible() {
        currentState = Invisible
        observers.forEach { it.onInvisible() }
    }

    fun onDeinit() {
        currentState = Deinit
        observers.forEach { it.onDeinit() }
    }
}

interface LifecycleDelegate {
    fun init(body: suspend () -> Unit)
    fun visible(body: suspend () -> Unit)
}