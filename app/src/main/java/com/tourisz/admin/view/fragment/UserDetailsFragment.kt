package com.tourisz.admin.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tourisz.R
import com.tourisz.api.response.UsersData
import com.tourisz.util.UserTypes

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class UserDetailsFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var usersData: UsersData? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                usersData = it.getSerializable(ARG_PARAM1) as UsersData?
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
    private lateinit var imgDp: ImageView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_user_details, container, false)
        initIds()

        setData(usersData)
        return mView
    }

    fun setData(usersData: UsersData?){
        txtEmail.text = usersData?.email
        txtUname.text = usersData?.userName
        txtPhone.text = usersData?.contactNumber
        txtPostDate.text = usersData?.createdOn
        usersData?.userType = UserTypes.NORMAL_USER

    }

    private fun initIds() {

        btnEdit = mView.findViewById(R.id.btnEdit)
        btnDelete = mView.findViewById(R.id.btnDelete)
        imgDp = mView.findViewById(R.id.imgDp)
        txtPostDate = mView.findViewById(R.id.txtPostDate)
        txtPhone = mView.findViewById(R.id.txtPhone)
        txtEmail = mView.findViewById(R.id.txtEmail)
        txtUname = mView.findViewById(R.id.txtUname)

        setListener(btnEdit, btnDelete)
    }

    fun setListener(vararg views: View){
        for (view in views){
            view.setOnClickListener(this)
        }
    }



    override fun onClick(view: View) {
        when(view.id){
            R.id.btnDelete -> onDeleteClicked()
            R.id.btnEdit -> onEditClicked()

        }
    }

    fun onEditClicked() {
        listener?.onUserEditClicked(usersData)
    }

    fun onDeleteClicked() {
        listener?.onUserDeleteClicked(usersData)
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

        fun onUserEditClicked(usersData: UsersData?)

        fun onUserDeleteClicked(usersData: UsersData?)

    }

    companion object {

        @JvmStatic
        fun newInstance(usersData: UsersData, param2: String) =
                UserDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, usersData)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }



}
