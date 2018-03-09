package onion.org.scan

import onion.org.annontation.ReqMap
import onion.util.PropertyReader
import onion.util.log
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.sql.SQLException
import java.util.HashMap
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener

class InitScan: ServletContextListener {
    private var path = ""
    private var allpath = ""

    private fun getPath() = path

    private fun setPath(path: String) {
        this.path = path
    }

    companion object {
        private var paths: MutableMap<String, File> = HashMap()
        fun setListPath(paths: MutableMap<String, File>) {
            this.paths = paths
        }
        fun getListPath(): Map<String, File> {
            return paths
        }
    }



    @Throws(ClassNotFoundException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class, InstantiationException::class, SQLException::class)
    private fun getFile() {
        val path1 = javaClass.protectionDomain.codeSource.location.path
        var pathall = path1.substring(0, path1.indexOf("WEB-INF/") + 8) + "classes/"
        pathall = pathall.substring(1, pathall.indexOf("classes/") + 8)
        pathall = "$pathall$path/"
        allpath = pathall
        scanFile()
    }

    @Throws(ClassNotFoundException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class, InstantiationException::class, SQLException::class)
    private fun scanFile() {
        val file = File(allpath)
        val array = file.listFiles()
        for (i in array!!.indices) {
            if (array[i].isFile) {
                doMethod(array[i])
            } else {
                allpath = array[i].path
                getFile()
            }
        }
    }

    @Throws(ClassNotFoundException::class, InstantiationException::class, IllegalAccessException::class, InvocationTargetException::class)
    private fun doMethod(file: File) {
        val filename = file.path
        val className = filename.substring(filename.indexOf("WEB-INF\\classes\\") + 16, filename.indexOf(".class"))
                .replace("\\\\".toRegex(), ".")// 在运行期获取的类名字符串
        val class1 = Class.forName(className)
        val method = class1.methods
        var rMapping = ""
        val `object`: Any? = null
        if (class1.isAnnotationPresent(ReqMap::class.java)) {
            val ReqMap = class1.getAnnotation(ReqMap::class.java) as ReqMap
            rMapping = ReqMap.value
        }
        for (method2 in method) {
            if (method2.isAnnotationPresent(ReqMap::class.java)) {
                val sObj = class1.newInstance()
                val reqMap = method2.getAnnotation(ReqMap::class.java)
                val reach = rMapping + reqMap.value
                paths[reach] = file
                log(3, "扫描到的请求路径:$reach")
            }
        }
    }

    override fun contextInitialized(sce: ServletContextEvent?) {
        if (getPath() == "") {
            var path = PropertyReader.get("scanPackage");
            if(path == null || path == "") throw NullPointerException("scanPackage不能为空")
            setPath(path.toString())
        }
        try {
            getFile()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
    }
}