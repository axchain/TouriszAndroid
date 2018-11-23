package com.tourisz.user.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import com.tourisz.R

private const val ARG_PARAM1 = "param1"

class ForgotFragment : Fragment() {
    private var isForgotPass: Boolean = true

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var mView: View
    private lateinit var btnSend: Button
    private lateinit var edtEmailOrMob: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isForgotPass = it.getBoolean(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        btnSend = mView.findViewById(R.id.btnSend)
        edtEmailOrMob = mView.findViewById(R.id.edtEmailOrMob)

        btnSend.setOnClickListener { onForgotSendClicked(edtEmailOrMob.text.toString()) }
        return mView
    }

    fun onForgotSendClicked(phoneEmail: String) {
        listener?.onForgotSendClicked(isForgotPass, phoneEmail)
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
        fun onForgotSendClicked(isForgotPass: Boolean, phoneEmail: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(isForgotPass: Boolean) = ForgotFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_PARAM1, isForgotPass)
            }
        }
    }
}
