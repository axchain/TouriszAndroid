package com.tourisz.user.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager

import com.tourisz.R
import com.tourisz.api.request.Object
import com.tourisz.api.response.EventData
import com.tourisz.api.response.HotelData
import com.tourisz.entity.HotelBooking
import com.tourisz.user.adapter.SlidingImageAdapter
import com.tourisz.util.BookingStatus
import com.tourisz.util.BookingTypes
import com.tourisz.util.helper.AndroidHelper
import me.relex.circleindicator.CircleIndicator

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class EventBookingDetailsFragment : Fragment() {
    private var eventData: EventData? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                eventData = it.getSerializable(ARG_PARAM1) as EventData?
                param2 = it.getString(ARG_PARAM2)
            }
        }
    }

    private lateinit var mView: View
    private lateinit var linBook: LinearLayout
    private lateinit var viewPager: AutoScrollViewPager
    private lateinit var indicator: CircleIndicator
    private lateinit var txtTitle: TextView
    private lateinit var txtDesc: TextView
    private lateinit var txtRate: TextView
    private lateinit var txtWeblink: TextView
    private lateinit var txtPersonName: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtAddress: TextView
    private lateinit var txtCity: TextView
    private lateinit var txtCountry: TextView
    private lateinit var txtDate: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_event_booking_details, container, false)
        initIds()
        setEventData(eventData)

        linBook.setOnClickListener{
            if (eventData!=null) {
                if (eventData?.isAvailable.equals("1")) {

                    if (AndroidHelper.getCurrentDate().before(AndroidHelper.getEventDate(eventData?.eventDateTime))) {
                        onBookingClicked(eventData!!)
                    } else {
                        AndroidHelper.showToast(context, getString(R.string.event_not_available))
                    }


                   /* if (eventData?.isBooked == BookingStatus.NOT_BOOKED) {
                    } else {
                        AndroidHelper.showToast(activity, getString(R.string.already_booked))
                    }*/
                }
            }else{
                AndroidHelper.showToast(activity, getString(R.string.event_not_available))
            }
        }
        viewPager.startAutoScroll()
        return mView
    }


    private fun initIds() {
        linBook = mView.findViewById(R.id.linBook)
        viewPager = mView.findViewById(R.id.viewPager)
        indicator = mView.findViewById(R.id.indicator)
        txtWeblink = mView.findViewById(R.id.txtWeblink)
        txtPersonName = mView.findViewById(R.id.txtPersonName)
        txtPhone = mView.findViewById(R.id.txtPhone)
        txtAddress = mView.findViewById(R.id.txtAddress)
        txtCity = mView.findViewById(R.id.txtCity)
        txtCountry = mView.findViewById(R.id.txtCountry)
        txtRate = mView.findViewById(R.id.txtRate)
        txtTitle = mView.findViewById(R.id.txtTitle)
        txtDesc = mView.findViewById(R.id.txtDesc)
        txtDate = mView.findViewById(R.id.txtDate)
    }


    fun setEventData(eventData: EventData?) {
        if (eventData?.poster != null) {
            setupViewPager(viewPager, eventData?.poster)
        }
        txtTitle.setText(eventData?.eventTitle)
        txtDate.setText(eventData?.eventDateTime)
        txtDesc.setText(eventData?.description)
        txtWeblink.setText(eventData?.webLink)
        txtPersonName.setText(eventData?.contactPerson)
        txtPhone.setText(eventData?.contactNumber)
        txtAddress.setText(eventData?.address)
        txtCity.setText(eventData?.city)
        txtCountry.setText(eventData?.country)
        txtRate.setText(eventData?.bookingCharges)
    }

    private fun setupViewPager(viewPager: ViewPager, nameslist: String?) {
        val names = AndroidHelper.getImageArray(nameslist)
        val adapter = SlidingImageAdapter(activity, names)
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)
    }



    fun onBookingClicked(obj: Object) {
        listener?.onBookingClicked(obj, false)
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
        fun onBookingClicked(obj: Object, forHotel: Boolean)
    }

    companion object {
        @JvmStatic
        fun newInstance(eventData: EventData, param2: String) =
                EventBookingDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, eventData)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


}
