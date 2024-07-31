package com.mobdeve.s12.abe.daniel.mco3.models

data class CustomList(
    val id: Int,
    val name: String,
    val shows: List<Show> = emptyList()
)
