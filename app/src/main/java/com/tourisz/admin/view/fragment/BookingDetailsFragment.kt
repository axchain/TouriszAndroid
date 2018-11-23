package com.tourisz.admin.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tourisz.Application

import com.tourisz.R
import com.tourisz.api.URLS
import com.tourisz.api.request.Object
import com.tourisz.api.response.AgentDetailsResponse
import com.tourisz.api.response.EventBookingData
import com.tourisz.api.response.HotelBookingData
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.entity.HotelBooking
import com.tourisz.util.BookingTypes
import com.tourisz.util.UserTypes
import com.tourisz.util.helper.AndroidHelper
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BookingDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param2: String? = null
    private var obj: Object? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                obj = it.getSerializable(ARG_PARAM1) as Object?
                param2 = it.getString(ARG_PARAM2)
            }

        }
    }

    private lateinit var mView: View
    private lateinit var imgHotel: ImageView
    private lateinit var txtTitle: TextView
    private lateinit var txtDate: TextView
    private lateinit var linRoom: LinearLayout
    private lateinit var linRoomType: LinearLayout

    private lateinit var txtNoPerson: TextView
    private lateinit var txtNoRoom: TextView
    private lateinit var txtRoomType: TextView
    private lateinit var txtRate: TextView
    private lateinit var txtBookingDate: TextView
    private lateinit var txtTransactionId: TextView
    private lateinit var txtComment: TextView
    private lateinit var txtAgentName: TextView
    private lateinit var txtAgencyName: TextView
    private lateinit var txtName: TextView
    private lateinit var img: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_booking_details, container, false)
        initIds()

        if (obj != null) {
            if (obj is HotelBookingData) {
                setHotelData(obj as HotelBookingData)
            } else if (obj is EventBookingData) {
                setEventData(obj as EventBookingData)
            }
        }

        return mView
    }

    fun setHotelData(hotelBookingData: HotelBookingData) {
        imgHotel.setImageResource(R.drawable.ic_hotel)
        linRoomType.visibility = View.VISIBLE
        linRoom.visibility = View.VISIBLE

        txtDate.setText("From " + hotelBookingData.bookingFromDate + " To " + hotelBookingData.bookingToDate)
        txtTitle.setText(hotelBookingData.hotelName)
        txtName.setText(hotelBookingData.customerName)
        txtNoRoom.setText(hotelBookingData.numberOfRooms)
        txtNoPerson.setText(hotelBookingData.numberOfPersons)
        txtRoomType.setText(AndroidHelper.getRoomType(hotelBookingData.roomType.toInt()))
        txtRate.setText(hotelBookingData.bookingCharges)
        txtTransactionId.setText(hotelBookingData.paymentTransactionId)
        txtBookingDate.setText(hotelBookingData.bookedOn)
        txtComment.setText(hotelBookingData.attachmentDescription)
        if (!TextUtils.isEmpty(hotelBookingData.attachment)) {
            setImage(hotelBookingData.attachment)
        }
        setAgetnt(hotelBookingData.agentId.toInt())
    }


    fun setEventData(eventBookingData: EventBookingData) {
        linRoom.visibility = View.GONE
        imgHotel.setImageResource(R.drawable.ic_event_available)
        linRoomType.visibility = View.GONE

        txtDate.text = eventBookingData.eventDateTime
        txtTitle.setText(eventBookingData.eventTitle)

        txtName.setText(eventBookingData.customerName)
        txtNoPerson.setText(eventBookingData.numberOfPersons)
        txtRate.setText(eventBookingData.bookingCharges)
        txtTransactionId.setText(eventBookingData.paymentTransactionId)
        txtBookingDate.setText(eventBookingData.bookedOn)
        txtComment.setText(eventBookingData.attachmentDescription)
        if (!TextUtils.isEmpty(eventBookingData.attachment)) {
            setImage(eventBookingData.attachment)
        }
        setAgetnt(eventBookingData.agentId.toInt())

    }

    fun setImage(name: String) {
        if (name.isNotEmpty()) {
            Glide.with(activity).load(URLS.newInstance().IMG_URL + name)
                    .placeholder(R.drawable.ic_banner_placeholder)
                    .crossFade()
                    .thumbnail(0.1f)
                    .into(img)
        }
    }

    private fun initIds() {
        txtDate = mView.findViewById(R.id.txtDate)
        imgHotel = mView.findViewById(R.id.imgHotel)
        txtTitle = mView.findViewById(R.id.txtTitle)
        linRoom = mView.findViewById(R.id.linRoom)
        linRoomType = mView.findViewById(R.id.linRoomType)
        txtNoPerson = mView.findViewById(R.id.txtNoPerson)
        txtNoRoom = mView.findViewById(R.id.txtNoRoom)
        txtRoomType = mView.findViewById(R.id.txtRoomType)
        txtRate = mView.findViewById(R.id.txtRate)
        txtBookingDate = mView.findViewById(R.id.txtBookingDate)
        txtTransactionId = mView.findViewById(R.id.txtTransactionId)
        txtComment = mView.findViewById(R.id.txtComment)
        txtAgentName = mView.findViewById(R.id.txtAgentName)
        txtAgencyName = mView.findViewById(R.id.txtAgencyName)
        txtName = mView.findViewById(R.id.txtName)
        img = mView.findViewById(R.id.img)


    }


    companion object {
        @JvmStatic
        fun newInstance(obj: Object, param2: String) =
                BookingDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, obj)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun setAgetnt(id: Int) {
        if (AndroidHelper.isNetworkAvailable(activity)) {
            callGetAgentDetailsAPI(id)
        } else {
            AndroidHelper.showToast(activity, getString(R.string.no_internet))
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

                    if (agentDetailsResponse.result && agentDetailsResponse.data != null) {
                        txtAgentName.setText(agentDetailsResponse.data.userName)
                        txtAgencyName.setText(agentDetailsResponse.data.agencyName)
                    }

                }, Response.ErrorListener { error ->

            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }

}
