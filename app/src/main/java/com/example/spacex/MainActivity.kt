package com.example.spacex

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spacex.ui.main.launch.view.LaunchListFragment
import com.example.spacex.ui.main.launch.view.LaunchModel
import com.example.spacex.ui.main.launch.view.LaunchDetailsFragment

class MainActivity : AppCompatActivity(), LaunchNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, LaunchListFragment.newInstance())
                .commitNow()
    }

    override fun onLaunchSelected(launchModel: LaunchModel) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, LaunchDetailsFragment.newInstance(
                launchModel.launch.id,
                launchModel.launch.rocket,
                launchModel.launch.details,
                launchModel.launch.payloads.toTypedArray(),
                launchModel.launch.links.youtube_id,
                launchModel.launch.links.wikipedia,
                launchModel.isFavorite
            ))
            .addToBackStack(null)
            .commit()
    }

    override fun onOpenLaunchWikiLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}

interface LaunchNavigation{
    fun onLaunchSelected(launchModel: LaunchModel)
    fun onOpenLaunchWikiLink(url: String)
}
