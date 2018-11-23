package com.tourisz.user.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.tourisz.R
import com.tourisz.custome_view.PinEntryEditText
import com.tourisz.util.helper.AndroidHelper

class OTPFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var btnSend: Button
    private lateinit var btnVerify: Button

    private lateinit var edtOtp: PinEntryEditText

    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_otp, container, false)
        btnSend = mView.findViewById(R.id.btnSend)
        btnVerify = mView.findViewById(R.id.btnVerify)

        edtOtp = mView.findViewById(R.id.edtOtp)
        btnSend.setOnClickListener { onOptSendClicked(edtOtp.text.toString()) }

        btnVerify.setOnClickListener {
            if (edtOtp.text.toString().isEmpty() || edtOtp.text.toString().length != 6) {
                AndroidHelper.showToast(activity, getString(R.string.enter_valid_otp))
            } else {
                onVerifyClicked(edtOtp.text.toString())
            }
        }

        return mView
    }

    fun onOptSendClicked(otp: String) {
        listener?.onOptSendClicked(otp)
    }

    fun onVerifyClicked(otp: String) {
        listener?.onVerifyClicked(otp)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onOptSendClicked(otp: String)
        fun onVerifyClicked(otp: String)
    }

    companion object {

        @JvmStatic
        fun newInstance() = OTPFragment()
    }

    fun setOtp(otp: String) {
        edtOtp.setText(otp)
    }
}
