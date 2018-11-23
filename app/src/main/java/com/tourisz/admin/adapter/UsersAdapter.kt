package com.tourisz.admin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tourisz.R
import com.tourisz.admin.view.fragment.UsersFragment
import com.tourisz.api.response.UsersData
import com.tourisz.entity.HotelBooking

class UsersAdapter(private val userList: List<UsersData>, private val usersFragment: UsersFragment) : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.users_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val usersData = userList[position]
        holder.txtName.setText(usersData.userName)
        holder.txtEmailId.setText(usersData.email)
        holder.txtContactNumber.setText(usersData.contactNumber)

        holder.linRoot.setOnClickListener({ usersFragment.onUserListItemClick(usersData) })
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}