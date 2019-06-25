package com.example.nhtu.demochat.Model

import com.google.gson.annotations.SerializedName

public class SocketExpire {
    constructor(from: String, to: String){
        From=from
        To=to
    }
    constructor()

    @SerializedName("from")
    var From:String=""
    @SerializedName("to")
    var To:String=""
}