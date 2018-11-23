package com.tourisz.api.request

import com.tourisz.Application
import com.tourisz.util.Constants
import com.tourisz.util.helper.AndroidHelper

class LoginRequest {

    var email: String? = null
    var password: String? = null
    var deviceToken: String = AndroidHelper.getSharedPreferenceString(Application.getInstance(), Constants.FCM_TOKEN)
    var deviceType: Int = 0

    constructor() {}
    constructor(email: String?, password: String?, deviceToken: String, deviceType: Int) {
        this.email = email
        this.password = password
        this.deviceToken = deviceToken
        this.deviceType = deviceType
    }


}
