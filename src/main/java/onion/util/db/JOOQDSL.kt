package onion.util.db

import onion.util.log
import org.jooq.DSLContext
import org.jooq.conf.StatementType
import org.jooq.impl.DSL
import java.sql.Connection

object JOOQDSL {
    private val threadConnection = ThreadLocal<Connection>()
    private val threadDsl = ThreadLocal<DSLContext>()
    fun getCon(): Connection {
        if (threadConnection.get() == null) {
            if (DButilsDataSource.getDatasource() == null) {
                val con = getDataSource().connection
                threadConnection.set(con)
            } else {
                val con = DButilsDataSource.getDatasource()!!.connection
                threadConnection.set(con)
            }
        }
        return threadConnection.get()
    }

    private fun getDsl(): DSLContext {
        if (threadDsl.get() == null) {
            threadDsl.set(DSL.using(getCon()))
        }
        return threadDsl.get()
    }

    // 预编译方法
    fun GetAutoDSL() = getDsl()

    // 预编译方法
    fun GetDSL(): DSLContext {
        getCon().autoCommit = false
        return getDsl()
    }

    // 非预编译方法
    fun GetAutoDSLNOPRE(): DSLContext {
        val dsl = getDsl()
        dsl.settings().withStatementType(StatementType.STATIC_STATEMENT)
        return getDsl()
    }

    // 非预编译方法
    fun GetDSLNOPRE(): DSLContext {
        getCon().autoCommit = false
        val dsl = getDsl()
        dsl.settings().withStatementType(StatementType.STATIC_STATEMENT)
        return getDsl()
    }

    fun Rollback() {
        val conn = threadConnection.get()
        if (conn != null && !conn.isClosed && !conn.autoCommit) {
            conn.rollback()
            conn.close()
            log(0, "关闭连接并执行事务回滚")
        }
        threadConnection.remove()
        threadDsl.remove()
    }

    fun Commit() {
        val conn = threadConnection.get()
        if (conn != null && !conn.isClosed && !conn.autoCommit) {
            conn.commit()
            conn.close()
            log(0, "关闭连接并执行事务提交")
        }
        threadConnection.remove()
        threadDsl.remove()
    }
}