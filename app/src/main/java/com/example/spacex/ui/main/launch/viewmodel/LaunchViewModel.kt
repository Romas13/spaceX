package com.example.spacex.ui.main.launch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacex.data.model.payload.Payload
import com.example.spacex.data.model.rocket.Rocket
import com.example.spacex.data.repository.Repositories
import com.example.spacex.ui.main.launch.view.LaunchModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

enum class LoadingState{
    Loading,
    Loaded,
    Error
}

class LaunchViewModel : ViewModel() {

    val launchesState = MutableLiveData<LoadingState>()
    val launches = MutableLiveData<List<LaunchModel>>()
    val launchesErrorMessage = MutableLiveData<String>()

    val rocketInfoState = MutableLiveData<LoadingState>()
    val rocketInfo = MutableLiveData<Rocket>()
    val payloads = MutableLiveData<List<Payload>>()
    val rocketInfoErrorMessage = MutableLiveData<String>()

    fun getLaunches(){
        if(launches.value != null) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                launchesState.postValue(LoadingState.Loading)

                val items = Repositories.spaceXApi.getLaunches()
                val favoriteLaunches = Repositories.launchesRepository.getFavoriteLaunches()

                launches.postValue(items.map { launch -> LaunchModel(
                    launch,
                    favoriteLaunches.find{ it.id == launch.id }?.isFavorite ?: false,
                    Date(launch.date_unix * 1000L)) })

                launchesState.postValue(LoadingState.Loaded)
            }
            catch (e:Exception){
                launchesErrorMessage.postValue(e.message)
                launchesState.postValue(LoadingState.Error)
            }
        }
    }

    fun getRocketInfo(rocketId:String, payloadIds: Array<String>?){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                rocketInfoState.postValue(LoadingState.Loading)

                val rocket = Repositories.spaceXApi.getRocketInfo(rocketId)

                val items = mutableListOf<Payload>()
                payloadIds?.forEach {
                    items.add(Repositories.spaceXApi.getPayload(it))
                }
                payloads.postValue(items)
                rocketInfo.postValue(rocket)

                rocketInfoState.postValue(LoadingState.Loaded)
            } catch (e: Exception) {
                rocketInfoErrorMessage.postValue(e.message)
                rocketInfoState.postValue(LoadingState.Error)
            }
        }
    }

    fun setLaunchFavorite(launchId:String) = launches.value?.find { it.launch.id == launchId }?.let { setLaunchFavorite(it) }

    fun setLaunchFavorite(launchModel: LaunchModel){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                launchModel.isFavorite = !launchModel.isFavorite

                Repositories.launchesRepository.setLaunchFavorite(launchModel)

                launches.postValue(launches.value)
            }
            catch (e: Exception){
                // Не пришел к единому мнению как лучше обработать исключение в данном месте
            }
        }
    }
}