package com.tourisz.user.presenter.login

import com.tourisz.entity.ForgotUnamePassword
import com.tourisz.api.request.LoginRequest
import com.tourisz.entity.OTPData
import com.tourisz.api.request.SignupRequest

interface LoginContract {


    companion object {
        var LOGIN_ACTION : Int= 1
        var SIGNUP_ACTION : Int= 2
        var FORGOT_ACTION : Int= 3
        var OTP_ACTION : Int= 4


    }

    /**
     * Call when user interact with the view
     */
    interface Presenter {

        fun onDestroy()

        fun onLoginClicked()

        fun onRegisterClicked()

        fun onForgotPassClicked()

        fun onForgotUnameClicked()

        fun updatePassword(password: String)

        fun updateUname(uname: String)

        fun updateName(name: String)

        fun updateEmailId(emailId: String)

        fun updateSignupPassword(signupPassword: String)

        fun updatePhone(phone: String)

        fun updateAgencyName(phone: String)

        fun updateUserType(type: Int)

        fun updateTerms(termChecked: Boolean)

        fun updateForgotPhoneEmail(phoneEmail: String)

        fun updateForgotType(isForgotPass: Boolean)

        fun onForgotSendClicked()

        fun updateOTP(otp: String)

        fun onOTPSendClicked()

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     */
    interface MainView {

        fun showProgress()

        fun hideProgress()

        fun onResponseSuccess(actionType: Int,userType:Int, response: String)

        fun onResponseFailure(actionType: Int, error: String)

    }


    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     */
    interface NetworkIntractor {

        interface OnFinishedListener {

            fun onFinished(actionType: Int, userType:Int, response: String)

            fun onFailure(actionType: Int, error: String)

        }

        fun doLogin(loginData: LoginRequest, onFinishedListener: OnFinishedListener)

        fun doSignup(signupRequest: SignupRequest, onFinishedListener: OnFinishedListener)

        fun doForgot(forgotUnamePassword: ForgotUnamePassword, onFinishedListener: OnFinishedListener)

        fun doValidateOTP(otpData: OTPData, onFinishedListener: OnFinishedListener)


    }


}
