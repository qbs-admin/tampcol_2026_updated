package com.qbrains.tampcolapp.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qbrains.tampcolapp.data.network.api.IAccountApi
import com.qbrains.tampcolapp.data.network.api.reponse.ContactusResponse
import com.qbrains.tampcolapp.data.network.manager.NetworkingManager
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.repositories.IAccountRepository
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import org.jetbrains.annotations.NotNull

class ContactusViewModel(
    private val context: Context,
    @NotNull private val repository: IAccountRepository
) : ViewModel() { // TODO: Implement the ViewModel

    fun getContactDetails(
        onSuccess: OnSuccess<ContactusResponse>,
        onError: OnError<Any>
    ) {
        repository.getContactDetails( onSuccess, onError)
    }

    class Factory(
        private val context: Context
    ) : ViewModelProvider.NewInstanceFactory() {


        private val repository = IAccountRepository.get(
            context,
//            database,
            NetworkingManager.createApi<IAccountApi>(context),
            PreferenceManager(context)
        )

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return ContactusViewModel(context, repository) as T
        }
    }
}