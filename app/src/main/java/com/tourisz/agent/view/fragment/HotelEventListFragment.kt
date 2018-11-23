package com.tourisz.agent.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.*

import com.tourisz.R
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.tourisz.user.view.fragment.child_fragment.MyEventBookingsFragment
import com.tourisz.user.view.fragment.child_fragment.MyHotelBookingsFragment
import android.support.design.widget.TabLayout
import com.tourisz.agent.view.fragment.child_fragment.EventsFragment
import com.tourisz.agent.view.fragment.child_fragment.HotelsFragment
import com.tourisz.user.view.activity.HomeActivity
import com.tourisz.util.SearchTypes


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HotelEventListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int = 0
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var mView: View

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_agent_hotel_event_list, container, false)
        fab = mView.findViewById(R.id.fab)

        viewPager = mView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = mView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                onListPagerChanged(position)
            }
        })

        fab.setOnClickListener { openAdd() }


        viewPager.setCurrentItem(param1)

        return mView
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

    fun onListPagerChanged(position: Int){
        listener?.onListPagerChanged(position)
    }
    fun openAdd(){
        listener?.openAdd()
    }
    fun setSearchType(position: Int){
        listener?.setSearchType(position)
    }
    interface OnFragmentInteractionListener {
        fun onListPagerChanged(position: Int)
        fun openAdd()
        fun setSearchType(type: Int)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
                HotelEventListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private lateinit var hotelsFragment: HotelsFragment
    private lateinit var eventsFragment: EventsFragment

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        hotelsFragment = HotelsFragment.newInstance("", "")
        eventsFragment = EventsFragment.newInstance("", "")
        adapter.addFragment(hotelsFragment, getString(R.string.hotel))
        adapter.addFragment(eventsFragment, getString(R.string.events))
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                fab.hide()
                Handler().postDelayed({ fab.show() }, 300)
                setSearchType( if (position == 0) SearchTypes.AGNET_HOTELS else SearchTypes.AGNET_EVENTS)

            }
        })

    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList: ArrayList<Fragment> = ArrayList()
        private val mFragmentTitleList: ArrayList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position)
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList.get(position)
        }
    }


    fun setCurrentItem(position: Int){
        viewPager.setCurrentItem(position)

    }

    fun refreshHotel(){
        hotelsFragment.callAPI()
    }

    fun refreshEvent(){
        eventsFragment.callAPI()
    }

}
