package com.tourisz.admin.view.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import android.widget.Button
import com.android.volley.Request
import com.android.volley.Response
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.nileshp.multiphotopicker.photopicker.activity.PickImageActivity
import com.tourisz.Application
import com.tourisz.R
import com.tourisz.admin.view.fragment.*
import com.tourisz.agent.view.fragment.AgentAddFragment
import com.tourisz.agent.view.fragment.AgentShowDetailsFragment
import com.tourisz.agent.view.fragment.child_fragment.EventsFragment
import com.tourisz.agent.view.fragment.child_fragment.HotelsFragment
import com.tourisz.api.URLS
import com.tourisz.api.request.AddFlyerRequest
import com.tourisz.api.request.Object
import com.tourisz.api.request.UpdateFlyerRequest
import com.tourisz.api.response.*
import com.tourisz.api.util.JsonObjectRequestWithHeader
import com.tourisz.entity.LocalFile
import com.tourisz.service.UploaderService
import com.tourisz.user.view.activity.BaseActivity
import com.tourisz.user.view.activity.SplashActivity
import com.tourisz.user.view.fragment.AboutFragment
import com.tourisz.user.view.fragment.ContactFragment
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
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class AdminHomeActivity : BaseActivity(), ProfileFragment.OnFragmentInteractionListener, AddAgentFragment.OnFragmentInteractionListener, AgentsFragment.OnFragmentInteractionListener, View.OnClickListener, UserDetailsFragment.OnFragmentInteractionListener, UsersFragment.OnFragmentInteractionListener, AddUserFragment.OnFragmentInteractionListener, HotelsFragment.OnFragmentInteractionListener, AgentShowDetailsFragment.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener, AgentDetailsFragment.OnFragmentInteractionListener, NumberPicker.OnValueChangeListener, SearchFragment.OnFragmentInteractionListener, EventsFragment.OnFragmentInteractionListener, AgentAddFragment.OnFragmentInteractionListener, HotelBookingsFragment.OnFragmentInteractionListener, EventBookingsFragment.OnFragmentInteractionListener, TransactionFragment.OnFragmentInteractionListener, FlyersFragment.OnFragmentInteractionListener, AddFlyerFragment.OnFragmentInteractionListener {

    private lateinit var txtTopTitle: TextView
    private lateinit var imgTopLogo: ImageView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var homeFragment: AdminHomeFragment
    private lateinit var agentDetailsFragment: AgentDetailsFragment
    private var searchMenu: MenuItem? = null
    private lateinit var fab: FloatingActionButton
    private lateinit var addFragment: AgentAddFragment
    private lateinit var addFlyerFragment: AddFlyerFragment
    private var searchType: Int = SearchTypes.ADMIN_USERS
    private lateinit var hotelsFragment: HotelsFragment
    private lateinit var eventsFragment: EventsFragment
    private lateinit var usersFragment: UsersFragment
    private lateinit var agentShowDetailsFragment: AgentShowDetailsFragment
    private lateinit var userDetailsFragment: UserDetailsFragment
    private var isEditMode = false
    private var isHotelFlow = true
    private var transactionTitle: String = ""
    private lateinit var obj: Object
    private lateinit var progressDialog: ProgressDialog
    private lateinit var agentsFragment: AgentsFragment
    private lateinit var flyersFragment: FlyersFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)
        setUp()
        initIds()
        registerBroadcast()
        setTopLogo()
        homeFragment = AdminHomeFragment.newInstance("", "")
        addFragment(homeFragment, false)

        fragmentManager.addOnBackStackChangedListener(this)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        searchMenu = menu?.findItem(R.id.action_search)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_search) {
            addSearchFragment(SearchFragment.newInstance(searchType, ""), true)

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackStackChanged() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is AdminHomeFragment) {
            setTopLogo()
            setMenuVisibility(false)
            fab.hide()
        } else if (currentFragment is UsersFragment) {
            setTopTitle(getString(R.string.users))
            setMenuVisibility(true)
            fab.show()
            searchType = SearchTypes.ADMIN_USERS
        } else if (currentFragment is UserDetailsFragment) {
            setTopTitle(getString(R.string.details))
            setMenuVisibility(true)
            fab.hide()
            searchType = SearchTypes.ADMIN_USERS
        } else if (currentFragment is BookingDetailsFragment) {
            setTopTitle(getString(R.string.booking_details))
            setMenuVisibility(false)
            fab.hide()
        } else if (currentFragment is AgentDetailsFragment) {
            setTopTitle(getString(R.string.details))
            setMenuVisibility(true)
            fab.hide()
            searchType = SearchTypes.ADMIN_AGNETS
        } else if (currentFragment is AddUserFragment) {
            if (isEditMode) {
                setTopTitle(getString(R.string.edit_user))
            } else {
                setTopTitle(getString(R.string.add_user))
            }
            setMenuVisibility(false)
            fab.hide()
        } else if (currentFragment is AddAgentFragment) {
            if (isEditMode) {
                setTopTitle(getString(R.string.edit_agent))
            } else {
                setTopTitle(getString(R.string.add_agent))
            }
            setMenuVisibility(false)
            fab.hide()
        } else if (currentFragment is AgentsFragment) {
            setTopTitle(getString(R.string.agents))
            setMenuVisibility(true)
            fab.show()
            searchType = SearchTypes.ADMIN_AGNETS
        } else if (currentFragment is HotelsFragment) {
            setTopTitle(getString(R.string.hotels))
            setMenuVisibility(true)
            fab.show()
            isHotelFlow = true
            searchType = SearchTypes.ADMIN_HOTELS
        } else if (currentFragment is EventsFragment) {
            setTopTitle(getString(R.string.events))
            setMenuVisibility(true)
            fab.show()
            isHotelFlow = false
            searchType = SearchTypes.ADMIN_EVENTS
        } else if (currentFragment is AgentShowDetailsFragment) {
            setTopTitle(getString(R.string.details))
            setMenuVisibility(true)
            fab.hide()
        } else if (currentFragment is AgentAddFragment) {
            if (isHotelFlow) {
                setTopTitle(if (isEditMode) getString(R.string.edit_hotel)
                else getString(R.string.add_hotel))
            } else {
                setTopTitle(if (isEditMode) getString(R.string.edit_event)
                else getString(R.string.add_event))
            }
            setMenuVisibility(false)
            fab.hide()
        } else if (currentFragment is HotelBookingsFragment) {
            setTopTitle(getString(R.string.hotel_bookings))
            setMenuVisibility(false)
            fab.hide()
            searchType = SearchTypes.ADMIN_HOTEL_BOOKINGS
        } else if (currentFragment is EventBookingsFragment) {
            setTopTitle(getString(R.string.event_bookings))
            setMenuVisibility(false)
            fab.hide()
            searchType = SearchTypes.ADMIN_EVENT_BOOKINGS
        } else if (currentFragment is TransactionFragment) {
            setTopTitle(transactionTitle)
            setMenuVisibility(false)
            fab.hide()
        } else if (currentFragment is TransactionDetailsFragment) {
            setTopTitle(getString(R.string.details))
            setMenuVisibility(false)
            fab.hide()
        } else if (currentFragment is FlyersFragment) {
            setTopTitle(getString(R.string.flyer))
            setMenuVisibility(false)
            fab.show()
            searchType = SearchTypes.ADMIN_FLYER
        } else if (currentFragment is AddFlyerFragment) {
            setTopTitle(transactionTitle)
            setMenuVisibility(false)
            fab.hide()
        } else if (currentFragment is AboutFragment) {
            setTopTitle(getString(R.string.about_us))
            setMenuVisibility(false)
            fab.hide()
        } else if (currentFragment is ContactFragment) {
            setTopTitle(getString(R.string.contact_us))
            setMenuVisibility(false)
            fab.hide()
        } else if (currentFragment is TnCFragment) {
            setTopTitle(getString(R.string.terms))
            setMenuVisibility(false)
            fab.hide()
        } else if (currentFragment is ProfileFragment) {
            setTopTitle(getString(R.string.my_profile))
            setMenuVisibility(false)
        } else {
            setMenuVisibility(false)
        }
        AndroidHelper.hideKeyboard(this)
    }

    private fun setUp() {
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        mToolbar?.title = ""
        setSupportActionBar(mToolbar)
        mToolbar?.setNavigationOnClickListener { toggleDrawer() }

        fragmentManager = supportFragmentManager
    }

    private fun initIds() {
        fab = findViewById(R.id.fab)
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


    override fun onClick(view: View) {
        when (view.id) {
            R.id.txtNavHome -> openHome()
            R.id.txtNavUser -> openUsers()
            R.id.txtNavAgent -> openAgents()
            R.id.txtNavHotels -> openHotels()
            R.id.txtNavEvents -> openEvents()
            R.id.txtNavHotelBookings -> openHotelBookings()
            R.id.txtNavEventBookings -> openEventBookings()
            R.id.txtNavHotelTransactions -> openHotelTransactions()
            R.id.txtNavEventTransactions -> openEventTransactions()
            R.id.txtNavAbout -> openAbout()
            R.id.txtNavTermsAndCondi -> openTermsAndCondi()
            R.id.txtNavFlyer -> openFlyer()
            R.id.txtNavContact -> openContact()
            R.id.txtNavLogout -> logout()
            R.id.txtNavChangePassword -> openProfile()

            R.id.fab -> openAddScreen()
        }
    }

    private fun openAddScreen() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is UsersFragment) {
            addFragment(AddUserFragment.newInstance(null, false), true)
        } else if (currentFragment is AgentsFragment) {
            addFragment(AddAgentFragment.newInstance(null, false), true)
        } else if (currentFragment is HotelsFragment) {
            isEditMode = false
            addFragment = AgentAddFragment.newInstance(HotelData(), null)
            addFragment(addFragment, true)

        } else if (currentFragment is EventsFragment) {
            isEditMode = false
            addFragment = AgentAddFragment.newInstance(null, EventData())
            addFragment(addFragment, true)
        } else if (currentFragment is FlyersFragment) {
            transactionTitle = getString(R.string.add_flyer)
            addFlyerFragment = AddFlyerFragment.newInstance(null, false)
            addFragment(addFlyerFragment, true)
        }
    }

    private fun toggleDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        AndroidHelper.hideKeyboard(this)
    }


    private fun openHome() {
        setTopLogo()
        toggleDrawer()
        showHome()
    }


    private fun showHome() {
        val currentFragment = getCurrentFragment()
        if (currentFragment !is AdminHomeFragment) {
            clearBackstack()
        }

    }

    private fun openUsers() {
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is UsersFragment) {
            clearBackstack()
            setMenuVisibility(false)
            usersFragment = UsersFragment.newInstance("", "")
            addFragment(usersFragment, true)
        }
    }

    private fun openAgents() {
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is AgentsFragment) {
            clearBackstack()
            setMenuVisibility(false)
            agentsFragment = AgentsFragment.newInstance("", "")
            addFragment(agentsFragment, true)
        }
    }

    private fun openHotels() {
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is HotelsFragment) {
            clearBackstack()
            setMenuVisibility(false)
            hotelsFragment = HotelsFragment.newInstance("", "")
            addFragment(hotelsFragment, true)
        }
    }

    private fun openEvents() {
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is EventsFragment) {
            clearBackstack()
            setMenuVisibility(false)
            eventsFragment = EventsFragment.newInstance("", "")
            addFragment(eventsFragment, true)
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

    private fun openHotelTransactions() {
        searchType = SearchTypes.ADMIN_HOTEL_TRANSACTIONS
        transactionTitle = getString(R.string.hotel_transactions)
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is TransactionFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(TransactionFragment.newInstance(BookingTypes.HOTEL, ""), true)
        } else {
            fragmentManager.popBackStack()
            setMenuVisibility(false)
            addFragment(TransactionFragment.newInstance(BookingTypes.HOTEL, ""), true)
        }
    }

    private fun openEventTransactions() {
        searchType = SearchTypes.ADMIN_EVENT_TRANSACTIONS
        transactionTitle = getString(R.string.event_transactions)
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is TransactionFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(TransactionFragment.newInstance(BookingTypes.EVENT, ""), true)
        } else {
            fragmentManager.popBackStack()
            setMenuVisibility(false)
            addFragment(TransactionFragment.newInstance(BookingTypes.EVENT, ""), true)
        }
    }


    private fun openAbout() {
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is AboutFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(AboutFragment.newInstance(true, ""), true)
        }
    }


    private fun openTermsAndCondi() {
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is TnCFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(TnCFragment.newInstance(true, ""), true)
        }
    }

    private fun openFlyer() {
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is FlyersFragment) {
            clearBackstack()
            setMenuVisibility(false)
            flyersFragment = FlyersFragment.newInstance("", "")
            addFragment(flyersFragment, true)
        }
    }

    private fun openContact() {
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is ContactFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(ContactFragment.newInstance(true, ""), true)
        }
    }

    private fun openProfile() {
        toggleDrawer()
        val currentFragment = getCurrentFragment()
        if (currentFragment !is ProfileFragment) {
            clearBackstack()
            setMenuVisibility(false)
            addFragment(ProfileFragment.newInstance("", ""), true)
        }
    }


    private fun logout() {
        AndroidHelper.addSharedPreference(this, Constants.USER_DATA, "")
        toggleDrawer()
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }


    override fun onUserListItemClick(usersData: UsersData) {
        userDetailsFragment = UserDetailsFragment.newInstance(usersData, "")
        addFragment(userDetailsFragment, true)
    }

    override fun onUserDeleteClicked(usersData: UsersData?) {
        showDeleteConfirmDialog(usersData, true)
    }


    override fun onUserEditClicked(usersData: UsersData?) {
        isEditMode = true
        addFragment(AddUserFragment.newInstance(usersData, true), true)

    }


    override fun onUserCreateSaveClicked(usersData: UsersData?, isEditMode: Boolean) {
        if (usersData != null) {
            if (isEditMode) {
                userDetailsFragment?.setData(usersData)
                callAddUpdateUserAPI(URLS.newInstance().UPDATE_USER(), usersData)
            } else {
                callAddUpdateUserAPI(URLS.newInstance().SIGNUP(), usersData)
            }
        }
    }


    override fun onAgentCreateSaveClicked(agentsData: AgentsData?, isEditMode: Boolean) {
        if (agentsData != null) {
            if (isEditMode) {
                agentDetailsFragment?.setData(agentsData)
                callAddUpdateUserAPI(URLS.newInstance().UPDATE_USER(), agentsData)
            } else {
                callAddUpdateUserAPI(URLS.newInstance().SIGNUP(), agentsData)
            }
        }
    }

    override fun onUserDpChangeClicked() {
        showSelectImgFromDialog()
    }


    override fun onAgentsListItemClick(agentsData: AgentsData) {
        agentDetailsFragment = AgentDetailsFragment.newInstance(agentsData, "")
        addFragment(agentDetailsFragment, true)

    }

    override fun onAgentDeleteClicked(agentsData: AgentsData?) {
        showDeleteConfirmDialog(agentsData, true)
    }

    override fun onAgentEditClicked(agentsData: AgentsData?) {
        isEditMode = true
        if (agentsData == null)
            return
        addFragment(AddAgentFragment.newInstance(agentsData, true), true)
    }


    override fun onValueChange(picker: NumberPicker?, old: Int, new: Int) {
    }


    override fun onSearchItemClicked(obj: Object, searchType: Int) {

        fragmentManager.popBackStack()
        if (obj is HotelData) {
            agentShowDetailsFragment = AgentShowDetailsFragment.newInstance(obj, null)
            replaceFragment(agentShowDetailsFragment, true)

        } else if (obj is EventData) {
            agentShowDetailsFragment = AgentShowDetailsFragment.newInstance(null, obj)
            replaceFragment(agentShowDetailsFragment, true)
        } else if (obj is UsersData) {
            userDetailsFragment = UserDetailsFragment.newInstance(obj, "")
            replaceFragment(userDetailsFragment, true)
        } else if (obj is AgentsData) {
            agentDetailsFragment = AgentDetailsFragment.newInstance(obj, "")
            replaceFragment(agentDetailsFragment, true)
        }


    }

    override fun onSearchBookClicked(obj: Object, searchType: Int) {

    }

    override fun onAddressClicked() {
        try {
            val typeFilter = AutocompleteFilter.Builder()
                   // .setCountry("IN")
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

    override fun onHotelListItemClick(hotelBooking: HotelData) {
        searchType = SearchTypes.ADMIN_HOTELS
        agentShowDetailsFragment = AgentShowDetailsFragment.newInstance(hotelBooking, null)
        addFragment(agentShowDetailsFragment, true)
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

    override fun onHotelEventDeleteClicked(hotelData: HotelData?, eventData: EventData?) {

        showDeleteConfirmDialog(if (hotelData != null) hotelData else eventData, true)

    }

    override fun onHotelEventEditClicked(hotelData: HotelData?, eventData: EventData?) {
        isEditMode = true
        addFragment = AgentAddFragment.newInstance(hotelData, eventData)
        addFragment(addFragment, true)
    }

    override fun onAddImageClicked() {
        pickImages()
    }

    var dateTime: String = ""

    override fun onEventDateTimeClicked() {
        showDatePickerDialog()
    }


    override fun onEventListItemClick(eventData: EventData) {
        searchType = SearchTypes.ADMIN_EVENTS
        agentShowDetailsFragment = AgentShowDetailsFragment.newInstance(null, eventData)
        addFragment(agentShowDetailsFragment, true)
    }


    override fun onHotelBookingListItemClick(hotelBooking: HotelBookingData) {
        searchType = SearchTypes.ADMIN_HOTEL_BOOKINGS
        addFragment(BookingDetailsFragment.newInstance(hotelBooking, ""), true)
    }


    override fun onEventBookingListItemClick(eventBookingData: EventBookingData) {
        searchType = SearchTypes.ADMIN_EVENT_BOOKINGS
        addFragment(BookingDetailsFragment.newInstance(eventBookingData, ""), true)
    }

    override fun onTranactionClick(transaction: Object?) {
        if (transaction != null) {
            addFragment(BookingDetailsFragment.newInstance(transaction, ""), true)
        }
    }

    override fun onFlyerListDeleteClick(flyerData: FlyerData) {
        showDeleteConfirmDialog(flyerData, false)
    }

    override fun onFlyerListEditClick(flyerData: FlyerData) {
        transactionTitle = getString(R.string.edit_flyer)
        addFlyerFragment = AddFlyerFragment.newInstance(flyerData, true)
        addFragment(addFlyerFragment, true)
    }

    override fun onFlyerListItemClick(flyerData: FlyerData) {
        showImageDialog(URLS.newInstance().FLYERSIMG_URL + flyerData.poster)

    }



    override fun onChangePasswordClicked(userData: UserData, password: String) {

    }

    override fun onChangeAdminPasswordClicked(oldPass: String, newPass: String) {
        if (AndroidHelper.isNetworkAvailable(this)) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.change_pass_confirm))
            builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                callChangePasswordAPI(oldPass, newPass)
            }

            builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        } else {
            AndroidHelper.showToast(this, getString(R.string.no_internet))
        }    }


    override fun onFlyerChangeClicked() {
        pickImage()

        // showSelectImgFromDialog()
    }


    override fun onFlyerCreateSaveClicked(flyerData: FlyerData?, isEditMode: Boolean, localFile: LocalFile?) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.submit_confirm))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->

            if (flyerData != null && AndroidHelper.isNetworkAvailable(this)) {
                showProgress("")
                if (isEditMode) {
                    var updateFlyerRequest = UpdateFlyerRequest()
                    if(!flyerData?.flyerPosition.isEmpty()) {
                        updateFlyerRequest.flyerPosition = flyerData?.flyerPosition?.toInt()
                    }
                    updateFlyerRequest.title = flyerData?.title
                    updateFlyerRequest.id = flyerData?.id.toInt()
                    updateFlyerRequest.status = flyerData?.status.toInt()
                    updateFlyerRequest.poster = flyerData?.poster

                    obj = updateFlyerRequest
                   // addFlyerFragment.setData(flyerData)


                } else {
                    var updateFlyerRequest = AddFlyerRequest()
                    if(!flyerData?.flyerPosition.isEmpty()) {
                        updateFlyerRequest.flyerPosition = flyerData?.flyerPosition?.toInt()
                    }
                    updateFlyerRequest.title = flyerData?.title
                    updateFlyerRequest.status = flyerData?.status.toInt()

                    obj = updateFlyerRequest
                }
                startUploaderService(isEditMode)

            } else {
                AndroidHelper.showToast(this, getString(R.string.no_internet))
            }
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }


    private var datePickerFragment: DatePickerFragment? = null
    fun showDatePickerDialog() {

        if (datePickerFragment == null) {
            datePickerFragment = DatePickerFragment(dateSelectListener, System.currentTimeMillis())
            datePickerFragment?.show(fragmentManager, "datePicker")
        } else {
            datePickerFragment?.dismiss()
            datePickerFragment = DatePickerFragment(dateSelectListener, System.currentTimeMillis())
            datePickerFragment?.show(fragmentManager, "datePicker")

        }
    }

    var selDate= Date()

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
            Log.e("date",date)
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


    fun showSelectImgFromDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.select_photo_from))
        builder.setPositiveButton(getString(R.string.camera)) { _, _ ->
            openCamera()
        }

        builder.setNegativeButton(getString(R.string.gallery)) { _, _ ->
            pickImage()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun showSubmitConfirmDialog(obj: Object?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.submit_confirm))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            if (obj is FlyerData) {
                AndroidHelper.showToast(this, "Submit successfully!")
                fragmentManager.popBackStack()
            }
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showDeleteConfirmDialog(obj: Object?, goToBack: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.delete_confirm))
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            if (AndroidHelper.isNetworkAvailable(this)) {
                callDeleteAPI(obj, goToBack)

            } else {
                AndroidHelper.showToast(this, getString(R.string.no_internet))
            }
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showImageDialog(path: String) {

        Log.e("name", path)
        val dialog = Dialog(this, android.R.style.Theme)
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black_trans_dialog)))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.show_image_dialog)
        dialog.setCancelable(false)
        var img = dialog.findViewById(R.id.img) as ImageView
        Glide.with(this).load(path)
                .placeholder(R.drawable.ic_hotelplaceholder)
                .error(R.drawable.ic_hotelplaceholder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img)

        var btnOk = dialog.findViewById(R.id.btnOk) as Button
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


    private fun registerBroadcast() {
        registerReceiver(broadCastReceiver, IntentFilter(Constants.ACTION_DONE))
    }

    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_REQUEST_CODE = 2
    private val PICK_PHOTO_FOR_AVATAR = 3

    private fun openCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA),
                        CAMERA_REQUEST_CODE)
            } else {
                dispatchTakePictureIntent()
            }
        } else {
            dispatchTakePictureIntent()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    internal var PLACE_AUTOCOMPLETE_REQUEST_CODE = 125

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val bm = extras?.get("data") as Bitmap
            setUserImage(bm)

        } else if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }
            val inputStream = getContentResolver().openInputStream(data.data)
            val bm = BitmapFactory.decodeStream(inputStream)
            setUserImage(bm)
        } else if (requestCode == PickImageActivity.PICKER_REQUEST_CODE) {

            var pathList: ArrayList<String>

            if (data?.getStringArrayListExtra(PickImageActivity.KEY_DATA_RESULT) != null) {

                pathList = data.getStringArrayListExtra(PickImageActivity.KEY_DATA_RESULT)!!
                Log.e("size", "${pathList.size}")

                localFiles.clear()
                for (path in pathList) {
                    localFiles.add(LocalFile(AndroidHelper.getFileNameFromPath(path), path, 0))
                }
                var currentFrag = getCurrentFragment()
                if (currentFrag is AgentAddFragment) {
                    addFragment.setAdapter(localFiles)
                } else if (currentFrag is AddFlyerFragment) {
                    addFlyerFragment.setImage(localFiles[0])
                }
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

    private fun setUserImage(imageBitmap: Bitmap) {
        val currentFragment = getCurrentFragment()
        if (currentFragment is AddUserFragment) {
            currentFragment.setImage(imageBitmap)
        }
    }


    fun pickImages() {
        val intent = Intent(this, PickImageActivity::class.java)
        intent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 5);
        intent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 1);
        startActivityForResult(intent, PickImageActivity.PICKER_REQUEST_CODE);

    }


    private fun pickImage() {
        val intent = Intent(this, PickImageActivity::class.java)
        intent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 1);
        intent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 1);
        startActivityForResult(intent, PickImageActivity.PICKER_REQUEST_CODE);
    }


    private fun getCurrentFragment(): Fragment? {
        return fragmentManager.findFragmentById(R.id.mainContainer)
    }

    private fun setMenuVisibility(shows: Boolean) {
        searchMenu?.isVisible = shows
    }


    private fun clearBackstack() {
        for (i in 0..fragmentManager.backStackEntryCount) {
            if (i != 0) {
                fragmentManager.popBackStack()
            }
        }
    }


    private fun setTopTitle(title: String) {
        imgTopLogo.visibility = View.GONE
        txtTopTitle.visibility = View.VISIBLE
        txtTopTitle.text = title
        isEditMode = false
    }

    private fun setTopLogo() {
        imgTopLogo.visibility = View.VISIBLE
        txtTopTitle.visibility = View.GONE
    }


    private fun addSearchFragment(fragment: Fragment, addToBackstack: Boolean) {
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack()
        }
        FragmentHelper.newInstance().addFragment(fragmentManager, fragment, addToBackstack, R.id.homeMainContainer)
    }

    private fun addFragment(fragment: Fragment, addToBackstack: Boolean) {
        FragmentHelper.newInstance().addFragment(fragmentManager, fragment, addToBackstack, R.id.mainContainer)
    }

    private fun replaceFragment(fragment: Fragment, addToBackstack: Boolean) {
        FragmentHelper.newInstance().replaceFragment(fragmentManager, fragment, addToBackstack, R.id.mainContainer)
    }

    fun uploadImages(isUpdate: Boolean) {
        startUploaderService(isUpdate)
    }

    var localFiles: ArrayList<LocalFile> = ArrayList()

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
                    hideProgress()

                    val currentFrag = getCurrentFragment()
                    if (currentFrag is AgentAddFragment) {
                        fragmentManager.popBackStack()
                    }

                    Handler().postDelayed({

                        when (intent.getIntExtra("type", 0)) {
                            UploaderService.type.ADD_HOTEL -> {
                                hotelsFragment?.callAPI()
                            }
                            UploaderService.type.ADD_EVENT -> {
                                eventsFragment?.callAPI()
                            }
                            UploaderService.type.UPDATE_HOTEL -> {
                                hotelsFragment?.callAPI()
                                agentShowDetailsFragment.setUpdatedData(intent.getSerializableExtra("obj") as Object)
                            }
                            UploaderService.type.UPDATE_EVENT -> {
                                eventsFragment?.callAPI()
                                agentShowDetailsFragment.setUpdatedData(intent.getSerializableExtra("obj") as Object)
                            }
                            UploaderService.type.UPDATE_FLYER -> {
                                fragmentManager.popBackStack()
                                flyersFragment?.callAPI()
                            }
                            UploaderService.type.ADD_FLYER -> {
                                fragmentManager.popBackStack()
                                flyersFragment?.callAPI()
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


    private fun callDeleteAPI(obj: Object?, goToBack: Boolean) {

        var url = ""
        var jsonObj = JSONObject()

        if (obj is HotelData) {
            jsonObj.put("id", obj.id.toInt())
            url = URLS.newInstance().DELETE_HOTEL()

        } else if (obj is EventData) {
            jsonObj.put("id", obj.id.toInt())
            url = URLS.newInstance().DELETE_EVENT()
        } else if (obj is UsersData) {
            obj.status = 2.toString()
            jsonObj = JSONObject(AndroidHelper.objectToString(obj))
            url = URLS.newInstance().UPDATE_USER()
        } else if (obj is AgentsData) {
            obj.status = 2.toString()
            jsonObj = JSONObject(AndroidHelper.objectToString(obj))
            url = URLS.newInstance().UPDATE_USER()
        } else if (obj is FlyerData) {
            jsonObj = JSONObject(AndroidHelper.objectToString(obj))
            url = URLS.newInstance().DELETE_FLYER()
        }

        Log.e("req", jsonObj.toString())

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                url, jsonObj,
                Response.Listener<JSONObject> { response ->
                    Log.e("Response", response.toString())
                    AndroidHelper.showToast(this, response.getString("response"))

                    if (response.getBoolean("result")) {
                        Handler().postDelayed({
                            if (obj is HotelData) {
                                hotelsFragment?.callAPI()
                            } else if (obj is EventData) {
                                eventsFragment?.callAPI()
                            } else if (obj is UsersData) {
                                usersFragment?.callAPI()
                            } else if (obj is AgentsData) {
                                agentsFragment?.callAPI()
                            } else if (obj is FlyerData) {
                                flyersFragment?.callAPI()
                            }
                        }, 1000)

                    }
                    if (goToBack) fragmentManager.popBackStack()

                }, Response.ErrorListener { error ->

            AndroidHelper.showToast(this, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }


    private fun callAddUpdateUserAPI(url: String, obj: Object) {

        val jsonObj = JSONObject(AndroidHelper.objectToString(obj))
        Log.e("url", url)
        Log.e("req", jsonObj.toString())

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                url, jsonObj,
                Response.Listener<JSONObject> { response ->
                    Log.e("Response", response.toString())
                    AndroidHelper.showToast(this, response.getString("response"))

                    if (response.getBoolean("result")) {
                        Handler().postDelayed({
                            if (obj is UsersData) {
                                usersFragment?.callAPI()
                            } else if (obj is AgentsData) {
                                agentsFragment?.callAPI()

                            }
                        }, 1000)

                    }
                    fragmentManager.popBackStack()

                }, Response.ErrorListener { error ->

            AndroidHelper.showToast(this, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)

    }


    fun callChangePasswordAPI(oldPass: String, newPass: String) {
        showProgress("Updating password..")

        var jsonObj = JSONObject()
        jsonObj.put("oldPassword", oldPass)
        jsonObj.put("newPassword", newPass)

        Log.e("req", jsonObj.toString())

        val jsonObjReq = JsonObjectRequestWithHeader(Request.Method.POST,
                URLS.newInstance().CHANGE_ADMIN_PASS(), jsonObj,
                Response.Listener<JSONObject> { response ->
                    Log.e("Response", response.toString())
                    hideProgress()
                    AndroidHelper.showToast(this, response.getString("response"))

                }, Response.ErrorListener { error ->
            hideProgress()
            AndroidHelper.showToast(this, "Error")

        })

        Application.getInstance().addToRequestQueue(jsonObjReq)


    }
}

