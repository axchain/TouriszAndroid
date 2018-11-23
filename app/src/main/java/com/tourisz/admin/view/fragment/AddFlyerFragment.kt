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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tourisz.R
import com.tourisz.api.URLS
import com.tourisz.api.response.FlyerData
import com.tourisz.api.response.UsersData
import com.tourisz.entity.HotelBooking
import com.tourisz.entity.LocalFile
import com.tourisz.util.UserTypes
import com.tourisz.util.helper.AndroidHelper
import kotlinx.android.synthetic.main.fragment_add_flyer.*
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFlyerFragment : Fragment(), View.OnClickListener {
    private var listener: OnFragmentInteractionListener? = null
    private var flyerData: FlyerData? = null
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                flyerData = it.getSerializable(ARG_PARAM1) as FlyerData?
                isEditMode = it.getBoolean(ARG_PARAM2)
            }
        }
    }


    private lateinit var mView: View
    private lateinit var spStatus: Spinner
    private lateinit var fab: FloatingActionButton
    private lateinit var btnCreate: Button
    private lateinit var imgFlyer: ImageView
    private lateinit var edtTitle: EditText
    private lateinit var edtPosition: EditText
    private var localFile: LocalFile? =null

    private lateinit var stat_adapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_add_flyer, container, false)
        initIds()

        val stat_array = getResources().getStringArray(R.array.status_type)
        stat_adapter = ArrayAdapter(activity, R.layout.spinner_layout, stat_array)
        spStatus.adapter = stat_adapter
        if (isEditMode) {
            btnCreate.text = getString(R.string.save)
            setData(flyerData)
           // fab.visibility = View.GONE
        } else {
            btnCreate.text = getString(R.string.create)
        }

        btnCreate.setOnClickListener(this)
        fab.setOnClickListener(this)

        return mView
    }

    private fun initIds() {
        fab = mView.findViewById(R.id.fab)
        spStatus = mView.findViewById(R.id.spStatus)
        edtTitle = mView.findViewById(R.id.edtTitle)
        edtPosition = mView.findViewById(R.id.edtPosition)

        btnCreate = mView.findViewById(R.id.btnCreate)
        imgFlyer = mView.findViewById(R.id.imgFlyer)

    }

    fun setData(flyerData: FlyerData?) {
        if (flyerData != null) {
            edtTitle.setText(flyerData.title)
            edtPosition.setText(flyerData.flyerPosition)
            spStatus.setSelection(getIndex(spStatus, AndroidHelper.getStatus(flyerData.status)))
            Glide.with(activity).load(URLS.newInstance().FLYERSIMG_URL + flyerData.poster)
                    .placeholder(R.drawable.ic_banner_placeholder)
                    .error(R.drawable.ic_banner_placeholder)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgFlyer)

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
            R.id.fab -> onFlyerChangeClicked()
        }
    }

    fun validateData() {
        if (TextUtils.isEmpty(edtTitle.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_title))
            return
        }

        if (!isEditMode) {
            if (localFile == null) {
                AndroidHelper.showToast(activity, getString(R.string.select_photo))
                return
            }
            flyerData = FlyerData()
        }
        flyerData?.title = edtTitle.text.toString()
        flyerData?.flyerPosition = edtPosition.text.toString()
        flyerData?.status = AndroidHelper.getStatusId(spStatus.selectedItem.toString()).toString()
        onCreateSaveClicked()
    }



    fun onCreateSaveClicked() {
        listener?.onFlyerCreateSaveClicked(flyerData, isEditMode,localFile)
    }

    fun onFlyerChangeClicked() {
        listener?.onFlyerChangeClicked()
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

        fun onFlyerChangeClicked()

        fun onFlyerCreateSaveClicked(flyerData: FlyerData?, isEditMode: Boolean, localFile: LocalFile?)

    }

    companion object {
        @JvmStatic
        fun newInstance(flyerData: FlyerData?, isEditMode: Boolean) =
                AddFlyerFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, flyerData)
                        putBoolean(ARG_PARAM2, isEditMode)
                    }
                }
    }

    fun setImage(localFile: LocalFile) {

        this.localFile = localFile
        Glide.with(activity).load(File(localFile.path))
                .placeholder(R.drawable.ic_banner_placeholder)
                .crossFade()
                .thumbnail(0.1f)
                .into(imgFlyer)
    }


}
