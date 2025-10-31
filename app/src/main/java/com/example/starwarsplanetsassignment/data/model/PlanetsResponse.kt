package com.example.starwarsplanetsassignment.data.model

import com.google.gson.annotations.SerializedName

data class PlanetsResponse (
  @SerializedName("count"    ) var count    : Int?               = null,
  @SerializedName("next"     ) var next     : String?            = null,
  @SerializedName("previous" ) var previous : String?            = null,
  @SerializedName("results"  ) var results  : ArrayList<Planet> = arrayListOf())