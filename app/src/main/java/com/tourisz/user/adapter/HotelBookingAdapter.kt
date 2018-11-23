package com.tourisz.user.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.anychart.anychart.Availability
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.tourisz.R
import com.tourisz.api.URLS
import com.tourisz.api.response.HotelData
import com.tourisz.user.view.fragment.HotelBookingFragment
import com.tourisz.util.helper.AndroidHelper

class HotelBookingAdapter(private val context: Context, private val hotelBookingList: List<HotelData>, private val hotelBookingFragment: HotelBookingFragment) : RecyclerView.Adapter<HotelBookingAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtBook: TextView
        var linRoot: LinearLayout
        var imgHotel: ImageView
        var txtHotelDesc: TextView
        var txtTitle: TextView

        init {
            txtBook = view.findViewById<View>(R.id.txtBook) as TextView
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtHotelDesc = view.findViewById<View>(R.id.txtHotelDesc) as TextView
            imgHotel = view.findViewById<View>(R.id.imgHotel) as ImageView
            txtTitle = view.findViewById<View>(R.id.txtTitle) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.hotel_booking_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hotelBooking = hotelBookingList[position]
        holder.txtTitle.setText(hotelBooking.hotelName)
        holder.txtHotelDesc.setText(hotelBooking.shortDescription)

        var names = AndroidHelper.getImageArray(hotelBooking.poster)
        if(names.isNotEmpty()){

            Glide.with(context).load(URLS.newInstance().IMG_URL + names[0])
                    .placeholder(R.drawable.ic_hotelplaceholder)
                    .error(R.drawable.ic_hotelplaceholder)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgHotel)
        }else{
            holder.imgHotel.setImageResource(R.drawable.ic_hotelplaceholder)
        }
        holder.txtBook.setOnClickListener{
            if(hotelBooking.isAvailable.equals("1")) {
                hotelBookingFragment.onHotelListBookingClick(hotelBooking)
            }else{
                AndroidHelper.showToast(context, context.getString(R.string.hotel_not_available))
            }
        }

        holder.linRoot.setOnClickListener({ hotelBookingFragment.onHotelListItemClick(hotelBooking) })
    }

    override fun getItemCount(): Int {
        return hotelBookingList.size
    }
}