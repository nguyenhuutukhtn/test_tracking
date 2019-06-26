package com.example.nhtu.demochat.Model

import com.google.gson.annotations.SerializedName

class GetSessionInput{
    @SerializedName("contract_name")
    var contractName:String = ""
    @SerializedName("api_key")
    var apiKey:String=""
    @SerializedName("socket_expire")
    var socketExpire=SocketExpire()
    @SerializedName("tracking")
    var tracking=ArrayList<String>()
}