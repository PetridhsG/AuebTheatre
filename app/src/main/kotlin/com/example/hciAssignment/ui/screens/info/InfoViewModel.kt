package com.example.hciAssignment.ui.screens.info

import com.example.hciAssignment.ui.screens.info.model.InfoContract
import com.example.hciAssignment.ui.screens.mvi.BaseViewModel

class InfoViewModel:
    BaseViewModel<InfoContract.Event, InfoContract.State, InfoContract.Effect>(){

    override fun setInitialState(): InfoContract.State = InfoContract.State

    override fun handleEvents(event: InfoContract.Event) {
        when(event){
            is InfoContract.Event.Init -> {

            }

            InfoContract.Event.GoBack -> {
                setEffect {
                    InfoContract.Effect.GoBack
                }
            }
        }
    }
}
