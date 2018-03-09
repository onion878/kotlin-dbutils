package onion.util.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.util.PropertyElf
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class DataSourceConfig: HikariConfig {
    
    constructor() {
        this.dataSourceProperties = Properties()
        this.healthCheckProperties = Properties()
        this.minimumIdle = 1
        this.maximumPoolSize = 5
        this.connectionTimeout = 300L
        this.validationTimeout = 300L
        this.initializationFailTimeout = 1L
        this.isAutoCommit = true
        this.loadProperties("/const.properties")
    }

    private fun loadProperties(propertyFileName: String) {
        val propFile = File(propertyFileName)

        try {
            val `is` = if (propFile.isFile) FileInputStream(propFile) else this.javaClass.getResourceAsStream(propertyFileName)
            var var4: Throwable? = null

            try {
                if (`is` == null) {
                    throw IllegalArgumentException("Cannot find property file: $propertyFileName")
                }

                val props = Properties()
                props.load(`is`)
                val props1 = Properties()
                props.keys.forEach { k ->
                    val key = k.toString().split(".")
                    if("hikari" == key[0]) {
                        props1[key[1]] = props[k]
                    }
                }
                PropertyElf.setTargetFromProperties(this, props1)
            } catch (var14: Throwable) {
                var4 = var14
                throw var14
            } finally {
                if (`is` != null) {
                    if (var4 != null) {
                        try {
                            (`is` as InputStream).close()
                        } catch (var13: Throwable) {
                            var4.addSuppressed(var13)
                        }

                    } else {
                        (`is` as InputStream).close()
                    }
                }

            }

        } catch (var16: IOException) {
            throw RuntimeException("Failed to read property file", var16)
        }

    }
}