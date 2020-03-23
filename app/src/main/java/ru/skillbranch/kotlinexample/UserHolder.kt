package ru.skillbranch.kotlinexample


import androidx.annotation.VisibleForTesting
import ru.skillbranch.kotlinexemple.User
import java.lang.IllegalArgumentException

object UserHolder {
    private val map = mutableMapOf<String, User>()
    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ) : User {
        if (!map.containsKey(email.toLowerCase())){
            return User.makeUser(fullName, email = email, password = password)
                .also { user -> map[user.login] = user }
        }
        else throw IllegalArgumentException("A user with this email already exists")

    }

    fun registerUserByPhone(
        fullName: String,
        phone: String
    ) : User {

        var result = Regex("""\D+""").replace(phone, "")
        result = "+$result"
        val regex = """^\+\d{11}""".toRegex()
        if(regex.containsMatchIn(result) ){
            if(!map.containsKey(result))return User.makeUser(fullName, phone = phone )
                .also { user -> map[user.login] = user }
            else throw IllegalArgumentException("A user with this phone already exists")
        }
        else throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
    }


    fun loginUser(login: String, password: String) : String? {
        val regex = """^\+\d{1,3}\s?\(\d{3}\)\s?\d{3}(-\d{2}){2}${'$'}""".toRegex()
        var result: String
        if(regex.containsMatchIn(login) ){
            result = Regex("""\D+""").replace(login, "")
            result = "+$result"
        } else result = login

        return map[result.trim()]?.let {
            if(it.checkPassword(password)) it.userInfo
            else null
        }
    }

    fun requestAccessCode(login: String) : Unit {
        val regex = """^\+\d{1,3}\s?\(\d{3}\)\s?\d{3}(-\d{2}){2}${'$'}""".toRegex()
        var result: String
        if(regex.containsMatchIn(login) ){
            result = Regex("""\D+""").replace(login, "")
            result = "+$result"
        } else result = login
        val user = map.get(result)
        user?.createAccessCode(login)




    }
//    fun splitInfo(str: String) : String{
//        val list_1: List<String> = str.split("login")
//        var firstPart = list_1.getOrNull(0)?.trim()
//        val list_2: List<String>? = firstPart?.split("lastName: ")
//        val lName = list_2?.getOrNull(1)?.trim()
//        firstPart = list_2?.getOrNull(0)?.trim()
//        val list_3: List<String>? = firstPart?.split("firstName: ")
//        val fName = list_3?.getOrNull(1)?.trim()
//        val fullName = "$fName $lName"
//        println("fName: $fName")
//        println("lName: $lName")
//        println("fullName: $fullName")
//
//        return fullName
//    }

    @VisibleForTesting (otherwise = VisibleForTesting.NONE)
    fun clearHolder(){
        map.clear()
    }
}