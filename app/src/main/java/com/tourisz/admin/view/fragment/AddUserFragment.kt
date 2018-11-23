package com.tourisz.admin.view.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tourisz.R
import com.tourisz.api.response.UsersData
import com.tourisz.util.UserTypes
import com.tourisz.util.helper.AndroidHelper
import com.tourisz.util.validator.Validator


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddUserFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private var usersData: UsersData? = null
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                usersData = it.getSerializable(ARG_PARAM1) as UsersData?
                isEditMode = it.getBoolean(ARG_PARAM2)
            }

        }
    }


    private lateinit var mView: View
    private lateinit var spStatus: Spinner
    private lateinit var edtName: EditText
    private lateinit var edtEmailId: EditText
    private lateinit var edtContactPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var txtInfo: TextView

    private lateinit var imgUser: ImageView
    private lateinit var btnCreate: Button
    private lateinit var stat_adapter: ArrayAdapter<String>
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_user, container, false)
        initIds()

        val stat_array = getResources().getStringArray(R.array.status_type)
        stat_adapter = ArrayAdapter(activity, R.layout.spinner_layout, stat_array)
        spStatus.adapter = stat_adapter

        if (isEditMode) {
            btnCreate.text = getString(R.string.save)
            setData(usersData)
        } else {
            btnCreate.text = getString(R.string.create)
            txtInfo.visibility = View.GONE
        }

        btnCreate.setOnClickListener(this)
        imgUser.setOnClickListener(this)
        fab.setOnClickListener(this)



        return mView
    }

    private fun initIds() {
        imgUser = mView.findViewById(R.id.imgUser)
        edtContactPhone = mView.findViewById(R.id.edtContactPhone)
        edtEmailId = mView.findViewById(R.id.edtEmailId)
        spStatus = mView.findViewById(R.id.spStatus)
        edtName = mView.findViewById(R.id.edtName)
        btnCreate = mView.findViewById(R.id.btnCreate)
        fab = mView.findViewById(R.id.fab)
        edtPassword = mView.findViewById(R.id.edtPassword)
        txtInfo = mView.findViewById(R.id.txtInfo)


    }

    fun setData(usersData: UsersData?) {
        if (usersData != null) {
            edtName.setText(usersData.userName)
            edtContactPhone.setText(usersData.contactNumber)
            edtEmailId.setText(usersData.email)
            Log.e("sts", usersData.status)
            spStatus.setSelection(getIndex(spStatus, AndroidHelper.getStatus(usersData.status)))

        }
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCreate -> validateData()
/*
            R.id.imgUser -> onUserDpChangeClicked()
*/
            R.id.fab -> onUserDpChangeClicked()
        }
    }


    fun validateData() {
        if (TextUtils.isEmpty(edtName.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_uname))
            return
        } else if (TextUtils.isEmpty(edtEmailId.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_email))
            return
        } else if (!Validator.newInstance().isValidEmail(edtEmailId.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.invalid_email))
            return
        } else if (TextUtils.isEmpty(edtContactPhone.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_contact_num))
            return
        } else if (Validator.newInstance().isPhoneNoValid(edtContactPhone.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.invalid_phone1))
            return
        } else if (!android.util.Patterns.PHONE.matcher(edtContactPhone.text.toString()).matches()) {
            AndroidHelper.showToast(activity, getString(R.string.invalid_phone1))
            return
        }



        if (!isEditMode) {
            if (!edtPassword.text.toString().isEmpty() && edtPassword.text.toString().length < 6) {
                AndroidHelper.showToast(activity, getString(R.string.invalid_pass))
                return
            }
            usersData = UsersData()
        }
        usersData?.userName = edtName.text.toString()
        usersData?.email = edtEmailId.text.toString()
        usersData?.contactNumber = edtContactPhone.text.toString()
        usersData?.password = edtPassword.text.toString()
        usersData?.status = AndroidHelper.getStatusId(spStatus.selectedItem.toString()).toString()
        usersData?.userType = UserTypes.NORMAL_USER
        onCreateSaveClicked()
    }

    fun onUserDpChangeClicked() {
        listener?.onUserDpChangeClicked()
    }


    fun onCreateSaveClicked() {
        listener?.onUserCreateSaveClicked(usersData, isEditMode)
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

    fun setImage(imageBitmap: Bitmap) {
        imgUser.setImageBitmap(imageBitmap)
    }

    interface OnFragmentInteractionListener {
        fun onUserDpChangeClicked()
        fun onUserCreateSaveClicked(usersData: UsersData?, isEditMode: Boolean)

    }

    companion object {
        @JvmStatic
        fun newInstance(usersData: UsersData?, isEditMode: Boolean) =
                AddUserFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, usersData)
                        putBoolean(ARG_PARAM2, isEditMode)
                    }
                }
    }

}
