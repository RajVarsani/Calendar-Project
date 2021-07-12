package com.example.thehinducalender.models

import java.io.Serializable
import java.net.URL


class Festival(
    var isShort: Boolean = true,
    var name: String = "",
    var date: String = "",
    var isHoliday: Boolean = false,
    var description: String = "",
    var wikiLink: URL = URL("https://en.wikipedia.org/"),
    var imagesLink: Array<String> = arrayOf("", "", "")
) : Serializable {


    override fun toString(): String {
        return "isShort = ${this.isShort} \n" +
                "name = ${this.name} \n" +
                "date = ${this.date} \n" +
                "isHoliday = ${this.isHoliday} \n" +
                "description = ${this.description} \n" +
                "wikiLink = ${this.wikiLink} \n" +
                "imagesLink = ${this.imagesLink}"
    }


}
//class Festival constructor(isShort: Boolean, name: String, date: String)
//
//class Festival constructor(
//    isShort: Boolean,
//    name: String,
//    date: String,
//    isHoliday: Boolean,
//    description: String,
//    wikiLink: URL,
//    imageLink: Array<String>
//)