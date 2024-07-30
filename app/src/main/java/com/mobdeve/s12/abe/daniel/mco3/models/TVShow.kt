package com.mobdeve.s12.abe.daniel.mco3.models

data class TVShow(
    val id: Int,
    val name: String,
    val genres: List<String>,
    val summary: String,
    val image: Image?
)

data class Image(
    val medium: String,
    val original: String
)
