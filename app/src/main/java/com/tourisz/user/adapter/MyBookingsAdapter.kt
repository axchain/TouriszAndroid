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
import com.tourisz.api.request.Object
import com.tourisz.api.response.EventBookingData
import com.tourisz.api.response.BookedHotelData
import com.tourisz.api.response.HotelBookingData
import com.tourisz.util.helper.AndroidHelper
import com.tourisz.util.listener.BookingClickListener

class MyBookingsAdapter(private val context: Context, private val hotelBookingList: List<Object>, private val bookingClickListener: BookingClickListener) : RecyclerView.Adapter<MyBookingsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linRoot: LinearLayout
        var txtDurationDate: TextView
        var txtTitle: TextView
        var txtDesc: TextView
        var txtBookingDate: TextView
        var imgHotel: ImageView

        init {
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtDurationDate = view.findViewById<View>(R.id.txtDurationDate) as TextView
            txtDesc = view.findViewById<View>(R.id.txtDesc) as TextView
            txtBookingDate = view.findViewById<View>(R.id.txtBookingDate) as TextView
            txtTitle = view.findViewById<View>(R.id.txtTitle) as TextView
            imgHotel = view.findViewById<View>(R.id.imgHotel) as ImageView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_booked_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hotelBooking = hotelBookingList[position]

        if(hotelBooking is HotelBookingData) {
            holder.txtTitle.setText(hotelBooking.hotelName)
            holder.txtBookingDate.setText(hotelBooking.bookedOn)
            holder.txtDurationDate.setText("From "+hotelBooking.bookingFromDate + " To "+hotelBooking.bookingToDate)
            holder.txtDesc.setText(hotelBooking.shortDescription)
            var names = AndroidHelper.getImageArray(hotelBooking.poster)
            if(names.isNotEmpty()){

                Glide.with(context).load(URLS.newInstance().IMG_URL + names[0])
                        .placeholder(R.drawable.ic_hotelplaceholder)
                        .error(R.drawable.ic_hotelplaceholder)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgHotel)
            }


        } else if(hotelBooking is EventBookingData) {
            holder.txtTitle.setText(hotelBooking.eventTitle)
            holder.txtBookingDate.setText(hotelBooking.bookedOn)
            holder.txtDurationDate.setText(hotelBooking.eventDateTime)
            holder.txtDesc.setText(hotelBooking.shortDescription)
            var names = AndroidHelper.getImageArray(hotelBooking.poster)
            if(names.isNotEmpty()){

                Glide.with(context).load(URLS.newInstance().IMG_URL + names[0])
                        .placeholder(R.drawable.ic_eventplaceholder)
                        .error(R.drawable.ic_eventplaceholder)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgHotel)
            }else{
                holder.imgHotel.setImageResource(R.drawable.ic_eventplaceholder)
            }


        }

        holder.linRoot.setOnClickListener({ bookingClickListener?.onItemClick(hotelBooking) })

    }

    override fun getItemCount(): Int {
        return hotelBookingList.size
    }
}