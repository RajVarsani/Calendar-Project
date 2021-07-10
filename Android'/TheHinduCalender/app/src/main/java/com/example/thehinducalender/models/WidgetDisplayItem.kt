package com.example.thehinducalender.models

import com.example.thehinducalender.R
import java.io.Serializable

class WidgetDisplayItem(
    val imageRes: Int = R.color.g_orange_for_bg,
    val accessStatus: String = "WATCH AD",
    val accessStatusDescription: String = "watch AD to \n" +
            "receive this cool \n" +
            "widget for free"
) : Serializable {
    override fun toString(): String {
        return "imageRes : $imageRes \n" +
                "accessStatus : $accessStatus \n" +
                "accessStatusDescription : $accessStatusDescription \n"
    }
}