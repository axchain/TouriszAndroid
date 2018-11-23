package com.tourisz.user.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.tourisz.R
import com.tourisz.util.validator.Validator


class LoginFragment : Fragment() , View.OnClickListener{

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var txtForgotPass: TextView
    private lateinit var txtForgotUname: TextView
    private lateinit var edtUname: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var imgFb: ImageView
    private lateinit var imgGplus: ImageView
    private lateinit var imgToggle: ImageView

    private lateinit var mView: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_login, container, false)
        initIds()
        return mView
    }

    fun onLoginClicked(uname: String, pass :String) {
        listener?.onLoginClicked(uname, pass)
    }

    fun onSignUpClicked() {
        listener?.onSignUpClicked()
    }

    fun onForgotPassClicked() {
        listener?.onForgotPassClicked()
    }

    fun onForgotUnameClicked() {
        listener?.onForgotUnameClicked()
    }

    fun onGplusLoginClicked() {
        listener?.onGplusLoginClicked()
    }

    fun onFbLoginClicked() {
        listener?.onFbLoginClicked()
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

        fun onLoginClicked(uname: String, pass :String)

        fun onSignUpClicked()

        fun onForgotPassClicked()

        fun onForgotUnameClicked()

        fun onFbLoginClicked()

        fun onGplusLoginClicked()


    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    private fun initIds() {

        txtForgotPass = mView.findViewById(R.id.txtForgotPass)
        txtForgotUname = mView.findViewById(R.id.txtForgotUname)
        edtPassword = mView.findViewById(R.id.edtPassword)
        edtUname = mView.findViewById(R.id.edtUname)

        btnLogin = mView.findViewById(R.id.btnLogin)
        btnSignUp = mView.findViewById(R.id.btnSignUp)

        imgFb = mView.findViewById(R.id.imgFb)
        imgGplus = mView.findViewById(R.id.imgGplus)
        imgToggle = mView.findViewById(R.id.imgToggle)

        setListner(txtForgotPass, btnSignUp, btnLogin, txtForgotUname, imgGplus, imgFb)
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


    fun setListner(vararg views: View){

        for (view in views){
            view.setOnClickListener(this)
        }

    }

    override fun onClick(view: View){

        when (view.id) {

            R.id.btnLogin -> onLoginClicked(edtUname.text.toString(), edtPassword.text.toString())

            R.id.btnSignUp -> onSignUpClicked()

            R.id.txtForgotUname -> onForgotUnameClicked()

            R.id.txtForgotPass -> onForgotPassClicked()

            R.id.imgFb -> onFbLoginClicked()

            R.id.imgGplus -> onGplusLoginClicked()


        }
    }



}
