package com.tourisz.user.presenter.login

import android.util.Log
import com.android.volley.Response
import com.google.gson.Gson
import com.tourisz.Application
import com.tourisz.api.response.RegisterResponse
import com.tourisz.api.URLS
import com.tourisz.entity.ForgotUnamePassword
import com.tourisz.api.request.LoginRequest
import com.tourisz.entity.OTPData
import com.tourisz.api.request.SignupRequest
import com.android.volley.Request
import org.json.JSONObject
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.util.Constants
import com.tourisz.util.UserTypes
import com.tourisz.util.helper.AndroidHelper


class NetworkIntractorImplementation : LoginContract.NetworkIntractor {

    override fun doLogin(loginData: LoginRequest, onFinishedListener: LoginContract.NetworkIntractor.OnFinishedListener) {

        Log.e("req",AndroidHelper.objectToString(loginData))
        val jsonObj = JSONObject(AndroidHelper.objectToString(loginData))

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().LOGIN(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    Log.e("Response", response.toString())
                    val registerResponse: RegisterResponse = Gson().fromJson(response.toString(), RegisterResponse::class.java)

                    if (registerResponse.result) {
                        val userResponse = registerResponse.data
                        onFinishedListener.onFinished(LoginContract.LOGIN_ACTION, userResponse.userType, registerResponse.response)
                        AndroidHelper.addSharedPreference(Application.getInstance(), Constants.USER_DATA, Gson().toJson(registerResponse.data))
                    } else {
                        onFinishedListener.onFailure(LoginContract.LOGIN_ACTION, registerResponse.response)
                    }


                }, Response.ErrorListener { error ->

            onFinishedListener.onFailure(LoginContract.LOGIN_ACTION, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }


    override fun doSignup(signupRequest: SignupRequest, onFinishedListener: LoginContract.NetworkIntractor.OnFinishedListener) {

        Log.e("req",AndroidHelper.objectToString(signupRequest))

        val jsonObj = JSONObject(AndroidHelper.objectToString(signupRequest))

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().SIGNUP(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    Log.e("Response", response.toString())
                    var registerResponse: RegisterResponse = Gson().fromJson(response.toString(), RegisterResponse::class.java)

                    if (registerResponse.result) {
                        var userResponse = registerResponse.data
                        onFinishedListener.onFinished(LoginContract.SIGNUP_ACTION,userResponse.userType, registerResponse.response)
                        AndroidHelper.addSharedPreference(Application.getInstance(), Constants.USER_DATA, Gson().toJson(registerResponse.data))
                    } else {
                        onFinishedListener.onFailure(LoginContract.SIGNUP_ACTION, registerResponse.response)
                    }


                }, Response.ErrorListener { error ->

            onFinishedListener.onFailure(LoginContract.SIGNUP_ACTION, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }


    override fun doForgot(forgotUnamePassword: ForgotUnamePassword, onFinishedListener: LoginContract.NetworkIntractor.OnFinishedListener) {


        Log.e("req",AndroidHelper.objectToString(forgotUnamePassword))

        val jsonObj = JSONObject(AndroidHelper.objectToString(forgotUnamePassword))

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().FORGOT(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    Log.e("Response", response.toString())
                    if(response.getBoolean("result")) {
                        onFinishedListener.onFinished(LoginContract.FORGOT_ACTION, UserTypes.NORMAL_USER, response.getString("response"))
                    }else{
                        onFinishedListener.onFailure(LoginContract.FORGOT_ACTION, response.getString("response"))
                    }

                }, Response.ErrorListener { error ->

            onFinishedListener.onFailure(LoginContract.FORGOT_ACTION, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }


    override fun doValidateOTP(otpData: OTPData, onFinishedListener: LoginContract.NetworkIntractor.OnFinishedListener) {

        /*val req = StringRequest(URLS.VALIDATE_OTP, Response.Listener { response ->

            onFinishedListener.onFinished(LoginContract.OTP_ACTION, response)

        }, Response.ErrorListener { error ->

            var msg = "Error"
            if (error != null && error.message != null) {
                msg = error.message!!
            }
            onFinishedListener.onFailure(LoginContract.OTP_ACTION, msg)
        })

        Application.getInstance().addToRequestQueue(req)*/
    }

}
