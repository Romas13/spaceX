package com.example.spacex.ui.main.launch.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.spacex.R
import com.example.spacex.data.model.launch.entities.Launch
import com.example.spacex.ui.main.base.BaseFragment
import com.example.spacex.ui.main.launch.adapter.LaunchesAdapter
import com.example.spacex.ui.main.launch.viewmodel.LaunchViewModel
import com.example.spacex.ui.main.launch.viewmodel.LoadingState
import java.io.Serializable
import java.util.*

class LaunchListFragment : BaseFragment(), LaunchListInteraction {

    override var layoutId: Int = R.layout.fragment_launch_list

    companion object {
        fun newInstance() =
            LaunchListFragment()
    }

    private lateinit var viewModel: LaunchViewModel

    private lateinit var loaderLayout: LinearLayout
    private lateinit var loaderMessageTextView: TextView
    private lateinit var loaderProgressBar: ProgressBar
    private lateinit var loaderRetryButton: Button

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: LaunchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = LaunchesAdapter(this)

        viewModel = ViewModelProvider(requireActivity()).get(LaunchViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loaderLayout = view.findViewById(R.id.loaderLayout)
        loaderProgressBar = loaderLayout.findViewById(R.id.loaderProgressBar)
        loaderMessageTextView = loaderLayout.findViewById(R.id.loaderMessageTextView)
        loaderRetryButton = loaderLayout.findViewById(R.id.loaderRetryButton)

        recycler = view.findViewById(R.id.recyclerView)
        recycler.adapter = adapter

        with(viewModel){
            launchesState.observe(viewLifecycleOwner){
                onLoadingStateChanged(it)
            }
            launches.observe(viewLifecycleOwner){
                onLaunchesChanged(it)
            }
        }

        if(savedInstanceState == null)
            viewModel.getLaunches()
    }

    private fun onLaunchesChanged(launches:List<LaunchModel>){
        adapter.differ.submitList(launches)
    }

    override fun onLaunchSelected(position: Int) {
        navigation?.onLaunchSelected(adapter.differ.currentList[position])
    }

    override fun onLaunchFavoriteSelected(position: Int) {
        viewModel.setLaunchFavorite(adapter.differ.currentList[position])
        adapter.notifyItemChanged(position)
    }

    private fun onLoadingStateChanged(loadingState: LoadingState) = when(loadingState) {
        LoadingState.Loading -> {
            loaderLayout.visibility = View.VISIBLE
            recycler.visibility = View.INVISIBLE
        }
        LoadingState.Loaded -> {
            loaderLayout.visibility = View.INVISIBLE
            recycler.visibility = View.VISIBLE
        }
        LoadingState.Error -> {
            loaderLayout.visibility = View.INVISIBLE
            recycler.visibility = View.INVISIBLE

            Toast.makeText(context, viewModel.launchesErrorMessage.value, Toast.LENGTH_LONG).show()
        }
    }
}

data class LaunchModel(val launch: Launch, var isFavorite:Boolean, val launchDate: Date) : Serializable

interface LaunchListInteraction{
    fun onLaunchSelected(position: Int)
    fun onLaunchFavoriteSelected(position: Int)
}
