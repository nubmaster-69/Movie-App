package com.hisu.movieapp.utils

import java.text.SimpleDateFormat
import java.util.*

class MyFormatUtils {
    companion object {
        fun dateFormat(dateStr: String): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.US)
            val date = dateFormat.parse(dateStr)!!
            return outputFormat.format(date)
        }

        fun ratingFormat(voteAvg: Double) = String.format("%.1f", voteAvg)

        fun voteCountFormat(voteCount: Int) = "(${voteCount} reviews)"

        fun runtimeFormat(runtime: Int) = "${runtime/60}h ${runtime%60}m"
    }
}