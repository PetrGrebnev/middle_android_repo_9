package ru.yandex.loginapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class LoginViewModelTest {

    companion object {
        const val STRING_EMPTY = ""

        const val INCORRECT_EMAIL = "practicum#yandex.ru"
        const val CORRECT_EMAIL = "practicum@yandex.ru"
        const val PASSWORD = "1234"

        const val DELAY = 3010L
    }

    private lateinit var loginViewModelTest: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        loginViewModelTest = LoginViewModel()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `attempt login with blank field on EmptyFieldsError`() = runTest {
        loginViewModelTest.login(STRING_EMPTY, STRING_EMPTY)

        val state = loginViewModelTest.state.value

        assertEquals(LoginScreenState.EmptyFieldsError, state)
    }

    @Test
    fun `attempt login with incorrect email on EmailValidationError`() = runTest {
        loginViewModelTest.login(INCORRECT_EMAIL, PASSWORD)

        val state = loginViewModelTest.state.value

        assertEquals(LoginScreenState.EmailValidationError, state)
    }

    @Test
    fun `wait state Loading when correct email`() = runTest {
        loginViewModelTest.login(CORRECT_EMAIL, PASSWORD)

        testDispatcher.scheduler.runCurrent()
        val state = loginViewModelTest.state.value

        assertEquals(LoginScreenState.Loading, state)
    }

    @Test
    fun `attempt login when correct email and password on Success`() = runTest {
        loginViewModelTest.login(CORRECT_EMAIL, PASSWORD)

        testDispatcher.scheduler.runCurrent()
        testDispatcher.scheduler.advanceTimeBy(DELAY)
        val state = loginViewModelTest.state.value

        assertEquals(LoginScreenState.Success, state)
    }
}