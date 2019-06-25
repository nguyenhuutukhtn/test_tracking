package com.example.nhtu.demochat.Model

import com.google.gson.annotations.SerializedName

class GetSessionOutput {
    @SerializedName("ErrorCode")
    var ErrorCode: Int?=null
    @SerializedName("Message")
    var Message: String=""
    @SerializedName("Result")
    var result: Result?=null

    inner class Result {
        @SerializedName("sessionID")
        var sessionID: String=""
    }
}
