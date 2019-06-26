package com.example.nhtu.demochat.Model

import com.google.gson.annotations.SerializedName

class TrackingOutPut {
    @SerializedName("DeviceIMEI")
    var DeviceIMEI: String=""
    @SerializedName("location")
    var location: String=""
    @SerializedName("UserTracking")
    var UserTracking: String=""
    @SerializedName("CodeEmployee")
    var CodeEmployee: String=""
}
