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
import com.tourisz.api.response.*
import com.tourisz.util.SearchTypes
import com.tourisz.util.helper.AndroidHelper
import com.tourisz.util.listener.SearchClickListener


class MultiViewTypeAdapter(private val context: Context, private val objects: List<Object>, private val serchType: Int, private val searchClickListener: SearchClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class UserHotelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var txtBook: TextView
        internal var linRoot: LinearLayout
        internal var imgHotel: ImageView
        internal var txtHotelDesc: TextView
        internal var txtTitle: TextView

        init {
            this.txtBook = itemView.findViewById(R.id.txtBook)
            this.linRoot = itemView.findViewById(R.id.linRoot)
            this.imgHotel = itemView.findViewById(R.id.imgHotel)
            this.txtHotelDesc = itemView.findViewById(R.id.txtHotelDesc)
            this.txtTitle = itemView.findViewById(R.id.txtTitle)
        }
    }

    class UserBookingHolder(view: View) : RecyclerView.ViewHolder(view) {

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


    class UserEventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var txtBook: TextView
        internal var linRoot: LinearLayout
        internal var imgHotel: ImageView
        internal var txtHotelDesc: TextView
        internal var txtTitle: TextView
        internal var txtDate: TextView

        init {
            this.txtBook = itemView.findViewById(R.id.txtBook)
            this.linRoot = itemView.findViewById(R.id.linRoot)
            this.imgHotel = itemView.findViewById(R.id.imgHotel)
            this.txtHotelDesc = itemView.findViewById(R.id.txtHotelDesc)
            this.txtTitle = itemView.findViewById(R.id.txtTitle)
            this.txtDate = itemView.findViewById(R.id.txtDate)
        }
    }

    class AgentHotelHolder(view: View) : RecyclerView.ViewHolder(view) {

        var linRoot: LinearLayout
        var txtHotelDesc: TextView
        var txtTitle: TextView
        var txtCountry: TextView
        var txtStatus: TextView
        var txtAvailability: TextView
        var imgHotel: ImageView
        init {
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtHotelDesc = view.findViewById<View>(R.id.txtHotelDesc) as TextView
            txtCountry = view.findViewById<View>(R.id.txtCountry) as TextView
            txtStatus = view.findViewById<View>(R.id.txtStatus) as TextView
            txtTitle = view.findViewById<View>(R.id.txtTitle) as TextView
            txtAvailability = view.findViewById<View>(R.id.txtAvailability) as TextView
            imgHotel = view.findViewById<View>(R.id.imgHotel) as ImageView
        }
    }


    class AgentEventHolder(view: View) : RecyclerView.ViewHolder(view) {

        var linRoot: LinearLayout
        var txtHotelDesc: TextView
        var txtTitle: TextView
        var txtCountry: TextView
        var txtStatus: TextView
        var txtAvailability: TextView
        var imgHotel: ImageView
        init {
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtHotelDesc = view.findViewById<View>(R.id.txtHotelDesc) as TextView
            txtCountry = view.findViewById<View>(R.id.txtCountry) as TextView
            txtStatus = view.findViewById<View>(R.id.txtStatus) as TextView
            txtTitle = view.findViewById<View>(R.id.txtTitle) as TextView
            txtAvailability = view.findViewById<View>(R.id.txtAvailability) as TextView
            imgHotel = view.findViewById<View>(R.id.imgHotel) as ImageView
        }
    }

    class UsersHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linRoot: LinearLayout
        var txtName: TextView
        var txtEmailId: TextView
        var txtContactNumber: TextView
        var imgUser: ImageView


        init {
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtName = view.findViewById<View>(R.id.txtName) as TextView
            txtEmailId = view.findViewById<View>(R.id.txtEmailId) as TextView
            txtContactNumber = view.findViewById<View>(R.id.txtContactNumber) as TextView
            imgUser = view.findViewById<View>(R.id.imgUser) as ImageView

        }
    }

    class AgentsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linRoot: LinearLayout
        var txtName: TextView
        var txtEmailId: TextView
        var txtContactNumber: TextView
        var txtAgencyName: TextView
        var imgUser: ImageView


        init {
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtName = view.findViewById<View>(R.id.txtName) as TextView
            txtEmailId = view.findViewById<View>(R.id.txtEmailId) as TextView
            txtContactNumber = view.findViewById<View>(R.id.txtContactNumber) as TextView
            txtAgencyName = view.findViewById<View>(R.id.txtAgencyName) as TextView
            imgUser = view.findViewById<View>(R.id.imgUser) as ImageView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder = when (serchType) {
            SearchTypes.USER_HOTELS -> UserHotelHolder(LayoutInflater.from(parent.context).inflate(R.layout.hotel_booking_list_item, parent, false)
            )
            SearchTypes.USER_BOOKED_HOTELS -> UserBookingHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_booked_list_item, parent, false)
            )
            SearchTypes.USER_BOOKED_EVENTS -> UserBookingHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_booked_list_item, parent, false)
            )
            SearchTypes.USER_EVENTS -> UserEventHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_booking_list_item, parent, false)
            )
            SearchTypes.AGNET_HOTELS -> AgentHotelHolder(LayoutInflater.from(parent.context).inflate(R.layout.agent_hotel_list_item, parent, false)
            )
            SearchTypes.AGNET_EVENTS -> AgentEventHolder(LayoutInflater.from(parent.context).inflate(R.layout.agent_event_list_item, parent, false)
            )
            SearchTypes.ADMIN_HOTELS -> AgentHotelHolder(LayoutInflater.from(parent.context).inflate(R.layout.agent_hotel_list_item, parent, false)
            )
            SearchTypes.ADMIN_EVENTS -> AgentEventHolder(LayoutInflater.from(parent.context).inflate(R.layout.agent_event_list_item, parent, false)
            )
            SearchTypes.ADMIN_USERS -> UsersHolder(LayoutInflater.from(parent.context).inflate(R.layout.users_list_item, parent, false)
            )
            SearchTypes.ADMIN_AGNETS -> AgentsHolder(LayoutInflater.from(parent.context).inflate(R.layout.agents_list_item, parent, false)
            )
            else -> UserHotelHolder(LayoutInflater.from(parent.context).inflate(R.layout.hotel_booking_list_item, parent, false))

        }
        return viewHolder
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserHotelHolder) {
            val hotelBooking = objects[position] as HotelData
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
            holder.txtBook.setOnClickListener{ searchClickListener.onBookItemClick(position) }
            holder.linRoot.setOnClickListener{ searchClickListener.onItemClick(position) }

        }else if (holder is UserEventHolder){
            val hotelBooking = objects[position] as EventData

            holder.txtTitle.setText(hotelBooking.eventTitle)
            holder.txtDate.setText(hotelBooking.eventDateTime)
            holder.txtHotelDesc.setText(hotelBooking.shortDescription)

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
            holder.txtBook.setOnClickListener{ searchClickListener.onBookItemClick(position) }
            holder.linRoot.setOnClickListener{ searchClickListener.onItemClick(position) }

        } else if(holder is UserBookingHolder && objects[position] is HotelBookingData){
            val hotelBooking = objects[position] as HotelBookingData


                holder.txtTitle.text = hotelBooking.hotelName
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

            holder.linRoot.setOnClickListener{ searchClickListener.onItemClick(position) }

            } else if(holder is UserBookingHolder && objects[position] is EventBookingData) {
            val hotelBooking = objects[position] as EventBookingData

            holder.txtTitle.setText(hotelBooking.eventTitle)
                holder.txtBookingDate.setText(hotelBooking.createdOn)
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

            holder.linRoot.setOnClickListener{ searchClickListener.onItemClick(position) }

            } else if (holder is AgentHotelHolder){
            val hotelBooking = objects[position] as HotelData

            holder.txtTitle.setText(hotelBooking.hotelName)
            holder.txtHotelDesc.setText(hotelBooking.shortDescription)
            holder.txtCountry.setText(hotelBooking.country)
            holder.txtStatus.setText(AndroidHelper.getStatus(hotelBooking.status))
            holder.txtAvailability.setText(AndroidHelper.getAvailability(hotelBooking.isAvailable))

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

            holder.linRoot.setOnClickListener{ searchClickListener.onItemClick(position) }

        } else if (holder is AgentEventHolder){
            val hotelBooking = objects[position] as EventData

            holder.txtTitle.setText(hotelBooking.eventTitle)
            holder.txtHotelDesc.setText(hotelBooking.shortDescription)
            holder.txtCountry.setText(hotelBooking.country)
            holder.txtStatus.setText(AndroidHelper.getStatus(hotelBooking.status))
            holder.txtAvailability.setText(AndroidHelper.getAvailability(hotelBooking.isAvailable))

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

            holder.linRoot.setOnClickListener{ searchClickListener.onItemClick(position) }

        } else if (holder is UsersHolder){
            val usersData = objects[position] as UsersData

            holder.txtName.setText(usersData.userName)
            holder.txtEmailId.setText(usersData.email)
            holder.txtContactNumber.setText(usersData.contactNumber)

            holder.linRoot.setOnClickListener{ searchClickListener.onItemClick(position) }

        } else if (holder is AgentsHolder){
            val agentsData = objects[position] as AgentsData
            holder.txtName.setText(agentsData.userName)
            holder.txtAgencyName.setText(agentsData.agencyName)
            holder.txtContactNumber.setText(agentsData.contactNumber)
            holder.txtEmailId.setText(agentsData.email)

            holder.linRoot.setOnClickListener{ searchClickListener.onItemClick(position) }

        }


    }


    override fun getItemCount(): Int {
        return objects.size
    }
}
