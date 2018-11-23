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
import com.tourisz.util.BookingTypes
import com.tourisz.util.helper.AndroidHelper
import me.relex.circleindicator.CircleIndicator

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HotelBookingDetailsFragment : Fragment() {
    private var hotelData: HotelData? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                hotelData = it.getSerializable(ARG_PARAM1) as HotelData?
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_hotel_booking_details, container, false)

        initIds()
        viewPager.startAutoScroll()
        setHotelData(hotelData)

        linBook.setOnClickListener {
            if (hotelData?.isAvailable.equals("1")) {
                if (hotelData != null) {
                    onBookingClicked(hotelData!!)
                }
            } else {
                AndroidHelper.showToast(activity, getString(R.string.hotel_not_available))
            }
        }
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
    }


    fun onBookingClicked(obj: Object) {
        listener?.onBookingClicked(obj, true)
    }


    fun setHotelData(hotelData: HotelData?) {
        if (hotelData?.poster != null) {
            setupViewPager(viewPager, hotelData?.poster)
        }
        txtTitle.setText(hotelData?.hotelName)
        txtRate.setText(hotelData?.bookingCharges)
        txtDesc.setText(hotelData?.description)
        txtWeblink.setText(hotelData?.webLink)
        txtPersonName.setText(hotelData?.contactPerson)
        txtPhone.setText(hotelData?.contactNumber)
        txtAddress.setText(hotelData?.address)
        txtCity.setText(hotelData?.city)
        txtCountry.setText(hotelData?.country)

    }


    private fun setupViewPager(viewPager: ViewPager, nameslist: String?) {
        val names = AndroidHelper.getImageArray(nameslist)
        val adapter = SlidingImageAdapter(activity, names)
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)
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
        fun newInstance(hotelData: HotelData, param2: String) =
                HotelBookingDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, hotelData)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


}
