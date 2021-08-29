package com.ivansertic.magictournament.models.objects

import com.ivansertic.magictournament.models.enums.UserType

object UserObject {

    private var type: UserType? = null

    fun createObject(type:String){
        this.type = UserType.valueOf(type)
    }

    fun getUserType(): UserType{
        return this.type!!
    }

    fun setTypeToNull(){
        this.type = null
    }

}