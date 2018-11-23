package com.tourisz.admin.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.tourisz.R
import com.tourisz.user.adapter.HotelBookingAdapter
import com.tourisz.entity.HotelBooking
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.*
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import com.tourisz.Application
import com.tourisz.admin.adapter.UsersAdapter
import com.tourisz.agent.adapter.HotelsAdapter
import com.tourisz.api.URLS
import com.tourisz.api.response.HotelListResponse
import com.tourisz.api.response.UsersData
import com.tourisz.api.response.UsersListResponse
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.util.BookingTypes
import com.tourisz.util.UserTypes
import com.tourisz.util.helper.AndroidHelper
import org.json.JSONObject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UsersFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
            callGetUsersListAPI()
        } else {
            swipe.isRefreshing = false
            AndroidHelper.showToast(activity, getString(R.string.no_internet))
        }
    }


    public fun onUserListItemClick(usersData: UsersData) {
        listener?.onUserListItemClick(usersData)
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
        // TODO: Update argument type and name
        fun onUserListItemClick(usersData: UsersData)

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                UsersFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun callGetUsersListAPI() {

        val jsonObj = JSONObject()

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_USER_LIST(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    swipe.isRefreshing = false
                    Log.e("Response", response.toString())
                     var usersListResponse: UsersListResponse = Gson().fromJson(response.toString(), UsersListResponse::class.java)

                     if (usersListResponse.result) {
                         if (usersListResponse.data.size > 0) {
                             rvHotel.visibility = View.VISIBLE
                             txtNoData.visibility = View.GONE
                             rvHotel.adapter = UsersAdapter(usersListResponse.data, this)
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
