package com.tourisz.admin.view.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.tourisz.Application

import com.tourisz.R
import com.tourisz.api.URLS
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.entity.HotelBooking
import com.tourisz.user.view.fragment.SearchFragment
import com.tourisz.util.SavingTypes
import com.tourisz.util.helper.AndroidHelper
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TnCFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var isEditable: Boolean = false
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isEditable = it.getBoolean(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var mView: View
    private lateinit var edtAbout: EditText
    private lateinit var txtAbout: TextView
    private lateinit var btnSave: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_tnc, container, false)
        edtAbout = mView.findViewById(R.id.edtAbout)
        txtAbout = mView.findViewById(R.id.txtAbout)
        btnSave = mView.findViewById(R.id.btnSave)
        edtAbout.setHint(getString(R.string.terms))

        if (isEditable) {
            txtAbout.visibility = View.GONE
            edtAbout.visibility = View.VISIBLE
            btnSave.visibility = View.VISIBLE
        } else {
            txtAbout.visibility = View.VISIBLE
            edtAbout.visibility = View.GONE
            btnSave.visibility = View.GONE
        }

        btnSave.setOnClickListener{
            if (edtAbout.text.toString().isEmpty()) {
                AndroidHelper.showToast(activity, "Enter terms and condition")
            } else {
                if (AndroidHelper.isNetworkAvailable(activity)) {
                    updateAbout()
                } else {
                    AndroidHelper.showToast(activity, getString(R.string.no_internet))
                }
            }        }

        if (AndroidHelper.isNetworkAvailable(activity)) {
            getAbout()
        } else {
            AndroidHelper.showToast(activity, getString(R.string.no_internet))
        }
        return mView
    }



    companion object {
        @JvmStatic
        fun newInstance(isEditable: Boolean, param2: String) =
                TnCFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(ARG_PARAM1, isEditable)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun getAbout(){
        val jsonObj = JSONObject()

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_TNC(), jsonObj,
                Response.Listener<JSONObject> { response ->

                    Log.e("Response", response.toString())
                    edtAbout.text = Editable.Factory.getInstance().newEditable(response.getString("content"))


                }, Response.ErrorListener { error ->

            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)
    }


    fun updateAbout(){
        val jsonObj = JSONObject()
        jsonObj.put("id",3)
        jsonObj.put("content", edtAbout.text.toString())
        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().UPDATE_TNC(), jsonObj,
                Response.Listener<JSONObject> { response ->

                    Log.e("Response", response.toString())
                    AndroidHelper.showToast(activity, response.getString("response"))


                }, Response.ErrorListener { error ->

            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)
    }
}
