package com.mobdeve.s12.abe.daniel.mco3.models

data class Show(
    val id: Int,
    val name: String,
    var status: String,
    var rating: Float,
    var comment: String?
)
