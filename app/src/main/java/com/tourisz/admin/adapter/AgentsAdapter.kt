package com.tourisz.admin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tourisz.R
import com.tourisz.admin.view.fragment.AgentsFragment
import com.tourisz.api.response.AgentsData
import com.tourisz.entity.HotelBooking

class AgentsAdapter(private val userList: List<AgentsData>, private val agentsFragment: AgentsFragment) : RecyclerView.Adapter<AgentsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.agents_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val agentsData = userList[position]
        holder.txtName.setText(agentsData.userName)
        holder.txtAgencyName.setText(agentsData.agencyName)
        holder.txtContactNumber.setText(agentsData.contactNumber)
        holder.txtEmailId.setText(agentsData.email)

        holder.linRoot.setOnClickListener({ agentsFragment.onAgentsListItemClick(agentsData) })
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}