package com.qbrains.tampcolapp.ui.fragments.drawerFragment

import android.content.Context
import androidx.lifecycle.ViewModel
import com.qbrains.tampcolapp.data.network.api.reponse.ErrorResMessage
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import com.qbrains.tampcolapp.data.repositories.IBlockBusterRepository
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import org.jetbrains.annotations.NotNull

class BlockBusterViewModel(
    private val context: Context,
    @NotNull private val repository: IBlockBusterRepository
) : ViewModel() {

    fun addCart(
        addCartRequest: AddCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    ) {
        repository.addToCart(addCartRequest, onSuccess, onError)
    }

}