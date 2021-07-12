package com.example.thehinducalender.models

class HomePageFestivalListModel(
    var name: String = "",
    var description: String = ""
) {
    override fun toString(): String {
        return "name = ${this.name}\n" +
                "description = ${this.description}"
    }
}