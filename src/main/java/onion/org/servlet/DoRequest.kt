package onion.org.servlet

import onion.org.annontation.ReqMap
import onion.org.tools.getStringParams
import onion.util.mapToObject
import java.io.File
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Throws(Exception::class)
fun doMethod(request: HttpServletRequest, response: HttpServletResponse, file: File, serpath: String): Any? {
    val filename = file.path
    val className = filename.substring(filename.indexOf("WEB-INF\\classes\\") + 16, filename.indexOf(".class"))
            .replace("\\\\".toRegex(), ".")// 在运行期获取的类名字符串
    val class1 = Class.forName(className)
    val method = class1.methods
    var `object`: Any? = null
    var rMapping = ""
    var name = ""
    if (class1.isAnnotationPresent(ReqMap::class.java)) {
        val reqMap = class1.getAnnotation(ReqMap::class.java!!) as ReqMap
        rMapping = reqMap.value
    }
    for (method2 in method) {
        if (method2.isAnnotationPresent(ReqMap::class.java)) {
            val sObj = class1.newInstance()
            val reqMap = method2.getAnnotation(ReqMap::class.java!!)
            if (request.servletPath == rMapping + reqMap.value) {
                val ccs = method2.parameterTypes
                val param = method2.parameters
                val data = arrayOfNulls<Any>(ccs.size)
                for (i in ccs.indices) {
                    name = ccs[i].name
                    when (name) {
                        "java.util.Map" -> data[i] = getStringParams(request)
                        "javax.servlet.http.HttpServletRequest" -> data[i] = request
                        "javax.servlet.http.HttpServletResponse" -> data[i] = response
                        else -> {
                            val ex = param[i].type
                            data[i] = mapToObject(ex, getStringParams(request))
                        }
                    }
                }
                `object` = method2.invoke(sObj, *data)
            }
        }
    }
    return `object`
}