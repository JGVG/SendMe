package com.j_gaby_1997.sendme.fragments.log_in

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.j_gaby_1997.sendme.data.repository.Repository

class LogInViewModelFactory(
        private val repository: Repository,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
    ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(key: String,
                                        modelClass: Class<T>,
                                        handle: SavedStateHandle
    ): T {
        return LogInViewModel(repository, handle) as T
    }

}