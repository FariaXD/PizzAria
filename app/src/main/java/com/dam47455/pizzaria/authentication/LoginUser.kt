package com.dam47455.pizzaria.authentication

class LoginUser(var username: String, var password: String) {

    enum class LoginValidation{
        NOERRORS, USERNAMEERROR, PASSWORDERROR
    }

    var validationFlag = LoginValidation.NOERRORS

    fun validateLoginFields(): LoginValidation {
        validationFlag = if (!validateUsername() && !registrationHasError()) LoginValidation.USERNAMEERROR else if(registrationHasError()) return validationFlag else LoginValidation.NOERRORS
        validationFlag = if (!validatePassword() && !registrationHasError()) LoginValidation.PASSWORDERROR else if(registrationHasError()) return validationFlag else LoginValidation.NOERRORS
        return validationFlag
    }

    fun validateUsername(): Boolean{
        return username.isNotEmpty()
    }

    fun validatePassword(): Boolean{
        return password.isNotEmpty()
    }

    private fun registrationHasError():Boolean{
        return validationFlag != LoginValidation.NOERRORS
    }
}