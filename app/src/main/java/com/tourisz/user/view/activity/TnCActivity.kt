package com.tourisz.user.view.activity

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.Html
import android.util.Log
import android.view.Menu
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.tourisz.Application
import com.tourisz.R
import com.tourisz.api.URLS
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.util.helper.AndroidHelper
import org.json.JSONObject

class TnCActivity : AppCompatActivity() {

    private lateinit var tnc: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tnc)
        val mToolbar = findViewById(R.id.toolbar) as Toolbar?
        supportActionBar?.setDisplayShowTitleEnabled(false);
        setSupportActionBar(mToolbar)
        mToolbar?.setNavigationOnClickListener { onBackPressed() }

        tnc = findViewById(R.id.tnc)
        if (AndroidHelper.isNetworkAvailable(this)){
            getAbout()
        }else{
            AndroidHelper.showToast(this, getString(R.string.no_internet))
        }
    }


    fun getAbout(){
        val jsonObj = JSONObject()

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_TNC(), jsonObj,
                Response.Listener<JSONObject> { response ->

                    Log.e("Response", response.toString())
                    tnc.text = Editable.Factory.getInstance().newEditable(response.getString("content"))


                }, Response.ErrorListener { error ->

            AndroidHelper.showToast(this, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)
    }

}
