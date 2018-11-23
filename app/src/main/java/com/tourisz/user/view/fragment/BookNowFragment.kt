package com.tourisz.user.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.Response
import com.tourisz.Application

import com.tourisz.R
import com.tourisz.api.URLS
import com.tourisz.api.request.BookEventRequest
import com.tourisz.api.request.BookHotelRequest
import com.tourisz.api.request.Object
import com.tourisz.api.response.*
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.entity.HotelBooking
import com.tourisz.util.BookingTypes
import com.tourisz.util.Constant
import com.tourisz.util.helper.AndroidHelper
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BookNowFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var obj: Object? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

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
    private lateinit var btnFromDate: Button
    private lateinit var btnToDate: Button
    private lateinit var btnNoPerson: Button
    private lateinit var btnNoRoom: Button
    private lateinit var btnBook: Button
    private lateinit var btnRoomType: Button
    private var bookHotelRequest = BookHotelRequest()
    private var bookEventRequest = BookEventRequest()
    private lateinit var userData: UserData
    private var fromDate = ""
    private var toDate = ""
    private var noOfPerson = 0
    private var noOfRoom = 0
    private var roomType = 0
    private var dateFrom: Date? = null
    private var dateTo: Date? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_book_now, container, false)
        initIds()
        userData = AndroidHelper.getUser(activity)

        if (obj is EventData) {
            btnFromDate.visibility = View.GONE
            btnToDate.visibility = View.GONE
            btnNoRoom.visibility = View.GONE
            btnRoomType.visibility = View.GONE
            setEventData(obj as EventData)

        } else if (obj is HotelData) {
            btnFromDate.visibility = View.VISIBLE
            btnToDate.visibility = View.VISIBLE
            btnNoRoom.visibility = View.VISIBLE
            btnRoomType.visibility = View.VISIBLE
            setHotelData(obj as HotelData)

        }
        return mView
    }

    fun setEventData(eventData: EventData?) {
        bookEventRequest.customerName = userData.userName
        bookEventRequest.userId = userData.id.toInt()
        bookEventRequest.eventId = eventData?.id?.toInt()
        bookEventRequest.bookingCharges = eventData?.bookingCharges?.toInt()
        bookEventRequest.agentId = eventData?.agentId?.toInt()

    }

    fun setHotelData(hotelData: HotelData?) {
        bookHotelRequest.customerName = userData.userName
        bookHotelRequest.userId = userData.id.toInt()
        bookHotelRequest.hotelId = hotelData?.id?.toInt()
        bookHotelRequest.bookingCharges = hotelData?.bookingCharges?.toInt()
        bookHotelRequest.agentId = hotelData?.agentId?.toInt()
    }


    private fun initIds() {
        btnFromDate = mView.findViewById(R.id.btnFromDate)
        btnToDate = mView.findViewById(R.id.btnToDate)
        btnNoPerson = mView.findViewById(R.id.btnNoPerson)
        btnNoRoom = mView.findViewById(R.id.btnNoRoom)
        btnBook = mView.findViewById(R.id.btnBook)
        btnRoomType = mView.findViewById(R.id.btnRoomType)

        setListener(btnFromDate, btnToDate, btnNoPerson, btnBook, btnNoRoom, btnRoomType)
    }

    fun setListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnFromDate -> onBookNowFromDateClicked()

            R.id.btnToDate -> onBookNowToDateClicked()

            R.id.btnNoPerson -> onBookNumPerClicked()

            R.id.btnNoRoom -> onBookNumRoomClicked()

            R.id.btnBook -> setBookData()

            R.id.btnRoomType -> listener?.onBookNumRoomTypeClicked()
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onBookNowFromDateClicked() {
        listener?.onBookNowFromDateClicked()
    }

    fun onBookNowToDateClicked() {
        listener?.onBookNowToDateClicked()
    }


    fun setBookData() {

        if (obj is EventData) {
            if (noOfPerson == 0) {
                AndroidHelper.showToast(activity, getString(R.string.select_num_person))
                return
            }

            bookEventRequest.numberOfPersons = noOfPerson
            //checkEventIsBooked()
            onBookNowBookClicked(obj, bookEventRequest)



        } else if (obj is HotelData) {

            if (fromDate.isEmpty()) {
                AndroidHelper.showToast(activity, getString(R.string.select_from_date))
                return
            } else if (toDate.isEmpty()) {
                AndroidHelper.showToast(activity, getString(R.string.select_to_date))
                return
            } else if (noOfPerson == 0) {
                AndroidHelper.showToast(activity, getString(R.string.select_num_person))
                return
            } else if (roomType == 0) {
                AndroidHelper.showToast(activity, getString(R.string.select_room_type))
                return
            } else if (noOfRoom == 0) {
                AndroidHelper.showToast(activity, getString(R.string.select_num_room))
                return
            }

            bookHotelRequest.numberOfPersons = noOfPerson
            bookHotelRequest.numberOfRooms = noOfRoom
            bookHotelRequest.bookingFromDate = fromDate
            bookHotelRequest.bookingToDate = toDate
            bookHotelRequest.roomType = roomType

            onBookNowBookClicked(obj, bookHotelRequest)
        }

    }

    var total = 0
    fun onBookNowBookClicked(dataObj: Object?, bookObj: Object?) {

        var daysCount = 1
        if (dateFrom != null && dateTo != null && dateFrom != dateTo) {
            daysCount = AndroidHelper.getDayDifference(dateFrom, dateTo).toInt()
            Log.e("daysCount", daysCount.toString())

        }
        if (bookObj is BookHotelRequest && dataObj is HotelData) {

            total = calculateTotal(bookObj.bookingCharges, daysCount, noOfRoom, noOfPerson)
            Log.e("total", total.toString())
            bookObj.total = total.toLong()
        } else if (bookObj is BookEventRequest && dataObj is EventData) {

            total = calculateTotal(bookObj.bookingCharges, daysCount, 1, noOfPerson)
            Log.e("total", total.toString())
            bookObj.total = total.toLong()
        }
        listener?.onBookNowBookClicked(dataObj, bookObj)
    }

    fun onBookNumPerClicked() {
        listener?.onBookNumPerClicked()
    }

    fun onBookNumRoomClicked() {
        listener?.onBookNumRoomClicked()
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
        fun onBookNowFromDateClicked()

        fun onBookNowToDateClicked()

        fun onBookNowBookClicked(dataObj: Object?, bookObj: Object?)

        fun onBookNumPerClicked()

        fun onBookNumRoomClicked()

        fun onBookNumRoomTypeClicked()
    }

    companion object {
        @JvmStatic
        fun newInstance(obj: Object, param2: String) =
                BookNowFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, obj)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun setFromDate(fromDate: String) {
        this.fromDate = fromDate
        try {
            dateFrom = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).parse(fromDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        btnFromDate.text = "From " + fromDate
    }

    fun setToDate(toDate: String) {
        this.toDate = toDate
        try {
            dateTo = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).parse(toDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        btnToDate.text = "To " + toDate
    }

    fun setNoPer(num: String) {
        noOfPerson = num.toInt()
        btnNoPerson.text = "No. of Persons: " + num
    }

    fun setNoRoom(num: String) {
        noOfRoom = num.toInt()
        btnNoRoom.text = "No. of Rooms: " + num
    }

    fun setRoomType(type: String) {
        roomType = AndroidHelper.getRoomTypeId(type)
        btnRoomType.text = type
    }


    fun checkEventIsBooked() {
        val jsonObj = JSONObject()
        jsonObj.put("userId", userData.id)
        jsonObj.put("eventId", bookEventRequest.eventId)
        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().CHECK_EVENT_BOOKED(), jsonObj,
                Response.Listener<JSONObject> { response ->

                    Log.e("Response", response.toString())

                    if (response.getBoolean("result")) {
                        AndroidHelper.showToast(activity, response.getString("response"))
                    } else {
                        onBookNowBookClicked(obj, bookEventRequest)

                    }

                }, Response.ErrorListener { error ->

            AndroidHelper.showToast(activity, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)
    }


    fun calculateTotal(rate: Int, noOfDay: Int, noOfRoom: Int, noOfPerson: Int): Int {
        Log.e("calc", "$rate * $noOfDay * $noOfRoom * $noOfPerson")

        return rate * noOfDay * noOfRoom * noOfPerson
    }
}
