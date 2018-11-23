package com.tourisz.user.presenter.login

import android.text.TextUtils
import com.tourisz.Application
import com.tourisz.R
import com.tourisz.entity.ForgotUnamePassword
import com.tourisz.api.request.LoginRequest
import com.tourisz.entity.OTPData
import com.tourisz.api.request.SignupRequest
import com.tourisz.util.UserTypes
import com.tourisz.util.validator.Validator
import java.util.regex.Pattern


class LoginMainPresenterImplementation(private var mainView: LoginContract.MainView?, private val networkIntractor: LoginContract.NetworkIntractor) : LoginContract.Presenter, LoginContract.NetworkIntractor.OnFinishedListener {
    private val loginData: LoginRequest
    private val signupRequest: SignupRequest
    private val forgotPhoneEmail: ForgotUnamePassword
    private val otpData: OTPData

    init {
        this.loginData = LoginRequest()
        this.signupRequest = SignupRequest()
        this.forgotPhoneEmail = ForgotUnamePassword()
        this.otpData = OTPData()

    }

    override fun onDestroy() {
        mainView = null
    }


    override fun onLoginClicked() {

        if (TextUtils.isEmpty(loginData.email)) {
            mainView?.onResponseFailure(LoginContract.LOGIN_ACTION, Application.resources.getString(R.string.enter_email))
            return
        }

        if (!Validator.newInstance().isValidEmail(loginData.email)) {
            mainView?.onResponseFailure(LoginContract.LOGIN_ACTION, Application.resources.getString(R.string.invalid_email))
            return
        }

        if (TextUtils.isEmpty(loginData.password)) {
            mainView?.onResponseFailure(LoginContract.LOGIN_ACTION, Application.resources.getString(R.string.enter_pass))
            return
        }


        if (!Validator.newInstance().isPasswordValid(loginData.password)) {
            mainView?.onResponseFailure(LoginContract.LOGIN_ACTION, Application.resources.getString(R.string.invalid_pass))
            return
        }

        mainView?.showProgress()
        networkIntractor.doLogin(loginData, this)

    }


    override fun onRegisterClicked() {

        if (TextUtils.isEmpty(signupRequest.userName)) {
            mainView?.onResponseFailure(LoginContract.SIGNUP_ACTION, Application.resources.getString(R.string.enter_uname)
            )
            return
        }
        val p = Pattern.compile("[A-Za-z]")
        val m = p.matcher(signupRequest.userName)
        // boolean b = m.matches();
        if (!m.find()) {
            mainView?.onResponseFailure(LoginContract.SIGNUP_ACTION, Application.resources.getString(R.string.enter_validuname)
            )
            return
        }

        if (signupRequest.userType == UserTypes.AGENT) {
            if (TextUtils.isEmpty(signupRequest.agencyName)) {
                mainView?.onResponseFailure(LoginContract.SIGNUP_ACTION, Application.resources.getString(R.string.enter_agencyname)
                )
                return
            }
        }

        if (!Validator.newInstance().isValidUname(signupRequest.userName)) {
            mainView?.onResponseFailure(LoginContract.SIGNUP_ACTION, Application.resources.getString(R.string.invalid_uname)
            )
            return
        }


        if (TextUtils.isEmpty(signupRequest.email)) {
            mainView?.onResponseFailure(LoginContract.SIGNUP_ACTION, Application.resources.getString(R.string.enter_email)
            )
            return
        }

        if (!Validator.newInstance().isValidEmail(signupRequest.email)) {
            mainView?.onResponseFailure(LoginContract.SIGNUP_ACTION, Application.resources.getString(R.string.invalid_email)
            )
            return
        }

        if (TextUtils.isEmpty(signupRequest.password)) {
            mainView?.onResponseFailure(LoginContract.SIGNUP_ACTION, Application.resources.getString(R.string.enter_pass)
            )
            return
        }


        if (!Validator.newInstance().isPasswordValid(signupRequest.password)) {
            mainView?.onResponseFailure(LoginContract.SIGNUP_ACTION, Application.resources.getString(R.string.invalid_pass)
            )
            return
        }


        if (TextUtils.isEmpty(signupRequest.contactNumber)) {
            mainView?.onResponseFailure(LoginContract.SIGNUP_ACTION, Application.resources.getString(R.string.enter_phone)
            )
            return
        }


        if (Validator.newInstance().isPhoneNoValid(signupRequest.contactNumber)) {
            mainView?.onResponseFailure(LoginContract.SIGNUP_ACTION, Application.resources.getString(R.string.invalid_phone)
            )
            return
        }


        if (!signupRequest.isTermChecked) {
            mainView?.onResponseFailure(LoginContract.SIGNUP_ACTION, Application.resources.getString(R.string.accept_tnc)
            )
            return
        }


        mainView?.showProgress()
        networkIntractor.doSignup(signupRequest, this)
    }


    override fun onForgotSendClicked() {


        if (TextUtils.isEmpty(forgotPhoneEmail.emailOrMob)) {
            mainView?.onResponseFailure(LoginContract.FORGOT_ACTION, Application.resources.getString(R.string.forgot_enter_phone_email)
            )
            return
        }


        if (!Validator.newInstance().isPhoneNoValid(forgotPhoneEmail.emailOrMob) && !Validator.newInstance().isValidEmail(forgotPhoneEmail.emailOrMob)) {
            mainView?.onResponseFailure(LoginContract.FORGOT_ACTION, Application.resources.getString(R.string.forgot_valid_phone_email)
            )
            return
        }



        if (Validator.newInstance().isPhoneNoValid(forgotPhoneEmail.emailOrMob)) {
            //send on phone
            forgotPhoneEmail.isSendOnPhone = true
        } else {
            if (Validator.newInstance().isValidEmail(forgotPhoneEmail.emailOrMob)) {
                //send on email
                forgotPhoneEmail.isSendOnPhone = false

            }

        }

        mainView?.showProgress()
        networkIntractor.doForgot(forgotPhoneEmail, this)

    }


    override fun updateOTP(otp: String) {
        otpData.opt = otp
    }

    override fun onOTPSendClicked() {

        if (TextUtils.isEmpty(otpData.opt)) {
            mainView?.onResponseFailure(LoginContract.OTP_ACTION, Application.resources.getString(R.string.enter_otp)
            )
            return
        }


        if (!Validator.newInstance().isOTPValid(otpData.opt)) {
            mainView?.onResponseFailure(LoginContract.FORGOT_ACTION, Application.resources.getString(R.string.invalid_otp)

            )
            return
        }

        mainView?.showProgress()
        networkIntractor.doValidateOTP(otpData, this)

    }

    override fun onForgotPassClicked() {

    }

    override fun onForgotUnameClicked() {

    }

    override fun updatePassword(password: String) {
        loginData.password = password

    }

    override fun updateUname(uname: String) {
        loginData.email = uname
    }

    override fun updatePhone(phone: String) {
        signupRequest.contactNumber = phone

    }

    override fun updateSignupPassword(signupPassword: String) {
        signupRequest.password = signupPassword

    }

    override fun updateEmailId(emailId: String) {
        signupRequest.email = emailId

    }

    override fun updateName(name: String) {
        signupRequest.userName = name
    }

    override fun updateTerms(termChecked: Boolean) {
        signupRequest.isTermChecked = termChecked

    }

    override fun updateAgencyName(name: String) {
        signupRequest.agencyName = name
    }

    override fun updateUserType(type: Int) {
        signupRequest.userType = type

    }

    override fun updateForgotType(isForgotPass: Boolean) {
        forgotPhoneEmail.isForgotPass = isForgotPass

    }

    override fun updateForgotPhoneEmail(phoneEmail: String) {
        forgotPhoneEmail.emailOrMob = phoneEmail
    }


    override fun onFinished(actionType: Int, userType: Int, response: String) {
        mainView?.hideProgress()
        mainView?.onResponseSuccess(actionType, userType, response)
    }


    override fun onFailure(actionType: Int, error: String) {
        mainView?.hideProgress()
        mainView?.onResponseFailure(actionType, error)
    }
}
