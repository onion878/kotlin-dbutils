package onion.util.db

import com.zaxxer.hikari.HikariDataSource

fun getDataSource() = HikariDataSource(DataSourceConfig())