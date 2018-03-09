package onion.org.filter

import onion.org.scan.InitScan
import onion.org.servlet.doMethod
import onion.util.db.JOOQDSL
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class DispatcherFilter: Filter {
    override fun destroy() {
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val request = request as HttpServletRequest
        val response = response as HttpServletResponse
        request.characterEncoding = "UTF-8"
        response.characterEncoding = "UTF-8"
        val serpath = InitScan.getListPath()
        val con = request.contextPath
        val conkey = request.requestURI.replaceFirst(con.toRegex(), "")
        val file = serpath.get(conkey)
        if (file == null)
            chain?.doFilter(request, response)
        try {
            val `object` = doMethod(request, response, file!!, request.requestURI)
            JOOQDSL.Commit()
            response.writer.append(`object`.toString())
        } catch (e: Exception) {
            JOOQDSL.Rollback()
            response.writer.append("{\"success\":false,\"msg\":\"" + e.message + "\"}")
            e.printStackTrace()
        }
    }

    override fun init(filterConfig: FilterConfig?) {
    }
}