package com.ivansertic.magictournament.models

import com.ivansertic.magictournament.models.enums.UserType

data class User(
        var id: Int,
        var email: String,
        var username: String,
        var password: String,
        var type: UserType
)