package com.example.hciAssignment.ui.screens.info.model

import com.example.hciAssignment.ui.screens.mvi.BaseEffect
import com.example.hciAssignment.ui.screens.mvi.BaseEvent
import com.example.hciAssignment.ui.screens.mvi.BaseState

sealed interface InfoContract {

    sealed class Event : BaseEvent {
        data object Init: Event()
        data object GoBack : Event()
    }

    data object State : BaseState

    sealed class Effect : BaseEffect {
        data object GoBack : Effect()
    }
}
