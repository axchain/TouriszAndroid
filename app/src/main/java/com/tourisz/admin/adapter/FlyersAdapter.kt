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
import com.tourisz.admin.view.fragment.FlyersFragment
import com.tourisz.api.URLS
import com.tourisz.api.response.FlyerData
import com.tourisz.util.helper.AndroidHelper

class FlyersAdapter(private val hotelBookingList: List<FlyerData>, private val flyersFragment: FlyersFragment) : RecyclerView.Adapter<FlyersAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linRoot: LinearLayout
        var txtTitle: TextView
        var txtPosition: TextView
        var txtStatus: TextView
        var txtEdit: TextView
        var txtDelete: TextView
        var img: ImageView

        init {
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtPosition = view.findViewById<View>(R.id.txtPosition) as TextView
            txtStatus = view.findViewById<View>(R.id.txtStatus) as TextView
            txtTitle = view.findViewById<View>(R.id.txtTitle) as TextView
            txtEdit = view.findViewById<View>(R.id.txtEdit) as TextView
            txtDelete = view.findViewById<View>(R.id.txtDelete) as TextView
            img = view.findViewById<View>(R.id.img) as ImageView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.flyer_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hotelBooking = hotelBookingList[position]
        holder.txtTitle.text = hotelBooking.title
        holder.txtPosition.text = hotelBooking.flyerPosition
        holder.txtStatus.text = AndroidHelper.getStatus(hotelBooking.status)

        Glide.with(flyersFragment.activity).load(URLS.newInstance().FLYERSIMG_URL + hotelBooking.poster)
                .placeholder(R.drawable.ic_hotelplaceholder)
                .error(R.drawable.ic_hotelplaceholder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img)

        holder.txtEdit.setOnClickListener({ flyersFragment.onFlyerListEditClick(hotelBooking) })
        holder.txtDelete.setOnClickListener({ flyersFragment.onFlyerListDeleteClick(hotelBooking) })
        holder.linRoot.setOnClickListener({ flyersFragment.onFlyerListItemClick(hotelBooking) })
    }

    override fun getItemCount(): Int {
        return hotelBookingList.size
    }
}