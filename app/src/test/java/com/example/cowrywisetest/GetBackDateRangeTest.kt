package com.example.cowrywisetest

import com.example.cowrywisetest.utils.CalendarWrapper
import com.example.cowrywisetest.utils.DateUtil
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.Calendar

class GetBackDateRangeTest {

    @Test
    fun testGetBackDateRange() {
        val calendarWrapper = mock(CalendarWrapper::class.java)

        // Create a mock today's date for consistent testing
        val mockToday = Calendar.getInstance().apply {
            set(2023, Calendar.OCTOBER, 5) // Set today's date to 2023-10-05
        }

        // Mock the behavior of CalendarWrapper.getInstance()
        `when`(calendarWrapper.getInstance()).thenReturn(mockToday)

        // Test case: 30 days back from 2023-10-05
        val noOfDayBack = 30
        val expectedDates = listOf(
            "2023-09-05", "2023-09-06", "2023-09-07", "2023-09-08", "2023-09-09",
            "2023-09-10", "2023-09-11", "2023-09-12", "2023-09-13", "2023-09-14",
            "2023-09-15", "2023-09-16", "2023-09-17", "2023-09-18", "2023-09-19",
            "2023-09-20", "2023-09-21", "2023-09-22", "2023-09-23", "2023-09-24",
            "2023-09-25", "2023-09-26", "2023-09-27", "2023-09-28", "2023-09-29",
            "2023-09-30", "2023-10-01", "2023-10-02", "2023-10-03", "2023-10-04",
            "2023-10-05"
        )

        // Call the function
        val actualDates = DateUtil.getBackDateRange(calendarWrapper, noOfDayBack)

        // Assert that the actual output matches the expected output
        assertEquals(expectedDates, actualDates)


    }
}