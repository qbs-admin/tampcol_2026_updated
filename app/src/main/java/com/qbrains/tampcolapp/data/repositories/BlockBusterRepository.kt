package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.google.gson.Gson
import com.qbrains.tampcolapp.data.network.api.IBlockBusterAPI
import com.qbrains.tampcolapp.data.network.api.reponse.ErrorResMessage
import com.qbrains.tampcolapp.data.network.api.reponse.ResMessage
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import kotlinx.coroutines.*

class BlockBusterRepository(
    private val context: Context,
    private val iBlockBusterAPI: IBlockBusterAPI,
    private val iPref: IPreferenceManager
) : IBlockBusterRepository {

    private val mJob = SupervisorJob()
    private val mScope = CoroutineScope(Dispatchers.IO + mJob)

    override fun addToCart(
        addCartRequest: AddCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iBlockBusterAPI.addCartAsync(addCartRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {

//                        iPref.setIsLoggedIn(true)
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

}