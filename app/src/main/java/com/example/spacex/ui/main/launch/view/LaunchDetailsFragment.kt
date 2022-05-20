package com.example.spacex.ui.main.launch.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.example.spacex.BuildConfig
import com.example.spacex.R
import com.example.spacex.data.model.payload.Payload
import com.example.spacex.data.model.rocket.Rocket
import com.example.spacex.databinding.FragmentLaunchDetailsBinding
import com.example.spacex.ui.main.base.BaseFragment
import com.example.spacex.ui.main.launch.viewmodel.LaunchViewModel
import com.example.spacex.ui.main.launch.viewmodel.LoadingState
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.youtube.player.YouTubeFragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

private const val ARG_LAUNCH = "ARG_LAUNCH"
private const val ARG_ROCKET = "ARG_ROCKET"
private const val ARG_DETAILS = "ARG_DETAILS"
private const val ARG_PAYLOADS = "ARG_PAYLOADS"
private const val ARG_YOUTUBE = "ARG_YOUTUBE"
private const val ARG_WIKI = "ARG_WIKI"
private const val ARG_FAVORITE = "ARG_FAVORITE"

class LaunchDetailsFragment : BaseFragment(),
    YouTubePlayer.OnInitializedListener {

    override var layoutId: Int = R.layout.fragment_launch_details

    companion object {
        fun newInstance(launchId: String, rocketId: String, details: String?, payloads: Array<String>,
                        youtube: String?, wiki: String?, isFavorite: Boolean) =
            LaunchDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LAUNCH, launchId)
                    putString(ARG_ROCKET, rocketId)
                    putString(ARG_DETAILS, details)
                    putStringArray(ARG_PAYLOADS, payloads)
                    putString(ARG_YOUTUBE, youtube)
                    putString(ARG_WIKI, wiki)
                    putBoolean(ARG_FAVORITE, isFavorite)
                }
            }
    }

    private lateinit var youtubeFragment: YouTubeFragment
    private lateinit var player: YouTubePlayer

    private lateinit var loaderLayout: LinearLayout
    private lateinit var loaderMessageTextView: TextView
    private lateinit var loaderProgressBar: ProgressBar
    private lateinit var loaderRetryButton: Button

    private lateinit var infoLayout: LinearLayout
    private lateinit var rocketName: TextView
    private lateinit var payloadMass: TextView
    private lateinit var launchDetails: TextView
    private lateinit var wikiLink: TextView

    private lateinit var favoriteFab: FloatingActionButton

    private lateinit var viewModel: LaunchViewModel

    private lateinit var launchId: String
    private lateinit var rocketId: String
    private lateinit var details: String
    private var payloads: Array<String>? = null
    private lateinit var youtube: String
    private lateinit var wiki: String
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            launchId = it.getString(ARG_LAUNCH) ?: ""
            rocketId = it.getString(ARG_ROCKET) ?: ""
            details = it.getString(ARG_DETAILS) ?: ""
            payloads = it.getStringArray(ARG_PAYLOADS)!!
            youtube = it.getString(ARG_YOUTUBE) ?: ""
            wiki = it.getString(ARG_WIKI) ?: ""
            isFavorite = it.getBoolean(ARG_FAVORITE)
        }

        viewModel = ViewModelProvider(requireActivity()).get(LaunchViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(youtube.isNotEmpty()) initYouTubePlayer()
        else view.findViewById<FragmentContainerView>(R.id.youTubePlayerFragment).visibility = View.GONE

        loaderLayout = view.findViewById(R.id.loaderLayout)
        loaderProgressBar = loaderLayout.findViewById(R.id.loaderProgressBar)
        loaderMessageTextView = loaderLayout.findViewById(R.id.loaderMessageTextView)
        loaderRetryButton = loaderLayout.findViewById(R.id.loaderRetryButton)

        infoLayout = view.findViewById(R.id.infoLayout)
        rocketName = view.findViewById(R.id.rocketNameTextView)
        payloadMass = view.findViewById(R.id.payloadMassTextView)
        launchDetails = view.findViewById(R.id.detailsTextView)
        wikiLink = view.findViewById(R.id.wikiLinkTextView)
        wikiLink.setOnClickListener{
            openLaunchWikiLink(wiki)
        }

        favoriteFab = view.findViewById(R.id.favoriteFab)
        favoriteFab.setOnClickListener {
            toggleFavorite()
        }
        setFabIconByLaunchFavorite(isFavorite)

        with(viewModel){
            rocketInfoState.observe(viewLifecycleOwner){
                onLoadingStateChanged(it)
            }
            rocketInfo.observe(viewLifecycleOwner){
                onRocketInfoChanged(it)
            }
            payloads.observe(viewLifecycleOwner){
                onPayloadsChanged(it)
            }
        }

        if(savedInstanceState == null){
            viewModel.getRocketInfo(rocketId, payloads)
        }
    }

    private fun initYouTubePlayer(){
        youtubeFragment = YouTubeFragment.newInstance()
        youtubeFragment.initialize(BuildConfig.YOUTUBE_API_KEY, this)

        childFragmentManager
            .beginTransaction()
            .replace(R.id.youTubePlayerFragment, youtubeFragment)
            .commit()
    }

    private fun toggleFavorite() {
        isFavorite = !isFavorite

        viewModel.setLaunchFavorite(launchId)

        setFabIconByLaunchFavorite(isFavorite)
    }

    private fun openLaunchWikiLink(wikipedia: String) {
        navigation?.onOpenLaunchWikiLink(wikipedia)
    }

    private fun setFabIconByLaunchFavorite(isFavorite:Boolean){
        favoriteFab.setImageResource(if (isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        this.player = player!!

        if(!wasRestored)
            player.cueVideo(youtube)
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        errorReason: YouTubeInitializationResult?
    ) {
        Toast.makeText(context, "Initialize failure - ${errorReason?.name}", Toast.LENGTH_LONG).show()
    }

    private fun onLoadingStateChanged(loadingState: LoadingState) = when(loadingState) {
        LoadingState.Loading -> {
            loaderLayout.visibility = View.VISIBLE
            infoLayout.visibility = View.INVISIBLE
        }
        LoadingState.Loaded -> {
            loaderLayout.visibility = View.INVISIBLE
            infoLayout.visibility = View.VISIBLE
        }
        LoadingState.Error -> {
            loaderLayout.visibility = View.INVISIBLE
            infoLayout.visibility = View.INVISIBLE
            Toast.makeText(context, viewModel.rocketInfoErrorMessage.value, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onRocketInfoChanged(rocket: Rocket) {
        rocketName.text = rocket.name
        launchDetails.text = details
        wikiLink.text = wiki
    }

    private fun onPayloadsChanged(payloads: List<Payload>) {
        payloadMass.text = getString(R.string.launch_payload_mass, payloads.sumOf { it.mass_kg })
    }
}