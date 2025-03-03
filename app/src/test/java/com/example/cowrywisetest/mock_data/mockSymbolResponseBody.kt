package com.example.cowrywisetest.mock_data

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody

// Mock Symbol API response
val mockSymbolResponseBody = """
    {
        "success": true,
        "symbols": {
            "AED": "United Arab Emirates Dirham",
            "AFN": "Afghan Afghani"
        }
    }
""".trimIndent()