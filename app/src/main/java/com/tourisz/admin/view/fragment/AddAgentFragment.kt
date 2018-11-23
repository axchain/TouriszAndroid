package com.tourisz.admin.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.tourisz.Application
import com.tourisz.R
import com.tourisz.api.response.AgentsData
import com.tourisz.user.presenter.login.LoginContract
import com.tourisz.util.UserTypes
import com.tourisz.util.helper.AndroidHelper
import com.tourisz.util.validator.Validator
import kotlinx.android.synthetic.main.fragment_add_agent.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddAgentFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private var agentsData: AgentsData? = null
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                agentsData = it.getSerializable(ARG_PARAM1) as AgentsData?
                isEditMode = it.getBoolean(ARG_PARAM2)
            }

        }
    }


    private lateinit var mView: View
    private lateinit var spStatus: Spinner
    private lateinit var edtName: EditText
    private lateinit var edtEmailId: EditText
    private lateinit var edtContactPhone: EditText
    private lateinit var edtAgencyName: EditText

    private lateinit var btnCreate: Button
    private lateinit var stat_adapter: ArrayAdapter<String>
    private lateinit var edtPassword: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_agent, container, false)
        initIds()

        val stat_array = getResources().getStringArray(R.array.status_type)
        stat_adapter = ArrayAdapter(activity, R.layout.spinner_layout, stat_array)
        spStatus.adapter = stat_adapter

        if (isEditMode) {
            btnCreate.text = getString(R.string.save)
            setData(agentsData)
            edtPassword.setHint(getString(R.string.password))
        } else {
            btnCreate.text = getString(R.string.create)
            edtPassword.setHint(getString(R.string.password))
        }

        btnCreate.setOnClickListener(this)

        return mView
    }

    private fun initIds() {
        edtContactPhone = mView.findViewById(R.id.edtContactPhone)
        edtEmailId = mView.findViewById(R.id.edtEmailId)
        spStatus = mView.findViewById(R.id.spStatus)
        edtName = mView.findViewById(R.id.edtName)
        btnCreate = mView.findViewById(R.id.btnCreate)
        edtPassword = mView.findViewById(R.id.edtPassword)
        edtAgencyName = mView.findViewById(R.id.edtAgencyName)


    }

    fun setData(agentsData: AgentsData?) {
        if (agentsData != null) {
            edtName.setText(agentsData.userName)
            edtContactPhone.setText(agentsData.contactNumber)
            edtEmailId.setText(agentsData.email)
            edtAgencyName.setText(agentsData.agencyName)
            spStatus.setSelection(getIndex(spStatus, AndroidHelper.getStatus(agentsData.status)))
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
            R.id.imgUser -> onUserDpChangeClicked()
            R.id.fab -> onUserDpChangeClicked()
        }
    }


    fun validateData() {
        if (TextUtils.isEmpty(edtName.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_full_name))
            return
        } else if (TextUtils.isEmpty(edtAgencyName.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_agency))
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
            if (edtPassword.text.toString().isEmpty() || edtPassword.text.toString().length < 6) {
                AndroidHelper.showToast(activity, getString(R.string.invalid_pass))
                return
            }
            agentsData = AgentsData()
        }
        agentsData?.userName = edtName.text.toString()
        agentsData?.email = edtEmailId.text.toString()
        agentsData?.contactNumber = edtContactPhone.text.toString()
        agentsData?.password = edtPassword.text.toString()
        agentsData?.agencyName = edtAgencyName.text.toString()
        agentsData?.status = AndroidHelper.getStatusId(spStatus.selectedItem.toString()).toString()
        agentsData?.userType = UserTypes.AGENT
        onCreateSaveClicked()
    }


    fun onUserDpChangeClicked() {
        listener?.onUserDpChangeClicked()
    }


    fun onCreateSaveClicked() {
        listener?.onAgentCreateSaveClicked(agentsData, isEditMode)
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
        fun onUserDpChangeClicked()
        fun onAgentCreateSaveClicked(agentsData: AgentsData?, isEditMode: Boolean)

    }

    companion object {
        @JvmStatic
        fun newInstance(agentsData: AgentsData?, isEditMode: Boolean) =
                AddAgentFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, agentsData)
                        putBoolean(ARG_PARAM2, isEditMode)
                    }
                }
    }

}
