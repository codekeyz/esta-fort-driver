package com.hoversoftsoln.esta_fort.core.data

import com.google.firebase.firestore.PropertyName

data class Driver(
        @get:PropertyName("id")
        @set:PropertyName("id") var id: String = "",
        @get:PropertyName("username")
        @set:PropertyName("username") var username: String = "",
        @get:PropertyName("email")
        @set:PropertyName("email") var email: String = "",
        @get:PropertyName("telephone")
        @set:PropertyName("telephone") var telephone: String = "",
        @get:PropertyName("status")
        @set:PropertyName("status") var status: Int = 0,
        @get:PropertyName("image")
        @set:PropertyName("image") var image: String = "",
        @get:PropertyName("rating")
        @set:PropertyName("rating") var rating: Int = 0,
        @set:PropertyName("token")
        @get:PropertyName("token") var token: String = "")