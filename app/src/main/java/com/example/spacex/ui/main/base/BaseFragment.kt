package com.example.spacex.ui.main.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spacex.LaunchNavigation

abstract class BaseFragment : androidx.fragment.app.Fragment(){
    protected abstract var layoutId: Int

    override fun onAttach(context: Context) {
        super.onAttach(context)

        navigation = context as LaunchNavigation
    }

    override fun onDetach() {
        super.onDetach()

        navigation = null
    }

    protected var navigation: LaunchNavigation? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)


}