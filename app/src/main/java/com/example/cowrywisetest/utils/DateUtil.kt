package com.example.cowrywisetest.utils

import android.annotation.SuppressLint
import androidx.core.util.TimeUtils
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.util.Calendar

class DateUtil {
    companion object {
        fun getBackDateRange(calendarWrapper: CalendarWrapper,noOfDayBack: Int): List<String> {
            // Get today's date
            val calendar = calendarWrapper.getInstance()

            // Calculate the date 30 days ago
            val thirtyDaysAgo = calendar.clone() as Calendar
            thirtyDaysAgo.add(Calendar.DAY_OF_YEAR, -noOfDayBack)

            // Generate a list of dates between 30 days ago and today
            val dateRange = generateDateRange(thirtyDaysAgo, calendar)

            // Print the list of dates
            println("Dates between ${thirtyDaysAgo.time} and ${calendar.time}:")
            return dateRange
        }

        /** Generates a list of dates between two given dates (inclusive).
         *
         * @param startDate The start date of the range.
         * @param endDate The end date of the range.
         * @return A list of LocalDate objects representing the dates in the range.
         */
        @SuppressLint("SimpleDateFormat")
        private fun generateDateRange(startDate: Calendar, endDate: Calendar): List<String> {
            val dates = mutableListOf<String>()
            val currentDate = startDate.clone() as Calendar
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")

            // Loop through each date from startDate to endDate
            while (currentDate <= endDate) {
                dates.add(dateFormat.format(currentDate.time)) // Add formatted date to the list
                currentDate.add(Calendar.DAY_OF_YEAR, 1) // Move to the next day
            }

            return dates
        }
    }
}