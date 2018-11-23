package com.tourisz.user.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager

import com.tourisz.R
import com.tourisz.api.request.Object
import com.tourisz.api.response.EventBookingData
import com.tourisz.api.response.HotelBookingData
import com.tourisz.user.adapter.SlidingImageAdapter
import com.tourisz.util.helper.AndroidHelper
import me.relex.circleindicator.CircleIndicator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MyBookingDetailsFragment : Fragment(), View.OnClickListener {
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
    private lateinit var btnImg: Button
    private lateinit var btnCmt: Button
    private lateinit var viewPager: AutoScrollViewPager
    private lateinit var indicator: CircleIndicator
    private lateinit var txtDate: TextView
    private lateinit var linPerson: LinearLayout
    private lateinit var linRoomType: LinearLayout
    private lateinit var linTotal: LinearLayout
    private lateinit var txtTotal: TextView

    private lateinit var linRoom: LinearLayout
    private lateinit var txtTitle: TextView
    private lateinit var txtDesc: TextView
    private lateinit var txtRate: TextView
    private lateinit var txtWeblink: TextView
    private lateinit var txtPersonName: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtAddress: TextView
    private lateinit var txtCity: TextView
    private lateinit var txtCountry: TextView
    private lateinit var txtBookingDate: TextView
    private lateinit var txtRoomType: TextView
    private lateinit var txtNoPerson: TextView
    private lateinit var txtNoRoom: TextView
    private var attachment = ""
    private var comment = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_booking_details, container, false)
        initIds()
        viewPager.startAutoScroll()

        if (obj != null) {
            if (obj is HotelBookingData) {
                setHotelData(obj as HotelBookingData)
            } else if (obj is EventBookingData) {
                linPerson.visibility = View.GONE
                linRoom.visibility = View.GONE
                linRoomType.visibility = View.GONE

                setEventData(obj as EventBookingData)
            }
        }

        return mView
    }

    private fun initIds() {
        viewPager = mView.findViewById(R.id.viewPager)
        indicator = mView.findViewById(R.id.indicator)
        btnImg = mView.findViewById(R.id.btnImg)
        btnCmt = mView.findViewById(R.id.btnCmt)
        txtRoomType = mView.findViewById(R.id.txtRoomType)
        txtBookingDate = mView.findViewById(R.id.txtBookingDate)
        txtNoPerson = mView.findViewById(R.id.txtNoPerson)
        txtNoRoom = mView.findViewById(R.id.txtNoRoom)
        linRoomType = mView.findViewById(R.id.linRoomType)
        txtDate = mView.findViewById(R.id.txtDate)
        linPerson = mView.findViewById(R.id.linPerson)
        linRoom = mView.findViewById(R.id.linRoom)
        txtWeblink = mView.findViewById(R.id.txtWeblink)
        txtPersonName = mView.findViewById(R.id.txtPersonName)
        txtPhone = mView.findViewById(R.id.txtPhone)
        txtAddress = mView.findViewById(R.id.txtAddress)
        txtCity = mView.findViewById(R.id.txtCity)
        txtCountry = mView.findViewById(R.id.txtCountry)
        txtRate = mView.findViewById(R.id.txtRate)
        txtTitle = mView.findViewById(R.id.txtTitle)
        txtDesc = mView.findViewById(R.id.txtDesc)
        txtTotal = mView.findViewById(R.id.txtTotal)
        linTotal = mView.findViewById(R.id.linTotal)

        setListener(btnCmt, btnImg)
    }


    fun setHotelData(hotelBookingData: HotelBookingData) {
        if (hotelBookingData.poster != null) {
            setupViewPager(viewPager, hotelBookingData.poster)
        }
        txtDate.text = "From " + hotelBookingData.bookingFromDate + " To " + hotelBookingData.bookingToDate
        txtTitle.setText(hotelBookingData.hotelName)
        txtRate.text = "Rate: " + hotelBookingData.bookingCharges
        txtDesc.setText(hotelBookingData.description)
        txtWeblink.setText(hotelBookingData.webLink)
        txtPersonName.setText(hotelBookingData.contactPerson)
        txtPhone.setText(hotelBookingData.contactNumber)
        txtAddress.setText(hotelBookingData.address)
        txtCity.setText(hotelBookingData.city)
        txtCountry.setText(hotelBookingData.country)
        txtBookingDate.setText(hotelBookingData.bookedOn)
        txtRoomType.setText(AndroidHelper.getRoomType(hotelBookingData.roomType.toInt()))
        txtNoPerson.setText(hotelBookingData.numberOfPersons)
        txtNoRoom.setText(hotelBookingData.numberOfRooms)
        txtTotal.text = "Total: " + hotelBookingData.totalBookingAmount

        if (hotelBookingData.attachment != null) {
            attachment = hotelBookingData.attachment
        }
        if (hotelBookingData.attachmentDescription != null) {
            comment = hotelBookingData.attachmentDescription
        }

    }


    fun setEventData(eventBookingData: EventBookingData) {
        if (eventBookingData.poster != null) {
            setupViewPager(viewPager, eventBookingData.poster)
        }
        txtDate.text = eventBookingData.eventDateTime
        txtTitle.setText(eventBookingData.eventTitle)
        txtRate.text = "Rate: " + eventBookingData.bookingCharges
        txtDesc.setText(eventBookingData.description)
        txtWeblink.setText(eventBookingData.webLink)
        txtPersonName.setText(eventBookingData.contactPerson)
        txtPhone.setText(eventBookingData.contactNumber)
        txtAddress.setText(eventBookingData.address)
        txtCity.setText(eventBookingData.city)
        txtCountry.setText(eventBookingData.country)
        txtBookingDate.setText(eventBookingData.bookedOn)
        txtNoPerson.setText(eventBookingData.numberOfPersons)
        txtTotal.text = "Total: " + eventBookingData.totalBookingAmount

        if (eventBookingData.attachment != null) {
            attachment = eventBookingData.attachment
        }
        if (eventBookingData.attachmentDescription != null) {
            comment = eventBookingData.attachmentDescription
        }
    }


    fun setListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    private fun setupViewPager(viewPager: ViewPager, nameslist: String) {
        val names = AndroidHelper.getImageArray(nameslist)
        val adapter = SlidingImageAdapter(activity, names)
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnImg -> onBookedDetailsImageClicked()
            R.id.btnCmt -> onBookedDetailsCommentClicked()

        }
    }

    fun onBookedDetailsImageClicked() {
        listener?.onBookedDetailsImageClicked(attachment)

    }

    fun onBookedDetailsCommentClicked() {
        listener?.onBookedDetailsCommentClicked(comment)
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

        fun onBookedDetailsImageClicked(name: String)

        fun onBookedDetailsCommentClicked(data: String)

    }

    companion object {

        @JvmStatic
        fun newInstance(obj: Object, param2: String) =
                MyBookingDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, obj)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


}
