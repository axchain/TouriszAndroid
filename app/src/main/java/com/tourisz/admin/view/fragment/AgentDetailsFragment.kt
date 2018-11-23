package com.tourisz.admin.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.tourisz.R
import com.tourisz.api.response.AgentsData
import com.tourisz.util.UserTypes

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AgentDetailsFragment : Fragment(), View.OnClickListener {
    private var agentsData: AgentsData? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                agentsData = it.getSerializable(ARG_PARAM1) as AgentsData?
                param2 = it.getString(ARG_PARAM2)
            }
        }
    }

    private lateinit var mView: View
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var txtPostDate: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtUname: TextView
    private lateinit var txtAgencyName: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_agent_details, container, false)
        initIds()
        setData(agentsData)
        return mView
    }

    private fun initIds() {

        btnEdit = mView.findViewById(R.id.btnEdit)
        btnDelete = mView.findViewById(R.id.btnDelete)
        txtPostDate = mView.findViewById(R.id.txtPostDate)
        txtPhone = mView.findViewById(R.id.txtPhone)
        txtEmail = mView.findViewById(R.id.txtEmail)
        txtUname = mView.findViewById(R.id.txtUname)
        txtAgencyName = mView.findViewById(R.id.txtAgencyName)

        setListener(btnEdit, btnDelete)
    }

    fun setListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    fun setData(agentsData: AgentsData?) {
        txtEmail.text = agentsData?.email
        txtUname.text = agentsData?.userName
        txtPhone.text = agentsData?.contactNumber
        txtPostDate.text = agentsData?.createdOn
        txtAgencyName.text = agentsData?.agencyName
        agentsData?.userType = UserTypes.AGENT

    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnDelete -> onDeleteClicked()
            R.id.btnEdit -> onEditClicked()
        }
    }

    fun onEditClicked() {
        listener?.onAgentEditClicked(agentsData)
    }

    fun onDeleteClicked() {
        listener?.onAgentDeleteClicked(agentsData)
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

        fun onAgentEditClicked(agentsData: AgentsData?)

        fun onAgentDeleteClicked(agentsData: AgentsData?)

    }

    companion object {

        @JvmStatic
        fun newInstance(agentsData: AgentsData, param2: String) =
                AgentDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, agentsData)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


}
