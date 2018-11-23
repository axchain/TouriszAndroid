package com.tourisz.agent.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import com.tourisz.Application

import com.tourisz.R
import com.tourisz.api.URLS
import com.tourisz.api.request.Object
import com.tourisz.api.response.*
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.user.adapter.SlidingImageAdapter
import com.tourisz.util.UserTypes
import com.tourisz.util.helper.AndroidHelper
import me.relex.circleindicator.CircleIndicator
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AgentShowDetailsFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var hotelData: HotelData? = null
    private var eventData: EventData? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                hotelData = it.getSerializable(ARG_PARAM1) as HotelData?
                eventData = it.getSerializable(ARG_PARAM2) as EventData?
            }
        }
    }

    private lateinit var mView: View
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var viewPager: AutoScrollViewPager
    private lateinit var indicator: CircleIndicator
    private lateinit var txtEventDate: TextView
    private lateinit var txtTitle: TextView
    private lateinit var linEventDate: LinearLayout
    private lateinit var linRoomType: LinearLayout
    private lateinit var txtShortDesc: TextView
    private lateinit var txtLargeDesc: TextView
    private lateinit var txtWeblink: TextView
    private lateinit var txtPersonName: TextView
    private lateinit var txtRoomType: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtAddress: TextView
    private lateinit var txtCity: TextView
    private lateinit var txtCountry: TextView
    private lateinit var txtAgentName: TextView
    private lateinit var txtAvailability: TextView
    private lateinit var txtStatus: TextView
    private lateinit var txtPostDate: TextView
    private lateinit var txtAgencyName: TextView
    private lateinit var txtRate: TextView
    private lateinit var linAgencyName: LinearLayout
    private lateinit var linAgent: LinearLayout
    private lateinit var userData: UserData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_agent_show_details, container, false)
        userData = AndroidHelper.getUser(activity)
        initIds()
        viewPager.startAutoScroll()

        Log.e("hotel", hotelData.toString())
        Log.e("event", eventData.toString())

        linRoomType.visibility = View.GONE

        if (eventData != null) {
            linRoomType.visibility = View.VISIBLE
            setEventData(eventData)
        } else {
            linEventDate.visibility = View.GONE
            setHotelData(hotelData)
        }

        if (userData.userType == UserTypes.AGENT) {
            btnDelete.visibility = View.GONE
        }
        return mView
    }


    private fun initIds() {
        linAgencyName = mView.findViewById(R.id.linAgencyName)
        txtWeblink = mView.findViewById(R.id.txtWeblink)
        txtPersonName = mView.findViewById(R.id.txtPersonName)
        txtRoomType = mView.findViewById(R.id.txtRoomType)
        txtPhone = mView.findViewById(R.id.txtPhone)
        txtAddress = mView.findViewById(R.id.txtAddress)
        txtCity = mView.findViewById(R.id.txtCity)
        txtCountry = mView.findViewById(R.id.txtCountry)
        txtAgentName = mView.findViewById(R.id.txtAgentName)
        txtAvailability = mView.findViewById(R.id.txtAvailability)
        txtStatus = mView.findViewById(R.id.txtStatus)
        txtPostDate = mView.findViewById(R.id.txtPostDate)
        txtAgencyName = mView.findViewById(R.id.txtAgencyName)
        txtRate = mView.findViewById(R.id.txtRate)
        viewPager = mView.findViewById(R.id.viewPager)
        indicator = mView.findViewById(R.id.indicator)
        btnEdit = mView.findViewById(R.id.btnEdit)
        btnDelete = mView.findViewById(R.id.btnDelete)
        txtEventDate = mView.findViewById(R.id.txtEventDate)
        linEventDate = mView.findViewById(R.id.linEventDate)
        txtTitle = mView.findViewById(R.id.txtTitle)
        linRoomType = mView.findViewById(R.id.linRoomType)
        txtShortDesc = mView.findViewById(R.id.txtShortDesc)
        txtLargeDesc = mView.findViewById(R.id.txtLargeDesc)
        linAgent = mView.findViewById(R.id.linAgent)

        setListener(btnEdit, btnDelete)
    }

    fun setListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    fun setEventData(eventData: EventData?) {
        if (!eventData?.poster.isNullOrBlank()) {
            setupViewPager(viewPager, eventData?.poster)
        }
        txtTitle.setText(eventData?.eventTitle)
        txtEventDate.setText(eventData?.eventDateTime)
        txtShortDesc.setText(eventData?.shortDescription)
        txtLargeDesc.setText(eventData?.description)
        txtWeblink.setText(eventData?.webLink)
        txtPersonName.setText(eventData?.contactPerson)
        txtPhone.setText(eventData?.contactNumber)
        txtRoomType.setText("NA")
        txtAddress.setText(eventData?.address)
        txtCity.setText(eventData?.city)
        txtCountry.setText(eventData?.country)
        txtStatus.setText(AndroidHelper.getStatus(eventData?.status))
        txtAvailability.setText(AndroidHelper.getAvailability(eventData?.isAvailable))
        txtPostDate.setText(eventData?.createdOn)
        txtRate.setText(eventData?.bookingCharges)
        eventData?.agentId?.toInt()?.let { setAgetnt(it) }

    }

    fun setHotelData(hotelData: HotelData?) {
        if (!hotelData?.poster.isNullOrBlank()) {
            Log.e("img", hotelData?.poster)
            setupViewPager(viewPager, hotelData?.poster)
        }
        txtTitle.setText(hotelData?.hotelName)
        txtRate.setText(hotelData?.bookingCharges)
        txtEventDate.setText("NA")
        txtShortDesc.setText(hotelData?.shortDescription)
        txtLargeDesc.setText(hotelData?.description)
        txtWeblink.setText(hotelData?.webLink)
        txtPersonName.setText(hotelData?.contactPerson)
        txtPhone.setText(hotelData?.contactNumber)
        txtRoomType.setText("NA")
        txtAddress.setText(hotelData?.address)
        txtCity.setText(hotelData?.city)
        txtCountry.setText(hotelData?.country)
        txtStatus.setText(AndroidHelper.getStatus(hotelData?.status))
        txtAvailability.setText(AndroidHelper.getAvailability(hotelData?.isAvailable))
        txtPostDate.setText(hotelData?.createdOn)
        hotelData?.agentId?.toInt()?.let { setAgetnt(it) }
    }


    private fun setupViewPager(viewPager: ViewPager, nameslist: String?) {
        val names = AndroidHelper.getImageArray(nameslist)
        val adapter = SlidingImageAdapter(activity, names)
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnDelete -> onDeleteClicked()
            R.id.btnEdit -> onEditClicked()

        }
    }
    fun setAgetnt(id: Int){
        if(userData.userType != UserTypes.ADMIN) {
            if (AndroidHelper.isNetworkAvailable(activity)) {
                callGetAgentDetailsAPI(id)
            } else {
                AndroidHelper.showToast(activity, getString(R.string.no_internet))
            }
        }else{
            linAgencyName.visibility = View.GONE
            linAgent.visibility = View.GONE
        }
    }

    fun onEditClicked() {
        listener?.onHotelEventEditClicked(hotelData, eventData)
    }

    fun onDeleteClicked() {
        listener?.onHotelEventDeleteClicked(hotelData, eventData)
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

        fun onHotelEventEditClicked(hotelData: HotelData?, eventData: EventData?)

        fun onHotelEventDeleteClicked(hotelData: HotelData?, eventData: EventData?)

    }

    companion object {

        @JvmStatic
        fun newInstance(hotelData: HotelData?, eventData: EventData?) =
                AgentShowDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, hotelData)
                        putSerializable(ARG_PARAM2, eventData)
                    }
                }
    }

    fun setUpdatedData(obj: Object) {
        if (obj is HotelData) {

            hotelData = obj
            setHotelData(hotelData)

        } else if (obj is EventData) {

            eventData = obj
            setEventData(eventData)

        }
    }



    fun callGetAgentDetailsAPI(id: Int) {

        val jsonObj = JSONObject()
        jsonObj.put("id", id)
        Log.e("req", jsonObj.toString())

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().GET_AGENT_DETAILS(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    Log.e("Response", response.toString())
                    var agentDetailsResponse: AgentDetailsResponse = Gson().fromJson(response.toString(), AgentDetailsResponse::class.java)

                    if (agentDetailsResponse.result && agentDetailsResponse.data!=null) {
                        txtAgentName.setText(agentDetailsResponse.data.userName)
                        txtAgencyName.setText(agentDetailsResponse.data.agencyName)
                    }

                }, Response.ErrorListener { error ->

            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }

    override fun onDestroy() {
        super.onDestroy()
        Application.getInstance().cancelPendingRequests()
    }
}
