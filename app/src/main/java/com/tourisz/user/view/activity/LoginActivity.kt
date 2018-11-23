package com.tourisz.user.view.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.tourisz.R
import com.tourisz.admin.view.activity.AdminHomeActivity
import com.tourisz.agent.view.activity.AgentHomeActivity
import com.tourisz.user.presenter.login.LoginContract
import com.tourisz.user.presenter.login.LoginMainPresenterImplementation
import com.tourisz.user.presenter.login.NetworkIntractorImplementation
import com.tourisz.user.view.fragment.ForgotFragment
import com.tourisz.user.view.fragment.LoginFragment
import com.tourisz.user.view.fragment.OTPFragment
import com.tourisz.user.view.fragment.SignUpFragment
import com.tourisz.util.Constants
import com.tourisz.util.UserTypes
import com.tourisz.util.helper.AndroidHelper
import com.tourisz.util.helper.FragmentHelper
import java.util.concurrent.TimeUnit


class LoginActivity : BaseActivity(), LoginFragment.OnFragmentInteractionListener, ForgotFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener, OTPFragment.OnFragmentInteractionListener,
        LoginContract.MainView, FragmentManager.OnBackStackChangedListener {

    private lateinit var loginFragment: LoginFragment
    private lateinit var forgotFragment: ForgotFragment
    private lateinit var signUpFragment: SignUpFragment
    private lateinit var otpFragment: OTPFragment;

    private lateinit var fragmentManager: FragmentManager
    private var progressDialog: ProgressDialog? = null
    private var presenter: LoginContract.Presenter? = null
    private var phone: String = ""
    private var userType: Int = UserTypes.NORMAL_USER
    private lateinit var imgRest: ImageView
    private lateinit var imgLogin: ImageView
    private lateinit var imgLogo: ImageView
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        imgLogin = findViewById(R.id.imgLogin)
        imgRest = findViewById(R.id.imgRest)
        imgLogo = findViewById(R.id.imgLogo)

        initProgressDialog()
        presenter = LoginMainPresenterImplementation(this, NetworkIntractorImplementation())

        fragmentManager = supportFragmentManager;
        loginFragment = LoginFragment.newInstance()
        replaceFragment(loginFragment, false)

        fragmentManager.addOnBackStackChangedListener(this)


    }

    override fun onBackStackChanged() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is LoginFragment) {
            imgLogin.visibility = View.VISIBLE
            imgRest.visibility = View.GONE
            imgLogo.setImageResource(R.mipmap.ic_logo_black)

        } else if (currentFragment is SignUpFragment) {
            imgLogin.visibility = View.VISIBLE
            imgRest.visibility = View.GONE
            imgLogo.setImageResource(R.mipmap.ic_logo_black)

        } else {
            imgLogin.visibility = View.GONE
            imgRest.visibility = View.VISIBLE
            imgLogo.setImageResource(R.mipmap.ic_logo)

        }
    }


    fun replaceFragment(fragment: Fragment, addToBackstack: Boolean) {
        FragmentHelper.newInstance().replaceFragment(fragmentManager, fragment, addToBackstack, R.id.rootContainer)
    }

    override fun onLoginClicked(uname: String, pass: String) {

        presenter?.updateUname(uname)
        presenter?.updatePassword(pass)


        presenter?.onLoginClicked()
    }


    override fun onFbLoginClicked() {
        AndroidHelper.showToast(this, "Coming soon..")
    }

    override fun onGplusLoginClicked() {
        AndroidHelper.showToast(this, "Coming soon..")
    }

    override fun onForgotPassClicked() {
        forgotFragment = ForgotFragment.newInstance(true)
        replaceFragment(forgotFragment, true)
    }

    override fun onForgotUnameClicked() {
        forgotFragment = ForgotFragment.newInstance(false)
        replaceFragment(forgotFragment, true)
    }

    override fun onSignUpClicked() {
        signUpFragment = SignUpFragment.newInstance()
        replaceFragment(signUpFragment, true)
    }

    override fun onForgotSendClicked(isForgotPass: Boolean, phoneEmail: String) {
        presenter?.updateForgotType(isForgotPass)
        presenter?.updateForgotPhoneEmail(phoneEmail)


        presenter?.onForgotSendClicked()

    }

    override fun onRegisterClicked(name: String, emailId: String, password: String, phone: String, isTermChecked: Boolean, userType: Int, agencyName: String) {
        presenter?.updateName(name)
        presenter?.updateSignupPassword(password)
        presenter?.updateEmailId(emailId)
        presenter?.updatePhone(phone)
        presenter?.updateTerms(isTermChecked)
        presenter?.updateUserType(userType)
        presenter?.updateAgencyName(agencyName)
        this.phone = phone

        presenter?.onRegisterClicked()

    }


    fun openHome(userType: Int) {
        when (userType) {
            UserTypes.NORMAL_USER -> startActivity(Intent(this, HomeActivity
            ::class.java))

            UserTypes.ADMIN -> startActivity(Intent(this, AdminHomeActivity
            ::class.java))

            UserTypes.AGENT -> startActivity(Intent(this, AgentHomeActivity
            ::class.java))
        }

        finish()
    }

    override fun onTncClicked() {
        startActivity(Intent(this, TnCActivity
        ::class.java))
    }

    fun initProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Please wait..")
    }


    override fun showProgress() {
        progressDialog?.show()
    }

    override fun hideProgress() {
        progressDialog?.dismiss()
    }


    override fun onResponseSuccess(actionType: Int, userType: Int, response: String) {
        AndroidHelper.hideKeyboard(this)

        when (actionType) {

            LoginContract.LOGIN_ACTION -> {
                AndroidHelper.addSharedPreference(this, Constants.KEY_USER, response)
                openHome(userType)
            }

            LoginContract.FORGOT_ACTION -> {
                fragmentManager.popBackStack()
            }

            LoginContract.OTP_ACTION -> {
                openHome(userType)
            }


            LoginContract.SIGNUP_ACTION -> {
                this.userType = userType
                fragmentManager.popBackStack()
                otpFragment = OTPFragment.newInstance()
                replaceFragment(otpFragment, true)
                sendOtp()
            }

        }
        Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()
    }


    override fun onResponseFailure(actionType: Int, error: String) {
        AndroidHelper.hideKeyboard(this)

        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        presenter?.onDestroy()
        hideProgress()
        super.onDestroy()
    }


    override fun onOptSendClicked(otp: String) {
        AndroidHelper.showToast(applicationContext, getString(R.string.otp_sent))
        resendOtp()
    }

    override fun onVerifyClicked(otp: String) {
        val credential = PhoneAuthProvider.getCredential(mVerificationId, otp)
        signInWithPhoneAuthCredential(credential)

    }

    fun sendOtp() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone, // Phone number to verify
                60,             // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,           // Activity (for callback binding)
                mCallbacks)
    }


    fun resendOtp() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this, // Activity (for callback binding)
                mCallbacks, // OnVerificationStateChangedCallbacks
                mResendToken)
    }


    private var mVerificationId: String = ""
    private var recivedOTP: String = ""

    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // verification completed
            recivedOTP = credential.smsCode!!
            otpFragment.setOtp(recivedOTP)
            verifyVerificationCode(recivedOTP)

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked if an invalid request for verification is made,
            // for instance if the the phone number format is invalid.
            e.printStackTrace()
            if (e is FirebaseAuthInvalidCredentialsException) {
                //
                AndroidHelper.showToast(applicationContext, "Invalid request")

            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                AndroidHelper.showToast(applicationContext, "The SMS quota for the project has been exceeded")

            } else {
                AndroidHelper.showToast(applicationContext, e.message)

            }

        }

        override fun onCodeSent(verificationId: String?,
                                token: PhoneAuthProvider.ForceResendingToken?) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later
            if (!TextUtils.isEmpty(verificationId)) {
                mVerificationId = verificationId!!
            }
            mResendToken = token

            AndroidHelper.showToast(applicationContext, "OTP sent")

        }

        override fun onCodeAutoRetrievalTimeOut(verificationId: String?) {
            // called when the timeout duration has passed without triggering onVerificationCompleted
            super.onCodeAutoRetrievalTimeOut(verificationId)
        }
    }


    private fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId, code)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this@LoginActivity) { task ->
                    if (task.isSuccessful) {
                        AndroidHelper.showToast(applicationContext, getString(R.string.otp_verfied))
                        openHome(userType)

                    } else {

                        //verification unsuccessful.. display an error message

                        var message = "Somthing is wrong, we will fix it soon..."

                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered..."
                        }
                        AndroidHelper.showToast(applicationContext, message)
                    }
                }
    }


    fun getCurrentFragment(): Fragment? {
        return fragmentManager.findFragmentById(R.id.rootContainer)
    }


}
