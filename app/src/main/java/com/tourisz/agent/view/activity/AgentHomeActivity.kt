package com.tourisz.agent.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.nileshp.multiphotopicker.photopicker.activity.PickImageActivity
import com.tourisz.R
import com.tourisz.admin.view.fragment.BookingDetailsFragment
import com.tourisz.admin.view.fragment.EventBookingsFragment
import com.tourisz.admin.view.fragment.HotelBookingsFragment
import com.tourisz.agent.view.fragment.AgentAddFragment
import com.tourisz.agent.view.fragment.AgentHomeFragment
import com.tourisz.agent.view.fragment.AgentShowDetailsFragment
import com.tourisz.agent.view.fragment.HotelEventListFragment
import com.tourisz.agent.view.fragment.child_fragment.EventsFragment
import com.tourisz.agent.view.fragment.child_fragment.HotelsFragment
import com.tourisz.api.request.Object
import com.tourisz.api.response.*
import com.tourisz.entity.HotelBooking
import com.tourisz.entity.LocalFile
import com.tourisz.service.UploaderService
import com.tourisz.user.view.activity.BaseActivity
import com.tourisz.user.view.activity.SplashActivity
import com.tourisz.user.view.fragment.HomeFragment
import com.tourisz.user.view.fragment.ProfileFragment
import com.tourisz.user.view.fragment.SearchFragment
import com.tourisz.util.BookingTypes
import com.tourisz.util.Constant
import com.tourisz.util.Constants
import com.tourisz.util.SearchTypes
import com.tourisz.util.helper.AndroidHelper
import com.tourisz.util.helper.FragmentHelper
import com.tourisz.util.listener.DateSelectListener
import com.tourisz.util.listener.TimeSelectListener
import com.tourisz.util.pickers.DatePickerFragment
import com.tourisz.util.pickers.TimePickerFragment
import kotlinx.android.synthetic.main.activity_home.*
import java.text.SimpleDateFormat
import java.util.*


class AgentHomeActivity : BaseActivity(), HotelBookingsFragment.OnFragmentInteractionListener, EventBookingsFragment.OnFragmentInteractionListener, HotelEventListFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, EventsFragment.OnFragmentInteractionListener, HotelsFragment.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener, View.OnClickListener, SearchFragment.OnFragmentInteractionListener, AgentShowDetailsFragment.OnFragmentInteractionListener, AgentAddFragment.OnFragmentInteractionListener {

    private lateinit var txtTopTitle: TextView
    private lateinit var imgTopLogo: ImageView
    private lateinit var fragmentManager: FragmentManager


    private var searchMenu: MenuItem? = null
    private lateinit var addFragment: AgentAddFragment
    private var searchType: Int = SearchTypes.AGNET_HOTELS
    private lateinit var obj: Object
    private lateinit var progressDialog: ProgressDialog
    private lateinit var agentShowDetailsFragment: AgentShowDetailsFragment
    private var currentItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_agent)
        setUp()
        initIds()
        registerBroadcast()

        setTopLogo()

        fragmentManager.addOnBackStackChangedListener(this)

        setMenuVisibility(false)

        addFragment(AgentHomeFragment.newInstance("", ""), false)

    }

    private fun registerBroadcast() {
        registerReceiver(broadCastReceiver, IntentFilter(Constants.ACTION_DONE))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.agent_search_menu, menu);
        searchMenu = menu!!.findItem(R.id.action_search)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_search) {

            var hotelBooking = HotelBooking()

            hotelBooking.type = if (currentItem == 0)
                BookingTypes.HOTEL
            else
                BookingTypes.EVENT

            addSearchFragment(SearchFragment.newInstance(searchType, ""), true)

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackStackChanged() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is AgentShowDetailsFragment) {
            setTopTitle(getString(R.string.details))
            setMenuVisibility(true)
            isEditMode = false;
        } else if (currentFragment is ProfileFragment) {
            setTopTitle(getString(R.string.my_profile))
            setMenuVisibility(false)
            isEditMode = false
        } else if (currentFragment is AgentAddFragment) {
            if (currentItem == 0) {
                setTopTitle(if (isEditMode) getString(R.string.edit_hotel)
                else getString(R.string.add_hotel))
            } else {
                setTopTitle(if (isEditMode) getString(R.string.edit_event)
                else getString(R.string.add_event))
            }
            setMenuVisibility(false)
        } else if (currentFragment is AgentHomeFragment) {
            setTopLogo()
            setMenuVisibility(false)
            isEditMode = false
        } else if (currentFragment is HotelEventListFragment) {
            setTopLogo()
            setMenuVisibility(true)
            isEditMode = false
        } else if (currentFragment is HotelBookingsFragment) {
            setTopTitle(getString(R.string.hotel_bookings))
            setMenuVisibility(false)
            searchType = SearchTypes.AGNET_HOTEL_BOOKINGS
        } else if (currentFragment is EventBookingsFragment) {
            setTopTitle(getString(R.string.event_bookings))
            setMenuVisibility(false)
            searchType = SearchTypes.AGNET_EVENT_BOOKINGS
        } else if (currentFragment is BookingDetailsFragment) {
            setTopTitle(getString(R.string.booking_details))
            setMenuVisibility(false)
            isEditMode = false
        } else {
            setTopLogo()
            setMenuVisibility(false)
            isEditMode = false
        }
        AndroidHelper.hideKeyboard(this)
    }

    private fun setUp() {
        val mToolbar = findViewById(R.id.toolbar) as Toolbar?
        supportActionBar?.setDisplayShowTitleEnabled(false);
        mToolbar?.title = ""
        setSupportActionBar(mToolbar)
        mToolbar?.setNavigationOnClickListener { toggleDrawer() }

        fragmentManager = supportFragmentManager
    }

    private fun initIds() {
        txtTopTitle = findViewById(R.id.txtTopTitle)
        imgTopLogo = findViewById(R.id.imgTopLogo)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private lateinit var hotelEventListFragment: HotelEventListFragment

    override fun onClick(view: View) {
        when (view.id) {
            R.id.txtNavHome -> openHome()


            R.id.txtNavHotel -> {
                toggleDrawer()
                var currentFrag = getCurrentFragment()
                if (currentFrag is HotelEventListFragment) {
                    currentFrag.setCurrentItem(0)
                } else {
                    hotelEventListFragment = HotelEventListFragment.newInstance(0, "")
                    addFragment(hotelEventListFragment, true)
                }
            }
            R.id.txtEvent -> {
                var currentFrag = getCurrentFragment();
                if (currentFrag is HotelEventListFragment) {
                    currentFrag.setCurrentItem(1)
                } else {
                    hotelEventListFragment = HotelEventListFragment.newInstance(1, "")
                    addFragment(hotelEventListFragment, true)
                }
                toggleDrawer()
            }

            R.id.txtNavProfile -> openProfile()

            R.id.txtNavLogout -> logout()

            R.id.txtNavHotelBookings -> openHotelBookings()

            R.id.txtNavEventBookings -> openEventBookings()
        }
    }


    fun openHome() {
        setTopLogo()
        toggleDrawer()
        showHome()
    }

    fun toggleDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        AndroidHelper.hideKeyboard(this)
    }

    fun openProfile() {
        toggleDrawer()
        showProfile()
    }

    fun showProfile() {
        val currentFragment = getCurrentFragment()
        if (currentFragment !is ProfileFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(ProfileFragment.newInstance("", ""), true)
        }
    }

    private fun openHotelBookings() {
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is HotelBookingsFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(HotelBookingsFragment.newInstance("", ""), true)
        }
    }


    private fun openEventBookings() {
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is EventBookingsFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(EventBookingsFragment.newInstance("", ""), true)
        }
    }


    fun showHome() {
        val currentFragment = getCurrentFragment()
        if (currentFragment !is AgentHomeFragment) {
            clearBackstack()
        }
    }

    override fun openAdd() {

        if (currentItem == 0) {
            addFragment = AgentAddFragment.newInstance(HotelData(), null)
        } else {
            addFragment = AgentAddFragment.newInstance(null, EventData())

        }

        addFragment(addFragment, true)
    }


    override fun setSearchType(type: Int) {
        searchType = type
    }

    fun logout() {
        AndroidHelper.addSharedPreference(this, Constants.USER_DATA, "")
        toggleDrawer()
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }


    fun setTopTitle(title: String) {
        imgTopLogo.visibility = View.GONE
        txtTopTitle.visibility = View.VISIBLE
        txtTopTitle.text = title
    }

    fun setTopLogo() {
        imgTopLogo.visibility = View.VISIBLE
        txtTopTitle.visibility = View.GONE
    }


    fun addSearchFragment(fragment: Fragment, addToBackstack: Boolean) {
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack()
        }
        FragmentHelper.newInstance().addFragment(fragmentManager, fragment, addToBackstack, R.id.homeMainContainer)
    }

    fun addFragment(fragment: Fragment, addToBackstack: Boolean) {
        FragmentHelper.newInstance().addFragment(fragmentManager, fragment, addToBackstack, R.id.mainContainer)
    }

    fun replaceFragment(fragment: Fragment, addToBackstack: Boolean) {
        FragmentHelper.newInstance().replaceFragment(fragmentManager, fragment, addToBackstack, R.id.mainContainer)
    }


    override fun onHotelListItemClick(hotelData: HotelData) {
        agentShowDetailsFragment = AgentShowDetailsFragment.newInstance(hotelData, null)
        addFragment(agentShowDetailsFragment, true)
    }


    override fun onEventListItemClick(eventData: EventData) {
        agentShowDetailsFragment = AgentShowDetailsFragment.newInstance(null, eventData)
        addFragment(agentShowDetailsFragment, true)
    }


    override fun onSearchItemClicked(obj: Object, searchTypes: Int) {
        fragmentManager.popBackStack()
        if (obj is HotelData) {
            agentShowDetailsFragment = AgentShowDetailsFragment.newInstance(obj, null)
            replaceFragment(agentShowDetailsFragment, true)

        } else if (obj is EventData) {
            agentShowDetailsFragment = AgentShowDetailsFragment.newInstance(null, obj)
            replaceFragment(agentShowDetailsFragment, true)
        }

    }

    override fun onHotelBookingListItemClick(hotelBooking: HotelBookingData) {
        searchType = SearchTypes.AGNET_HOTEL_BOOKINGS
        addFragment(BookingDetailsFragment.newInstance(hotelBooking, ""), true)
    }


    override fun onEventBookingListItemClick(eventBookingData: EventBookingData) {
        searchType = SearchTypes.AGNET_EVENT_BOOKINGS
        addFragment(BookingDetailsFragment.newInstance(eventBookingData, ""), true)
    }

    override fun onSearchBookClicked(obj: Object, searchType: Int) {
    }

    override fun onListPagerChanged(position: Int) {
        currentItem = position;
    }

    override fun onHotelEventDeleteClicked(hotelData: HotelData?, eventData: EventData?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.delete_confirm))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            AndroidHelper.showToast(this, "Deleted successfully!")
            onBackPressed()
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }


    override fun onChangeAdminPasswordClicked(oldPass: String, newPass: String) {
    }

    override fun onCreateSaveClicked(hotelData: HotelData?, eventData: EventData?, isUpdate: Boolean) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.submit_confirm))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->

            if (hotelData != null) {
                obj = hotelData
                showProgress(if (isUpdate) getString(R.string.updating_hotel) else getString(R.string.adding_hotel))
            } else if (eventData != null) {
                obj = eventData
                showProgress(if (isUpdate) getString(R.string.updating_event) else getString(R.string.adding_event))
            }
            uploadImages(isUpdate)
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    var isEditMode = false
    override fun onHotelEventEditClicked(hotelData: HotelData?, eventData: EventData?) {
        isEditMode = true
        addFragment = AgentAddFragment.newInstance(hotelData, eventData)
        addFragment(addFragment, true)

    }

    internal var PLACE_AUTOCOMPLETE_REQUEST_CODE = 125

    override fun onAddressClicked() {
        try {
            val typeFilter = AutocompleteFilter.Builder()
                    //.setCountry("IN")
                    .build()
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(this)
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)
        } catch (e: GooglePlayServicesRepairableException) {
            // TODO: Handle the error.
        } catch (e: GooglePlayServicesNotAvailableException) {
            // TODO: Handle the error.
        }

    }


    override fun onAddImageClicked() {
        pickImages()
    }

    var dateTime: String = ""

    override fun onEventDateTimeClicked() {

        showDatePickerDialog()
    }

    override fun onChangePasswordClicked(userData: UserData, password: String) {

    }

    var selDate = Date()

    private var datePickerFragment: DatePickerFragment? = null
    fun showDatePickerDialog() {
        dateTime = ""

        if (datePickerFragment == null) {
            datePickerFragment = DatePickerFragment(dateSelectListener, System.currentTimeMillis())
            datePickerFragment?.show(fragmentManager, "datePicker")
        } else {
            datePickerFragment?.dismiss()
            datePickerFragment = DatePickerFragment(dateSelectListener, System.currentTimeMillis())
            datePickerFragment?.show(fragmentManager, "datePicker")

        }
    }



    private var timePickerFragment: TimePickerFragment? = null

    fun showTimePickerDialog() {

        if (timePickerFragment == null) {
            timePickerFragment = TimePickerFragment(timeSelectListener, selDate)
            timePickerFragment?.show(fragmentManager, "timePicker")
        } else {
            timePickerFragment?.dismiss()
            timePickerFragment = TimePickerFragment(timeSelectListener, selDate)
            timePickerFragment?.show(fragmentManager, "timePicker")
        }
    }


    internal var dateSelectListener: DateSelectListener = object : DateSelectListener {
        override fun onDateSelected(date: String) {
            dateTime = date
            Log.e("date", date)
            selDate = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).parse(date)

            showTimePickerDialog()
        }
    }

    internal var timeSelectListener: TimeSelectListener = object : TimeSelectListener {
        override fun onTimeSelected(time: String) {
            dateTime = "$dateTime $time"
            addFragment.setDateTime(dateTime)

        }

    }


    var localFiles: ArrayList<LocalFile> = ArrayList()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PickImageActivity.PICKER_REQUEST_CODE) {


            var pathList: ArrayList<String>

            if (data?.getStringArrayListExtra(PickImageActivity.KEY_DATA_RESULT) != null) {

                pathList = data.getStringArrayListExtra(PickImageActivity.KEY_DATA_RESULT)!!
                Log.e("size", "${pathList.size}")

                for (path in pathList) {
                    localFiles.add(LocalFile(AndroidHelper.getFileNameFromPath(path), path, 0))
                }
                addFragment.setAdapter(localFiles)
            }

        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)

                val currentFrag = getCurrentFragment()
                if (currentFrag is AgentAddFragment) {
                    currentFrag.setAddress(place.name.toString(), place.latLng)
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data)
                // TODO: Handle the error.
                Log.i("error", status.statusMessage)

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }


    fun pickImages() {
        val intent = Intent(this, PickImageActivity::class.java)
        intent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 5);
        intent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 1);
        startActivityForResult(intent, PickImageActivity.PICKER_REQUEST_CODE);

    }


    fun getCurrentFragment(): Fragment? {
        return fragmentManager.findFragmentById(R.id.mainContainer)
    }

    fun setMenuVisibility(shows: Boolean) {
        searchMenu?.isVisible = shows
    }


    fun uploadImages(isUpdate: Boolean) {
        startUploaderService(isUpdate)
    }


    private var uploaderService = UploaderService()

    private fun startUploaderService(isUpdate: Boolean) {

        val mServiceIntent = Intent(this, uploaderService::class.java)
        if (localFiles.size > 0) {
            mServiceIntent.putParcelableArrayListExtra("filepath", localFiles)
        }
        mServiceIntent.putExtra("data", obj)
        mServiceIntent.putExtra("action", isUpdate)
        mServiceIntent.setAction("start_service")

        startService(mServiceIntent)
        localFiles.clear()


    }

    fun showProgress(msg: String) {
        progressDialog = ProgressDialog.show(this, getString(R.string.please_wait), msg, true, false)
    }

    fun hideProgress() {
        progressDialog?.dismiss()
    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            when (intent?.action) {
                Constants.ACTION_DONE -> {
                    var currentFrag = getCurrentFragment()
                    if (currentFrag is AgentAddFragment) {
                        hideProgress()
                        fragmentManager.popBackStack()
                    }

                    Handler().postDelayed({

                        when (intent.getIntExtra("type", 0)) {
                            UploaderService.type.ADD_HOTEL -> {
                                hotelEventListFragment?.refreshHotel()
                            }
                            UploaderService.type.ADD_EVENT -> {
                                hotelEventListFragment?.refreshEvent()
                            }
                            UploaderService.type.UPDATE_HOTEL -> {
                                hotelEventListFragment?.refreshHotel()
                                agentShowDetailsFragment.setUpdatedData(intent.getSerializableExtra("obj") as Object)
                            }
                            UploaderService.type.UPDATE_EVENT -> {
                                hotelEventListFragment?.refreshEvent()
                                agentShowDetailsFragment.setUpdatedData(intent.getSerializableExtra("obj") as Object)
                            }
                        }

                    }, 1000)
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadCastReceiver)
    }

    fun clearBackstack() {
        for (i in 0..fragmentManager.backStackEntryCount) {
            if (i != 0) {
                fragmentManager.popBackStack()
            }
        }
    }


}

