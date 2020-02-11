package com.example.lifecycle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal fun initToDeinit(
    coroutineContext: CoroutineContext,
    body: suspend () -> Unit
): LifecycleObserver = InitFlowLifecycle(coroutineContext).apply { init(body) }

internal fun visibleToInvisible(
    coroutineContext: CoroutineContext,
    body: suspend () -> Unit
): LifecycleObserver = VisibleFlowLifecycle(coroutineContext).apply { visible(body) }

internal class InitFlowLifecycle(coroutineContext: CoroutineContext) : LifecycleObserver {

    private val scope = CoroutineScope(coroutineContext + Job())

    private var init: (suspend () -> Unit)? = null

    fun init(body: suspend () -> Unit) {
        this.init = body
    }

    override fun onInit() {
        init?.let { _init ->
            scope.launch { _init.invoke() }
        }
    }

    override fun onDeinit() {
        if (scope.isActive) {
            scope.cancel()
        }
    }
}

internal class VisibleFlowLifecycle(coroutineContext: CoroutineContext) : LifecycleObserver {
    private val scope = CoroutineScope(coroutineContext + Job())
    private var visible: (suspend () -> Unit)? = null

    fun visible(body: suspend () -> Unit) {
        this.visible = body
    }

    override fun onVisible() {
        visible?.let { _visible ->
            scope.launch { _visible.invoke() }
        }
    }

    override fun onInvisible() {
        if (scope.isActive) {
            scope.cancel()
        }
    }
}