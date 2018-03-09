package onion.util.db

import javax.sql.DataSource

class DButilsDataSource {

    companion object {
        private var dataSource: DataSource? = null

        @Synchronized
        fun getDatasource(): DataSource? {
            return dataSource
        }

        fun setDatasource(datasource: DataSource) {
            this.dataSource = datasource
        }
    }



}