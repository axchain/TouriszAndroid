package com.tourisz.admin.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import com.tourisz.Application
import com.tourisz.R
import com.tourisz.admin.adapter.TransactionAdapter
import com.tourisz.api.URLS
import com.tourisz.api.request.Object
import com.tourisz.api.response.EventBookingsResponse
import com.tourisz.api.response.HotelBookingsResponse
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.util.BookingTypes
import com.tourisz.util.helper.AndroidHelper
import com.tourisz.util.listener.TransactionClickListener
import org.json.JSONObject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TransactionFragment : Fragment(), TransactionClickListener {
    private var type: Int = BookingTypes.HOTEL
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var mView: View
    private lateinit var rvHotel: RecyclerView
    private lateinit var txtNoData: TextView
    private lateinit var swipe: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_hotel_booking, container, false)
        rvHotel = mView.findViewById(R.id.rvHotel)
        txtNoData = mView.findViewById(R.id.txtNoData)
        swipe = mView.findViewById(R.id.swipe)

        rvHotel.layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(rvHotel.getContext(), DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(activity?.baseContext!!, R.drawable.custom_divider)!!)
        rvHotel.addItemDecoration(divider)

        callAPI()

        swipe.setOnRefreshListener {
            callAPI()
        }
        return mView
    }


    fun callAPI() {
        swipe.isRefreshing = true

        if (AndroidHelper.isNetworkAvailable(activity)) {
            if (type == BookingTypes.HOTEL) {
                callGetHotelBookingsListAPI()
            }else{
                callGetEventBookingsListAPI()
            }
        } else {
            swipe.isRefreshing = false
            AndroidHelper.showToast(activity, getString(R.string.no_internet))
        }
    }


    override fun onTranactionClick(transaction: Object?) {
        listener?.onTranactionClick(transaction)
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
        fun onTranactionClick(transaction: Object?)

    }

    companion object {
        @JvmStatic
        fun newInstance(type: Int, param2: String) =
                TransactionFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, type)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun callGetHotelBookingsListAPI() {

        val jsonObj = JSONObject()
        jsonObj.put("adminId", 1)
        //jsonObj.put("title", "")
        Log.e("req", jsonObj.toString())



        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_HOTEL_BOOKINGS(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    swipe.isRefreshing = false
                    Log.e("Response", response.toString())
                    var hotelListResponse: HotelBookingsResponse = Gson().fromJson(response.toString(), HotelBookingsResponse::class.java)

                    if (hotelListResponse.result) {
                        if (hotelListResponse.data.size > 0) {
                            rvHotel.visibility = View.VISIBLE
                            txtNoData.visibility = View.GONE
                            rvHotel.adapter = TransactionAdapter(hotelListResponse.data, this)
                        } else {
                            rvHotel.visibility = View.GONE
                            txtNoData.visibility = View.VISIBLE
                        }

                    } else {
                        rvHotel.visibility = View.GONE
                        txtNoData.visibility = View.VISIBLE
                    }

                }, Response.ErrorListener { error ->
            swipe.isRefreshing = false

            rvHotel.visibility = View.GONE
            txtNoData.visibility = View.VISIBLE
            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }



    fun callGetEventBookingsListAPI() {

        val jsonObj = JSONObject()
        jsonObj.put("adminId", 1)
        // jsonObj.put("title", "")
        Log.e("req", jsonObj.toString())

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_EVENT_BOOKINGS(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    swipe.isRefreshing = false
                    Log.e("Response", response.toString())
                    var eventBookingsResponse: EventBookingsResponse = Gson().fromJson(response.toString(), EventBookingsResponse::class.java)

                    if (eventBookingsResponse.result) {
                        if (eventBookingsResponse.data.size > 0) {
                            rvHotel.visibility = View.VISIBLE
                            txtNoData.visibility = View.GONE
                            rvHotel.adapter = TransactionAdapter(eventBookingsResponse.data, this)
                        } else {
                            rvHotel.visibility = View.GONE
                            txtNoData.visibility = View.VISIBLE
                        }

                    } else {
                        rvHotel.visibility = View.GONE
                        txtNoData.visibility = View.VISIBLE
                    }

                }, Response.ErrorListener { error ->
            swipe.isRefreshing = false

            rvHotel.visibility = View.GONE
            txtNoData.visibility = View.VISIBLE
            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }
}
