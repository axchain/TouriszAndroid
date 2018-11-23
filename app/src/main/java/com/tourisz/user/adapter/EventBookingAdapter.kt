package com.tourisz.user.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.tourisz.R
import com.tourisz.api.URLS
import com.tourisz.api.response.EventData
import com.tourisz.user.view.fragment.EventBookingFragment
import com.tourisz.entity.HotelBooking
import com.tourisz.util.BookingStatus
import com.tourisz.util.helper.AndroidHelper

class EventBookingAdapter(private val context: Context, private val hotelBookingList: List<EventData>, private val eventBookingFragment: EventBookingFragment) : RecyclerView.Adapter<EventBookingAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtBook: TextView
        var linRoot: LinearLayout
        var imgHotel: ImageView
        var txtHotelDesc: TextView
        var txtTitle: TextView
        var txtDate: TextView

        init {

            txtBook = view.findViewById<View>(R.id.txtBook) as TextView
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtHotelDesc = view.findViewById<View>(R.id.txtHotelDesc) as TextView
            imgHotel = view.findViewById<View>(R.id.imgHotel) as ImageView
            txtTitle = view.findViewById<View>(R.id.txtTitle) as TextView
            txtDate = view.findViewById<View>(R.id.txtDate) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.event_booking_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hotelBooking = hotelBookingList[position]

        holder.txtTitle.setText(hotelBooking.eventTitle)
        holder.txtDate.setText(hotelBooking.eventDateTime)
        holder.txtHotelDesc.setText(hotelBooking.shortDescription)

        var names = AndroidHelper.getImageArray(hotelBooking.poster)
        if (names.isNotEmpty()) {

            Glide.with(context).load(URLS.newInstance().IMG_URL + names[0])
                    .placeholder(R.drawable.ic_eventplaceholder)
                    .error(R.drawable.ic_eventplaceholder)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgHotel)
        } else {
            holder.imgHotel.setImageResource(R.drawable.ic_eventplaceholder)
        }
        holder.txtBook.setOnClickListener {
            if (hotelBooking?.isAvailable.equals("1")) {

                if (AndroidHelper.getCurrentDate().before(AndroidHelper.getEventDate(hotelBooking.eventDateTime))) {
                    eventBookingFragment.onHotelListBookingClick(hotelBooking)
                } else {
                    AndroidHelper.showToast(context, context.getString(R.string.event_not_available))
                }

                /*   if (hotelBooking.isBooked == BookingStatus.NOT_BOOKED) {

                   }else{
                       AndroidHelper.showToast(context, context.getString(R.string.already_booked))

                   }*/

            } else {
                AndroidHelper.showToast(context, context.getString(R.string.event_not_available))
            }
        }

        holder.linRoot.setOnClickListener({ eventBookingFragment.onHotelListItemClick(hotelBooking) })
    }

    override fun getItemCount(): Int {
        return hotelBookingList.size
    }
}