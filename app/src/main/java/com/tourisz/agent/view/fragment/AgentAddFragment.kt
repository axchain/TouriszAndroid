package com.tourisz.agent.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.maps.model.LatLng
import com.tourisz.R
import com.tourisz.agent.adapter.FileListAdapter
import com.tourisz.agent.adapter.ImageListAdapter
import com.tourisz.api.response.EventData
import com.tourisz.api.response.HotelData
import com.tourisz.api.response.UserData
import com.tourisz.entity.LocalFile
import com.tourisz.user.adapter.SlidingImageAdapter
import com.tourisz.util.Constant
import com.tourisz.util.UserTypes
import com.tourisz.util.helper.AndroidHelper
import com.tourisz.util.listener.OnFileAddClickListener
import com.tourisz.util.validator.Validator
import kotlinx.android.synthetic.main.fragment_agent_add.*
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AgentAddFragment : Fragment(), OnFileAddClickListener, View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private var hotelData: HotelData? = null
    private var eventData: EventData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                hotelData = it.getSerializable(ARG_PARAM1) as HotelData?
                eventData = it.getSerializable(ARG_PARAM2) as EventData?
            }

        }
    }


    private lateinit var mView: View
    private lateinit var rvAddPhotos: RecyclerView
    private var arrayList: ArrayList<LocalFile> = ArrayList()
    private lateinit var fileListAdapter: FileListAdapter
    private lateinit var spCountry: Spinner
    private lateinit var spStatus: Spinner
    private lateinit var spAvailability: Spinner
    private lateinit var spRoomType: Spinner
    private lateinit var txtEventDateTime: TextView
    private lateinit var edtName: EditText
    private lateinit var edtShortDesc: EditText
    private lateinit var edtLongDesc: EditText
    private lateinit var edtContactName: EditText
    private lateinit var edtContactPhone: EditText
    private lateinit var edtWeblink: EditText
    private lateinit var txtAddress: TextView
    private lateinit var edtCity: EditText
    private lateinit var edtRate: EditText

    private lateinit var lineDate: View
    private lateinit var lineRoom: View
    private lateinit var btnCreate: Button
    private lateinit var con_adapter: ArrayAdapter<String>
    private lateinit var stat_adapter: ArrayAdapter<String>
    private lateinit var av_adapter: ArrayAdapter<String>
    private lateinit var userData: UserData
    private var latLng: LatLng? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_agent_add, container, false)
        userData = AndroidHelper.getUser(activity)

        initIds()

        Log.e("hotel", hotelData.toString())
        Log.e("event", eventData.toString())
        val con_array = getResources().getStringArray(R.array.countries)
        con_adapter = ArrayAdapter(activity, R.layout.spinner_layout, con_array)
        spCountry.adapter = con_adapter

        val stat_array = getResources().getStringArray(R.array.status_type)
        stat_adapter = ArrayAdapter(activity, R.layout.spinner_layout, stat_array)
        spStatus.adapter = stat_adapter

        val av_array = getResources().getStringArray(R.array.avail_type)
        av_adapter = ArrayAdapter(activity, R.layout.spinner_layout, av_array)
        spAvailability.adapter = av_adapter


        val room_array = getResources().getStringArray(R.array.array_room_type)
        val room_adapter = ArrayAdapter(activity, R.layout.spinner_layout, room_array)
        spRoomType.adapter = room_adapter


        if (eventData != null) {
            txtEventDateTime.visibility = View.VISIBLE
            lineDate.visibility = View.VISIBLE
            edtName.setHint(getString(R.string.event_title_hint))

            spRoomType.visibility = View.GONE
            lineRoom.visibility = View.GONE
            setEventData(eventData)
        } else {
            if (hotelData != null) {
                txtEventDateTime.visibility = View.GONE
                lineDate.visibility = View.GONE

                edtName.setHint(getString(R.string.hotel_name_hint))

                spRoomType.visibility = View.VISIBLE
                lineRoom.visibility = View.VISIBLE

                setHotelData(hotelData)
            }
        }


        txtEventDateTime.setOnClickListener(this)
        btnCreate.setOnClickListener(this)
        txtAddress.setOnClickListener(this)


        return mView
    }

    private fun initIds() {
        edtRate = mView.findViewById(R.id.edtRate)
        edtShortDesc = mView.findViewById(R.id.edtShortDesc)
        edtLongDesc = mView.findViewById(R.id.edtLongDesc)
        edtContactName = mView.findViewById(R.id.edtContactName)
        edtContactPhone = mView.findViewById(R.id.edtContactPhone)
        edtWeblink = mView.findViewById(R.id.edtWeblink)
        txtAddress = mView.findViewById(R.id.txtAddress)
        edtCity = mView.findViewById(R.id.edtCity)

        rvAddPhotos = mView.findViewById(R.id.rvAddPhotos)
        spCountry = mView.findViewById(R.id.spCountry)
        spStatus = mView.findViewById(R.id.spStatus)
        spRoomType = mView.findViewById(R.id.spRoomType)
        spAvailability = mView.findViewById(R.id.spAvailability)
        txtEventDateTime = mView.findViewById(R.id.txtEventDateTime)
        edtName = mView.findViewById(R.id.edtName)
        lineRoom = mView.findViewById(R.id.lineRoom)
        lineDate = mView.findViewById(R.id.lineDate)
        btnCreate = mView.findViewById(R.id.btnCreate)

        val layoutManager = GridLayoutManager(activity, 4)
        rvAddPhotos.layoutManager = layoutManager

        setAdapter(arrayList)
    }


    fun setEventData(eventData: EventData?) {
        if (eventData?.eventTitle == null) {
            btnCreate.text = getString(R.string.create)
            return
        }

        edtName.setText(eventData?.eventTitle)
        txtEventDateTime.setText(eventData?.eventDateTime)
        edtShortDesc.setText(eventData?.shortDescription)
        edtLongDesc.setText(eventData?.description)
        edtWeblink.setText(eventData?.webLink)
        edtContactName.setText(eventData?.contactPerson)
        edtContactPhone.setText(eventData?.contactNumber)
        txtAddress.setText(eventData?.address)
        edtCity.setText(eventData?.city)
        edtRate.setText(eventData?.bookingCharges)


        spCountry.setSelection(getIndex(spCountry, eventData?.country))
        spStatus.setSelection(getIndex(spStatus, AndroidHelper.getStatus(eventData?.status)))
        spAvailability.setSelection(getIndex(spAvailability, AndroidHelper.getAvailability(eventData?.isAvailable)))
        setImageAdapter(eventData?.poster)

    }

    fun setHotelData(hotelData: HotelData?) {
        if (hotelData?.hotelName == null) {
            btnCreate.text = getString(R.string.create)
            return
        }

        edtName.setText(hotelData?.hotelName)
        edtShortDesc.setText(hotelData?.shortDescription)
        edtLongDesc.setText(hotelData?.description)
        edtWeblink.setText(hotelData?.webLink)
        edtContactName.setText(hotelData?.contactPerson)
        edtContactPhone.setText(hotelData?.contactNumber)
        txtAddress.setText(hotelData?.address)
        edtCity.setText(hotelData?.city)
        edtRate.setText(hotelData?.bookingCharges)

        spRoomType.setSelection(getIndex(spRoomType, "Delux"))
        spCountry.setSelection(getIndex(spCountry, hotelData?.country))
        spStatus.setSelection(getIndex(spStatus, AndroidHelper.getStatus(hotelData?.status)))
        spAvailability.setSelection(getIndex(spAvailability, AndroidHelper.getAvailability(hotelData?.isAvailable)))

        setImageAdapter(hotelData?.poster)
    }

    fun setImageAdapter(nameslist: String) {
        val names = AndroidHelper.getAddImageArray(nameslist)
        val adapter = ImageListAdapter(activity, names, this)
        rvAddPhotos.adapter = adapter
    }


    private fun getIndex(spinner: Spinner, myString: String?): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }

        return 0
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.txtEventDateTime -> onEventDateTimeClicked()
            R.id.btnCreate -> {
                validateData()
            }
            R.id.txtAddress -> onAddressClicked()


        }
    }

    fun validateData() {

        if (TextUtils.isEmpty(edtName.text.toString())) {

                AndroidHelper.showToast(activity, if (eventData == null) getString(R.string.enter_hotel) else getString(R.string.enter_event))

            return
        } else if (eventData != null && (TextUtils.isEmpty(txtEventDateTime.text.toString()) || txtEventDateTime.text.equals(getString(R.string.events_date_hint)))) {
            AndroidHelper.showToast(activity, getString(R.string.select_date_time))
            return
        } else if (TextUtils.isEmpty(edtShortDesc.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_short_desc))
            return
        } else if (TextUtils.isEmpty(edtLongDesc.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_long_desc))
            return
        } else if (TextUtils.isEmpty(edtContactName.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_contact_name))
            return
        } else if (TextUtils.isEmpty(edtContactPhone.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_contact_num))
            return
        } else if (Validator.newInstance().isPhoneNoValid(edtContactPhone.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.invalid_phone1))
            return
        } else if (edtRate.text.equals(getString(R.string.rate_room_hint)) || TextUtils.isEmpty(edtRate.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_rate))
            return
        } else if (edtRate.text.toString().toInt() == 0) {
            AndroidHelper.showToast(activity, getString(R.string.enter_rate))
            return
        } else if (txtAddress.text.equals(getString(R.string.address_hint))) {
            AndroidHelper.showToast(activity, getString(R.string.select_address))
            return
        } else if (TextUtils.isEmpty(edtCity.text.toString())) {
            AndroidHelper.showToast(activity, getString(R.string.enter_city))
            return
        }else if (btnCreate.text.equals(getString(R.string.create)) && arrayList.size <1) {
            AndroidHelper.showToast(activity, getString(R.string.select_images))
            return
        }

        if (eventData == null) {

            hotelData?.hotelName = edtName.text.toString()
            hotelData?.shortDescription = edtShortDesc.text.toString()
            hotelData?.description = edtLongDesc.text.toString()
            hotelData?.webLink = edtWeblink.text.toString()
            hotelData?.contactPerson = edtContactName.text.toString()
            hotelData?.contactNumber = edtContactPhone.text.toString()
            hotelData?.address = txtAddress.text.toString()
            hotelData?.city = edtCity.text.toString()
            hotelData?.country = spCountry.selectedItem.toString()
            hotelData?.status = AndroidHelper.getStatusId(spStatus.selectedItem.toString()).toString()
            hotelData?.isAvailable = AndroidHelper.getAvailabilityId(spAvailability.selectedItem.toString()).toString()
            hotelData?.bookingCharges = edtRate.text.toString()
            if (latLng != null) {
                hotelData?.lat = latLng?.latitude.toString()
                hotelData?.lng = latLng?.longitude.toString()
            } else {
                hotelData?.lat = ""
                hotelData?.lng = ""
            }
            if (btnCreate.text.toString().equals(getString(R.string.create))) {
                hotelData?.agentId = userData.id
            }

        } else {

            eventData?.eventTitle = edtName.text.toString()

            val date1 = SimpleDateFormat(Constant.DATE_TIME_FORMAT, Locale.ENGLISH).parse(txtEventDateTime.text.toString())

            val dateStr2 = SimpleDateFormat(Constant.SERVER_DATE_TIME_FORMAT, Locale.ENGLISH).format(date1)

            eventData?.eventDateTime = dateStr2
            eventData?.shortDescription = edtShortDesc.text.toString()
            eventData?.description = edtLongDesc.text.toString()
            eventData?.webLink = edtWeblink.text.toString()
            eventData?.contactPerson = edtContactName.text.toString()
            eventData?.contactNumber = edtContactPhone.text.toString()
            eventData?.address = txtAddress.text.toString()
            eventData?.city = edtCity.text.toString()
            eventData?.bookingCharges = edtRate.text.toString()
            eventData?.country = spCountry.selectedItem.toString()
            eventData?.status = AndroidHelper.getStatusId(spStatus.selectedItem.toString()).toString()
            eventData?.isAvailable = AndroidHelper.getAvailabilityId(spAvailability.selectedItem.toString()).toString()
            if (latLng != null) {
                eventData?.lat = latLng?.latitude.toString()
                eventData?.lng = latLng?.longitude.toString()
            } else {
                eventData?.lat = ""
                eventData?.lng = ""
            }

            if (btnCreate.text.toString().equals(getString(R.string.create))) {
                eventData?.agentId = userData.id
            }

        }

        onCreateSaveClicked()
    }

    fun setDateTime(dateTime: String) {
        txtEventDateTime.text = dateTime
    }

    override fun onAddClick() {
        onAddImageClicked()
    }

    fun onAddImageClicked() {
        listener?.onAddImageClicked()
    }

    fun onEventDateTimeClicked() {
        listener?.onEventDateTimeClicked()
    }

    fun onAddressClicked() {
        listener?.onAddressClicked()
    }

    fun onCreateSaveClicked() {
        listener?.onCreateSaveClicked(hotelData, eventData, btnCreate.text.equals(getString(R.string.save)))
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
        fun onAddImageClicked()
        fun onEventDateTimeClicked()
        fun onAddressClicked()
        fun onCreateSaveClicked(hotelData: HotelData?, eventData: EventData?, isUpdate: Boolean)

    }

    companion object {
        @JvmStatic
        fun newInstance(hotelBooking: HotelData?, eventData: EventData?) =
                AgentAddFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, hotelBooking)
                        putSerializable(ARG_PARAM2, eventData)
                    }
                }
    }

    fun setAdapter(list: ArrayList<LocalFile>) {
        arrayList.clear()
        arrayList.addAll(list)
        arrayList.add(LocalFile("add*", "", 0))
        fileListAdapter = FileListAdapter(activity, arrayList, this)
        rvAddPhotos.adapter = fileListAdapter
    }

    fun setAddress(address: String, latLng: LatLng) {
        this.latLng = latLng
        txtAddress.text = address
    }
}
