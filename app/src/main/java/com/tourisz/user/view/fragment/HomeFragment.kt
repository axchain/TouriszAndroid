package com.tourisz.user.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import com.tourisz.Application

import com.tourisz.R
import com.tourisz.api.URLS
import com.tourisz.api.response.FlyerData
import com.tourisz.api.response.FlyerListResponse
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.user.adapter.CarosalAdapter
import com.tourisz.util.HomeOptions
import com.tourisz.util.helper.AndroidHelper
import me.relex.circleindicator.CircleIndicator
import org.json.JSONObject
import java.util.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(),View.OnClickListener {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var mView: View
    private lateinit var viewPager: AutoScrollViewPager
    private lateinit var indicator: CircleIndicator
    private lateinit var txtHotel: TextView
    private lateinit var txtEvent: TextView
    private lateinit var txtBookings: TextView
    private lateinit var txtProfile: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false)
        initIds()

        viewPager.startAutoScroll()

        callFlyerAPI()

        return mView
    }

    fun callFlyerAPI(){
        if (AndroidHelper.isNetworkAvailable(activity)){
            getFlyersList()
        }else{
            AndroidHelper.showToast(activity, getString(R.string.no_internet))
        }
    }

    private fun initIds() {
        viewPager = mView.findViewById(R.id.viewPager)
        indicator = mView.findViewById(R.id.indicator)
        txtHotel = mView.findViewById(R.id.txtHotel)
        txtEvent = mView.findViewById(R.id.txtEvents)
        txtBookings = mView.findViewById(R.id.txtBookings)
        txtProfile = mView.findViewById(R.id.txtProfile)

        setListener(txtHotel, txtEvent, txtBookings, txtProfile)
    }

    fun setListener(vararg views: View){
        for (view in views){
            view.setOnClickListener(this)
        }
    }

    private fun setupViewPager(viewPager: ViewPager, data: ArrayList<FlyerData>) {
        val adapter = CarosalAdapter(activity, data)
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)

    }

    fun onHomeOptionSelected(selection: Int) {
        listener?.onHomeOptionSelected(selection)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        fun onHomeOptionSelected(selection: Int)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onClick(view: View) {

        when (view.id){

            R.id.txtHotel -> onHomeOptionSelected(HomeOptions.HOTEL)

            R.id.txtEvents -> onHomeOptionSelected(HomeOptions.EVENT)

            R.id.txtBookings -> onHomeOptionSelected(HomeOptions.MY_BOOKING)

            R.id.txtProfile -> onHomeOptionSelected(HomeOptions.MY_PROFILE)


        }

    }





    fun getFlyersList(){
        val jsonObj = JSONObject()

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_FLYER_LIST(), jsonObj,
                Response.Listener<JSONObject> { response ->

                    Log.e("Response", response.toString())
                    var flyerListResponse: FlyerListResponse = Gson().fromJson(response.toString(), FlyerListResponse::class.java)
                    if (flyerListResponse.result) {
                        if (flyerListResponse.data.size > 0) {
                            setupViewPager(viewPager, flyerListResponse.data)
                       } else {

                        }

                    } else {

                    }
                }, Response.ErrorListener { error ->

            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)
    }

}
