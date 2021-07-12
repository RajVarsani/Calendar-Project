package com.example.thehinducalender.models

class Quote (
    var quote :String = "",
    var author :String = ""
){
    override fun toString(): String {
        return "Quote : ${this.quote} \nAuthor : ${this.author}"
    }
}