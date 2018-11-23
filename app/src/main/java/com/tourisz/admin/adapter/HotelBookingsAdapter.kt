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
import com.tourisz.admin.view.fragment.HotelBookingsFragment
import com.tourisz.admin.view.fragment.UsersFragment
import com.tourisz.api.URLS
import com.tourisz.api.response.HotelBookingData
import com.tourisz.entity.HotelBooking
import com.tourisz.util.helper.AndroidHelper

class HotelBookingsAdapter(private val userList: List<HotelBookingData>, private val hotelBookingsFragment: HotelBookingsFragment) : RecyclerView.Adapter<HotelBookingsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linRoot: LinearLayout
        var txtHotelName: TextView
        var txtCustomerName: TextView
        var imgHotel: ImageView
        var txtDurationDate: TextView
        var txtNoPerson: TextView
        //var txtBookingDate: TextView

        init {
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtHotelName = view.findViewById<View>(R.id.txtHotelName) as TextView
            txtCustomerName = view.findViewById<View>(R.id.txtCustomerName) as TextView
            imgHotel = view.findViewById<View>(R.id.imgHotel) as ImageView
            txtDurationDate = view.findViewById<View>(R.id.txtDurationDate) as TextView
            txtNoPerson = view.findViewById<View>(R.id.txtNoPerson) as TextView
           // txtBookingDate = view.findViewById<View>(R.id.txtBookingDate) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.hotel_bookings_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hotelBooking = userList[position]
        holder.txtCustomerName.setText(hotelBooking.customerName)
        holder.txtHotelName.setText(hotelBooking.hotelName)
        holder.txtNoPerson.setText(hotelBooking.numberOfPersons)
        holder.txtDurationDate.setText("From "+hotelBooking.bookingFromDate + " To "+hotelBooking.bookingToDate)
        var names = AndroidHelper.getImageArray(hotelBooking.poster)
        if(names.isNotEmpty()){

            Glide.with(hotelBookingsFragment.activity).load(URLS.newInstance().IMG_URL + names[0])
                    .placeholder(R.drawable.ic_hotelplaceholder)
                    .error(R.drawable.ic_hotelplaceholder)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgHotel)
        }

        holder.linRoot.setOnClickListener({ hotelBookingsFragment.onHotelBookingListItemClick(hotelBooking) })
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}