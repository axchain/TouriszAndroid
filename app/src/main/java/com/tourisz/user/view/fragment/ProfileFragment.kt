package com.tourisz.user.view.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.tourisz.R
import com.tourisz.api.URLS
import com.tourisz.api.response.UserData
import com.tourisz.util.UserTypes
import com.tourisz.util.helper.AndroidHelper
import de.hdodenhof.circleimageview.CircleImageView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var mView: View
    private lateinit var btnChangePass: Button
    private lateinit var txtUName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtPhone: TextView

    private lateinit var btnOk: Button
    private lateinit var linChangePass: LinearLayout
    private lateinit var userData: UserData
    private lateinit var img: CircleImageView
    private lateinit var edtNewPassword: EditText
    private lateinit var edtConPassword: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_profile, container, false)
        initIds()
        userData = AndroidHelper.getUser(activity)
        if (userData.userType == UserTypes.AGENT) {
            btnChangePass.visibility = View.GONE
        }

        if (userData.userType == UserTypes.ADMIN) {
            edtConPassword.setHint(getString(R.string.new_password))
            edtNewPassword.setHint(getString(R.string.old_password))

        }
        setData(userData)

        btnChangePass.setOnClickListener { linChangePass.visibility = View.VISIBLE }

        btnOk.setOnClickListener {
            validate()
        }

        return mView
    }

    private fun initIds() {
        img = mView.findViewById(R.id.img)
        btnChangePass = mView.findViewById(R.id.btnChangePass)
        btnOk = mView.findViewById(R.id.btnOk)
        linChangePass = mView.findViewById(R.id.linChangePass)
        txtEmail = mView.findViewById(R.id.txtEmail)
        txtPhone = mView.findViewById(R.id.txtPhone)
        txtUName = mView.findViewById(R.id.txtUName)
        edtNewPassword = mView.findViewById(R.id.edtNewPassword)
        edtConPassword = mView.findViewById(R.id.edtConPassword)

    }

    fun setData(userData: UserData) {
        txtUName.text = userData.userName
        txtEmail.text = userData.email
        txtPhone.text = userData.contactNumber
        Glide.with(activity).load(URLS.newInstance().IMG_URL + userData.photo)
                .placeholder(R.drawable.ic_dp)
                .error(R.drawable.ic_dp)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img)
    }

    fun validate() {
        var pass = edtNewPassword.text.toString()
        var con_pass = edtConPassword.text.toString()

        if (pass.isEmpty() || con_pass.isEmpty()) {
            AndroidHelper.showToast(activity, getString(R.string.enter_new_pass))
            return
        }

        if (userData.userType != UserTypes.ADMIN && !pass.equals(con_pass)) {
            AndroidHelper.showToast(activity, getString(R.string.missmatch_pass))
            return
        }



        linChangePass.visibility = View.GONE
        if (userData.userType == UserTypes.ADMIN) {
            onChangeAdminPasswordClicked(pass, con_pass)
        } else {
            onChangePasswordClicked(pass)
        }
    }

    fun onChangePasswordClicked(password: String) {
        listener?.onChangePasswordClicked(userData, password)
    }

    fun onChangeAdminPasswordClicked(oldPass: String, newPass: String) {
        listener?.onChangeAdminPasswordClicked(oldPass, newPass)
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
        fun onChangePasswordClicked(userData: UserData, password: String)
        fun onChangeAdminPasswordClicked(oldPass: String, newPass: String)

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
