package com.tourisz.util.helper

import android.support.transition.Fade
import android.support.transition.Slide
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.tourisz.R

class FragmentHelper{

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHelper()
    }

    fun replaceFragment(fragmentManager : FragmentManager, fragment: Fragment, addToBackstack: Boolean, container: Int) {

        setAnimation(fragment)

        val transaction = fragmentManager.beginTransaction()
        transaction.replace(container, fragment)
        if(addToBackstack) {
            transaction.addToBackStack(fragment::class.java.simpleName)
        }
        transaction.commit()
    }

    fun addFragment(fragmentManager : FragmentManager, fragment: Fragment, addToBackstack: Boolean, container: Int) {

        setAnimation(fragment)

        val transaction = fragmentManager.beginTransaction()
        transaction.add(container, fragment)
        if(addToBackstack) {
            transaction.addToBackStack(fragment::class.java.simpleName)
        }
        transaction.commit()
    }

    fun setAnimation(fragment: Fragment){
        val exitFade = Fade()
        val FADE_DEFAULT_TIME: Long = 100
        val MOVE_DEFAULT_TIME: Long = 10

        exitFade.setDuration(FADE_DEFAULT_TIME)
        fragment.setExitTransition(exitFade)

        val enterFade = Fade()
        enterFade.startDelay = MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME
        enterFade.duration = FADE_DEFAULT_TIME
        fragment.setEnterTransition(enterFade)
    }


}
