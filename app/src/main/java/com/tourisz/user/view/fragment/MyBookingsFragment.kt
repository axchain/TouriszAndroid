package com.tourisz.user.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.*

import com.tourisz.R
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.tourisz.user.view.fragment.child_fragment.MyEventBookingsFragment
import com.tourisz.user.view.fragment.child_fragment.MyHotelBookingsFragment
import android.support.design.widget.TabLayout
import com.tourisz.user.view.activity.HomeActivity
import com.tourisz.util.SearchTypes



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MyBookingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var mView: View

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_bookings, container, false)

        viewPager = mView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = mView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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

    fun onBookedViewPagerChanged(position: Int){
        listener?.onBookedViewPagerChanged(position)
    }

    interface OnFragmentInteractionListener {
        fun onBookedViewPagerChanged(position: Int)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MyBookingsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(MyHotelBookingsFragment.newInstance("", ""), getString(R.string.hotel))
        adapter.addFragment(MyEventBookingsFragment.newInstance("", ""), getString(R.string.events))
        viewPager.adapter = adapter
        onBookedViewPagerChanged(0)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                onBookedViewPagerChanged(position)
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


}
