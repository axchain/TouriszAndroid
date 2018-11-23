package com.tourisz.user.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatCheckBox
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tourisz.R
import com.tourisz.util.UserTypes


class SignUpFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var mView: View
    private lateinit var btnRegister: Button
    private lateinit var txtTnC: TextView
    private lateinit var cbTnC: AppCompatCheckBox
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtAgency: EditText
    private lateinit var rgType: RadioGroup
    private lateinit var imgToggle: ImageView
    private lateinit var spCountry: Spinner

    private lateinit var viewAgency: View
    private var userType: Int = UserTypes.NORMAL_USER

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        initIds()

        txtTnC.setOnClickListener {
            listener?.onTncClicked()
        }

        btnRegister.setOnClickListener {
            onRegisterClicked(edtName.text.toString(), edtEmail.text.toString(), edtPassword.text.toString(), spCountry.selectedItem.toString() + edtPhone.text.toString(), userType, edtAgency.text.toString())
        }
        return mView
    }

    private fun initIds() {
        btnRegister = mView.findViewById(R.id.btnRegister)
        imgToggle = mView.findViewById(R.id.imgToggle)
        txtTnC = mView.findViewById(R.id.txtTnC)
        cbTnC = mView.findViewById(R.id.cbTnC)
        edtEmail = mView.findViewById(R.id.edtEmail)
        edtPassword = mView.findViewById(R.id.edtPassword)
        edtName = mView.findViewById(R.id.edtName)
        edtPhone = mView.findViewById(R.id.edtPhone)
        rgType = mView.findViewById(R.id.rgType)
        edtAgency = mView.findViewById(R.id.edtAgency)
        viewAgency = mView.findViewById(R.id.viewAgency)
        viewAgency = mView.findViewById(R.id.viewAgency)
        spCountry = mView.findViewById(R.id.spCountry)
        val con_array = getResources().getStringArray(R.array.country_code)
        var con_adapter = ArrayAdapter(activity, R.layout.spinner_layout, con_array)
        spCountry.adapter = con_adapter

        rgType.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {

            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {

                if (checkedId == R.id.cbUser) {
                    edtAgency.visibility = View.GONE
                    viewAgency.visibility = View.GONE

                    userType = UserTypes.NORMAL_USER

                } else {
                    edtAgency.visibility = View.VISIBLE
                    viewAgency.visibility = View.VISIBLE
                    userType = UserTypes.AGENT


                }
            }

        })

        var isVis = false
        imgToggle.setOnClickListener{
            if (isVis){
                isVis = false
                imgToggle.setImageResource(R.drawable.ic_visibility)
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }else{
                isVis = true
                imgToggle.setImageResource(R.drawable.ic_visibility_off)
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            }
        }
    }

    fun onRegisterClicked(name: String, emailId: String, password: String, phone: String, userType: Int, agencyName: String) {
        listener?.onRegisterClicked(name, emailId, password, phone, cbTnC.isChecked, userType, agencyName)
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
        fun onRegisterClicked(name: String, emailId: String, password: String, phone: String, isTermChecked: Boolean, userType: Int, agencyName: String)
        fun onTncClicked()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}
