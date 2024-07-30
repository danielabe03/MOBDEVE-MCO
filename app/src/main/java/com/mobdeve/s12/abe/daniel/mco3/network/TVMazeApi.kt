package com.mobdeve.s12.abe.daniel.mco3.network

import com.mobdeve.s12.abe.daniel.mco3.models.TVShow
import com.mobdeve.s12.abe.daniel.mco3.models.TVShowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVMazeApi {
    @GET("search/shows")
    fun searchShows(@Query("q") query: String): Call<List<TVShowResponse>>

    @GET("shows/{id}")
    fun getShowDetails(@Path("id") id: Int): Call<TVShow>
}
