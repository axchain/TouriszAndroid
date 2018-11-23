package com.tourisz.admin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.tourisz.R
import com.tourisz.api.request.Object
import com.tourisz.api.response.EventBookingData
import com.tourisz.api.response.HotelBookingData
import com.tourisz.util.BookingTypes
import com.tourisz.util.listener.TransactionClickListener

class TransactionAdapter(private val list: List<Object>, private val transactionClickListener: TransactionClickListener) : RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var linRoot: LinearLayout
        var txtName: TextView
        var txtCustomerName: TextView
        var txtTransactionId: TextView


        init {
            linRoot = view.findViewById<View>(R.id.linRoot) as LinearLayout
            txtName = view.findViewById<View>(R.id.txtName) as TextView
            txtCustomerName = view.findViewById<View>(R.id.txtCustomerName) as TextView
            txtTransactionId = view.findViewById<View>(R.id.txtTransactionId) as TextView

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transaction = list[position]
        if (transaction is HotelBookingData) {
            holder.txtName.text = transaction.hotelName
            holder.txtCustomerName.text = transaction.customerName
            holder.txtTransactionId.text = transaction.paymentTransactionId
        } else if (transaction is EventBookingData) {
            holder.txtName.text = transaction.eventTitle
            holder.txtCustomerName.text = transaction.customerName
            holder.txtTransactionId.text = transaction.paymentTransactionId
        }
        holder.linRoot.setOnClickListener({ transactionClickListener.onTranactionClick(transaction) })
    }

    override fun getItemCount(): Int {
        return list.size
    }
}