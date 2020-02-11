package com.example.lifecycle


sealed class LifecycleState
object Unknown : LifecycleState()
object Init : LifecycleState()
object Deinit : LifecycleState()
object Visible : LifecycleState()
object Invisible : LifecycleState()