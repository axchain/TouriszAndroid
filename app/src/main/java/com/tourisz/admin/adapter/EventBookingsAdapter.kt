package com.tourisz.admin.adapter

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
import com.tourisz.admin.view.fragment.EventBookingsFragment
import com.tourisz.api.URLS
import com.tourisz.api.response.EventBookingData
import com.tourisz.util.helper.AndroidHelper

class EventBookingsAdapter(private val list: List<EventBookingData>, private val eventBookingsFragment: EventBookingsFragment) : RecyclerView.Adapter<EventBookingsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linRoot: LinearLayout
        var txtHotelName: TextView
        var txtCustomerName: TextView
        var imgHotel: ImageView
        var txtDurationDate: TextView
        var txtNoRoom: TextView
        var txtNoPerson: TextView


        init {
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtHotelName = view.findViewById<View>(R.id.txtHotelName) as TextView
            txtCustomerName = view.findViewById<View>(R.id.txtCustomerName) as TextView
            imgHotel = view.findViewById<View>(R.id.imgHotel) as ImageView
            txtDurationDate = view.findViewById<View>(R.id.txtDurationDate) as TextView
            txtNoRoom = view.findViewById<View>(R.id.txtNoRoom) as TextView
            txtNoPerson = view.findViewById<View>(R.id.txtNoPerson) as TextView

            //txtDurationDate.visibility = View.GONE
            txtNoRoom.visibility = View.GONE


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.hotel_bookings_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val eventBookingData = list[position]
        holder.txtCustomerName.setText(eventBookingData.customerName)
        holder.txtHotelName.setText(eventBookingData.eventTitle)
        holder.txtDurationDate.setText(eventBookingData.eventDateTime)
        holder.txtNoPerson.setText(eventBookingData.numberOfPersons)
        var names = AndroidHelper.getImageArray(eventBookingData.poster)
        if(names.isNotEmpty()){

            Glide.with(eventBookingsFragment.activity).load(URLS.newInstance().IMG_URL + names[0])
                    .placeholder(R.drawable.ic_hotelplaceholder)
                    .error(R.drawable.ic_hotelplaceholder)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgHotel)
        }


        holder.linRoot.setOnClickListener({ eventBookingsFragment.onEventBookingListItemClick(eventBookingData) })
    }

    override fun getItemCount(): Int {
        return list.size
    }
}