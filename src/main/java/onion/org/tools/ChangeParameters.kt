package onion.org.tools

import java.util.HashMap
import javax.servlet.http.HttpServletRequest

fun ifTypes(typeName: String): Boolean {
    val types = arrayOf("String", "Integer", "int", "Float", "float", "Double", "double", "Boolean", "boolean", "", "", "", "", "")
    for (string in types) {
        if (typeName == string)
            return true
    }
    return false
}

fun getStringParams(request: HttpServletRequest): MutableMap<String, String> {
    val rawParam = request.parameterMap
    val keyParam = HashMap<String, String>()
    for (key in rawParam.keys) {
        if (rawParam[key] != null) {
            val value = rawParam[key]
            if (value != null && value!!.size == 1) {
                keyParam[key] = value!![0]
            }
        }
    }
    return keyParam
}