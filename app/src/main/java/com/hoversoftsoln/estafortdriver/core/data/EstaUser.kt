package com.hoversoftsoln.estafortdriver.core.data

import com.google.firebase.firestore.PropertyName

data class EstaUser(
        @get:PropertyName("username")
        @set:PropertyName("username") var username: String = "",
        @get:PropertyName("email")
        @set:PropertyName("email") var email: String = "",
        @get:PropertyName("telephone")
        @set:PropertyName("telephone") var telephone: String = "",
        @get:PropertyName("location")
        @set:PropertyName("location") var location: String = "",
        @set:PropertyName("token")
        @get:PropertyName("token") var token: String = "")
