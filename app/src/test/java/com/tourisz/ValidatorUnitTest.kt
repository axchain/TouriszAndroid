package com.tourisz

import com.tourisz.util.validator.Validator
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ValidatorUnitTest {

    @Test
    fun email_isCorrect() {
        val loginValidator = Validator()

        assertEquals(true, loginValidator.isEmailValid("nikhil@gmail.com"))

        assertEquals(false, loginValidator.isEmailValid("n@gmail.1"))

        assertEquals(false, loginValidator.isEmailValid("nikhil@gmail.1"))

        assertEquals(false, loginValidator.isEmailValid("nikhilxyz@g.com"))

        assertEquals(false, loginValidator.isEmailValid("nikhil@123"))
    }

}
