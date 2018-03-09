package onion.util

val logOne = PropertyReader.get("logTo")
val logTwo = PropertyReader.get("logLevel")

val logTo = logOne?.toString()?.toInt() ?: 1
val level = logTwo?.toString()?.toInt() ?: 0
fun log(lvl: Int, msg: Any) {
    when (logTo) {
        1 -> if (lvl >= level) {
            println("###LOGTIME ${GetNow()} = $msg")
        }
    }
}

fun info(msg: Any) {
    println("###LOGTIME [INFO] ${GetNow()} = $msg")
}

fun error(msg: Any) {
    System.err.println("###LOGTIME [ERROR] ${GetNow()} = $msg")
}