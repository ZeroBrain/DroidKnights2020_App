package com.droidknights.app2020.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.droidknights.app2020.BR
import com.droidknights.app2020.ext.assistedViewModels
import com.example.lifecycle.LifecycleController
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<VM : ViewModel, B : ViewDataBinding>(
    @LayoutRes val layoutResId: Int,
    viewModelClass: KClass<VM>
) : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var dispatcherProvider: DefaultDispatcherProvider

    private val lifecycleController by lazy { LifecycleController(dispatcherProvider.default()) }
    protected val viewModel: VM by assistedViewModels(lifecycleController, viewModelClass) { viewModelFactory }

    protected val binding: B by lazy { DataBindingUtil.bind<B>(view!!)!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setVariable(BR.vm, viewModel)
            lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleController.onInit()
    }

    override fun onResume() {
        super.onResume()
        lifecycleController.onVisible()
    }


    override fun onPause() {
        lifecycleController.onInvisible()
        super.onPause()
    }

    override fun onDestroy() {
        lifecycleController.onDeinit()
        super.onDestroy()
    }
}