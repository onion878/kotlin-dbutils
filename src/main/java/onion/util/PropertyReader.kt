package onion.util

import java.io.InputStream
import java.util.*

class PropertyReader {

    companion object {

        private val ps = Properties()

        init {
            var inPut: InputStream? = null
            try {
                inPut = PropertyReader::class.java
                        .getResourceAsStream("/const.properties")
                ps.load(inPut)
                inPut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inPut!!.close()
            }
        }

        fun get(key: String): Any? {
            var r = ps[key]
            if (r == null) {
                log(2, "未找到属性值:$key");
            }
            return r;
        }
    }

}