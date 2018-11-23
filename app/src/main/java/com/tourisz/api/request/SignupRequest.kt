package com.tourisz.api.request

import com.tourisz.Application
import com.tourisz.util.Constants
import com.tourisz.util.helper.AndroidHelper

class SignupRequest {

    var userName: String? = null
    var email: String? = null
    var password: String? = null
    var agencyName: String? = null
    var contactNumber: String? = null
    var userType: Int? = null
    var deviceToken: String = AndroidHelper.getSharedPreferenceString(Application.getInstance(), Constants.FCM_TOKEN)
    var deviceType: Int = 0
    var isTermChecked: Boolean = false

    constructor() {}

    constructor(userName: String?, email: String?, password: String?, agencyName: String?, phone: String?,userType: Int?, deviceToken: String, deviceType: Int, isTermChecked: Boolean) {
        this.userName = userName
        this.email = email
        this.password = password
        this.agencyName = agencyName
        this.contactNumber=phone
        this.deviceToken = deviceToken
        this.deviceType = deviceType
        this.userType = userType
        this.isTermChecked = isTermChecked
    }


}
