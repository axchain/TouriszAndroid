package com.tourisz.user.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.StringRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.tourisz.Application
import com.tourisz.admin.view.activity.AdminHomeActivity
import com.tourisz.agent.adapter.HotelsAdapter
import com.tourisz.agent.view.activity.AgentHomeActivity
import com.tourisz.api.URLS
import com.tourisz.api.response.HotelListResponse
import com.tourisz.api.response.UISettings
import com.tourisz.api.response.UserData
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.util.Constants
import com.tourisz.util.Constants.Companion.FCM_TOKEN
import com.tourisz.util.UserTypes
import com.tourisz.util.helper.AndroidHelper
import org.json.JSONObject


class SplashActivity : AppCompatActivity() {

    val TAG: String = SplashActivity::class.java.simpleName;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerFCMToken()
       /* if(AndroidHelper.isNetworkAvailable(this)) {
            getUISettingsAPI()
        }else{
        }*/
        goHome()

    }


    fun registerFCMToken() {

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.e(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result.token
                    AndroidHelper.addSharedPreference(this, FCM_TOKEN, token)
                    Log.e(TAG, "Token: $token")
                })

    }


    fun getUISettingsAPI() {


        val jsonObjReq = StringRequest(Request.Method.GET,
                URLS.newInstance().GET_UISETTING(),
                Response.Listener<String> { response ->
                    Log.e("res", response)
                    AndroidHelper.addSharedPreference(this, Constants.UI_SETTINGS, response)
                    goHome()
                }, Response.ErrorListener { error ->
            goHome()
        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }

    fun goHome() {
        if (TextUtils.isEmpty(AndroidHelper.getSharedPreferenceString(this, Constants.USER_DATA))) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            var userResponse = Gson().fromJson<UserData>(AndroidHelper.getSharedPreferenceString(this, Constants.USER_DATA), UserData::class.java)
            if (userResponse?.userType == UserTypes.NORMAL_USER) {
                startActivity(Intent(this, HomeActivity::class.java))

            } else if (userResponse?.userType == UserTypes.AGENT) {
                startActivity(Intent(this, AgentHomeActivity::class.java))

            } else if (userResponse?.userType == UserTypes.ADMIN) {
                startActivity(Intent(this, AdminHomeActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        finish()
    }

}
