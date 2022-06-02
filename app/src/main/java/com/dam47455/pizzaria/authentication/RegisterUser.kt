package com.dam47455.pizzaria.authentication

class RegisterUser(var username: String, var email: String, var password: String, var cpassword: String) {

    val MIN_LENGTH: Int = 8
    val MAX_LENGTH: Int = 16
    var validationFlag : RegisterValidation = RegisterValidation.NOERRORS
    enum class RegisterValidation{
        NOERRORS, USERNAMEERROR, EMAILERROR, PASSWORDERROR, CONFIRMPASSWORDERROR
    }
    fun validateInformation(): RegisterValidation {
        validationFlag = if (!validateUsername() && !registrationHasError()) RegisterValidation.USERNAMEERROR else if(registrationHasError()) return validationFlag else RegisterValidation.NOERRORS
        validationFlag = if (!validateEmail() && !registrationHasError()) RegisterValidation.EMAILERROR else if(registrationHasError()) return validationFlag else RegisterValidation.NOERRORS
        validationFlag = if (!validatePassword() && !registrationHasError()) RegisterValidation.PASSWORDERROR else if(registrationHasError()) return validationFlag else RegisterValidation.NOERRORS
        validationFlag = if (!passwordMatches() && !registrationHasError()) RegisterValidation.CONFIRMPASSWORDERROR else if(registrationHasError()) return validationFlag else RegisterValidation.NOERRORS
        return validationFlag
    }

    private fun registrationHasError():Boolean{
        return validationFlag != RegisterValidation.NOERRORS
    }

    private fun validateUsername(): Boolean{
        return username.isNotEmpty() && isLetters(username) && username.length > MIN_LENGTH && username.length < MAX_LENGTH
    }

    private fun validateEmail(): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private fun validatePassword(): Boolean{
        return password.isNotEmpty() && password.length > MIN_LENGTH && password.length < MAX_LENGTH
    }

    private fun passwordMatches(): Boolean {
        return cpassword.isNotEmpty() && password == cpassword
    }

    private fun isLetters(string: String): Boolean {
        return string.filter { it in 'A'..'Z' || it in 'a'..'z' }.length == string.length
    }
}