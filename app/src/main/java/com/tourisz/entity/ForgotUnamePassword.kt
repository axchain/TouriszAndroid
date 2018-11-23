package com.tourisz.entity

class ForgotUnamePassword {
    var isForgotPass: Boolean = false
    var emailOrMob: String? = null
    var isSendOnPhone: Boolean = false

    constructor() {}
    constructor(isForgotPass: Boolean, phoneEmail: String?, isResetByPhone: Boolean) {
        this.isForgotPass = isForgotPass
        this.emailOrMob = phoneEmail
        this.isSendOnPhone = isResetByPhone
    }


}
