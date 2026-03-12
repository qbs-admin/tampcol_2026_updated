package com.qbrains.tampcolapp.data.interfaces

import org.json.JSONObject

interface ProductionDataCallback {
    fun onDataReceived(productionData: JSONObject?)
}
