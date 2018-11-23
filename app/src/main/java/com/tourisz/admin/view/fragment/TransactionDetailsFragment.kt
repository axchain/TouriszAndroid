package com.tourisz.admin.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.tourisz.R
import com.tourisz.api.request.Object
import com.tourisz.api.response.EventBookingData
import com.tourisz.api.response.HotelBookingData
import com.tourisz.util.BookingTypes

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TransactionDetailsFragment : Fragment() {
    private var param2: String? = null
    private var transaction: Object? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                transaction = it.getSerializable(ARG_PARAM1) as Object
                param2 = it.getString(ARG_PARAM2)
            }

        }
    }

    private lateinit var mView: View
    private lateinit var imgHotel: ImageView
    private lateinit var txtTitle: TextView
    private lateinit var txtDate: TextView
    private lateinit var linRoom: LinearLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_transaction_details, container, false)
        initIds()

        if (transaction!=null){
            if (transaction is EventBookingData){
              setEventData(transaction as EventBookingData)
            }else  if (transaction is HotelBookingData){
              setHotelData(transaction as HotelBookingData)
            }
        }

        return mView
    }

    private fun initIds() {
        txtDate = mView.findViewById(R.id.txtDate)
        imgHotel = mView.findViewById(R.id.imgHotel)
        txtTitle = mView.findViewById(R.id.txtTitle)
        linRoom = mView.findViewById(R.id.linRoom)

    }

    fun setEventData(eventBookingData: EventBookingData){
        linRoom.visibility = View.GONE
        txtDate.text = eventBookingData.eventDateTime
        txtTitle.text = eventBookingData.eventTitle
        imgHotel.setImageResource(R.drawable.ic_event_available)
    }

    fun setHotelData(hotelBookingData: HotelBookingData){
        linRoom.visibility = View.VISIBLE
        txtDate.text = hotelBookingData.bookingFromDate
        txtTitle.text= hotelBookingData.hotelName
        imgHotel.setImageResource(R.drawable.ic_hotel)
    }


    companion object {
        @JvmStatic
        fun newInstance(transaction: Object, param2: String) =
                TransactionDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, transaction)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


}
