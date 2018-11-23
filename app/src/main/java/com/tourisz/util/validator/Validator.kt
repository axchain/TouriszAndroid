package com.tourisz.util.validator

import android.util.Patterns
import android.text.TextUtils


class Validator {

    init {
    }

    companion object {
        @JvmStatic
        fun newInstance() = Validator()
    }


    fun isValidEmail(email: String?): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidUname(uname: String?): Boolean {
        return !TextUtils.isEmpty(uname) && uname?.length!! > 3

    }

    fun isPasswordValid(password: String?): Boolean {
        return password?.length!! >= 6
    }

    fun isPhoneNoValid(phone: String?): Boolean {
        if (phone?.length == 12){
            return false
        }
        if (phone?.length == 13){
            return false
        }
        if (phone?.length == 14){
            return false
        }


        return true
    }

    fun isOTPValid(otp: String?): Boolean {
        return otp?.length == 4
    }

}