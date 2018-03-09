package onion.util

import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val pattern = PropertyReader.get("pattern")
/**
 * Date转换为String（格式为配置文件格式）
 *
 * @param date
 * @return
 */
fun format(date: Date?): String? {
    return if (date == null) {
        null
    } else {
        val df = SimpleDateFormat(pattern as String?)
        df.format(date)
    }
}

/**
 * String转换为Date（格式为配置文件格式）
 *
 * @param dateStr
 * @return
 */
fun date(dateStr: String): Date? {
    val df = SimpleDateFormat(pattern as String?)
    var d: Date? = null

    try {
        d = df.parse(dateStr)
        return d
    } catch (var4: ParseException) {
        log(2, "DateUtil.date:日期转换错误:${var4.message}")
        throw RuntimeException(var4.message)
    }

}

/**
 * 转换未时间戳
 *
 * @param date
 * @return
 */
fun toStamp(date: Date?): Timestamp? {
    return if (date == null) {
        null
    } else {
        log(0, "DateUtil.toStamp:日期转换时间戳:$date")
        Timestamp(date.time)
    }
}

/**
 * 得到一个月的天数
 */
fun DayOfMonth(): Int {
    val a = Calendar.getInstance(Locale.CHINA)
    return a.getActualMaximum(Calendar.DATE)
}

/**
 * 得到当前年月份
 */
fun GetYearAndMonth(): String {
    val currentTime = Date()
    val formatter = SimpleDateFormat("yyyy-MM")
    return formatter.format(currentTime)
}

/**
 * String转Date
 *
 * @param date
 * @return Date
 */
fun StringToDate(date: String): Date? {
    try {
        val standarDateFormat = SimpleDateFormat(
                "yyyy-MM-dd")
        return standarDateFormat.parse(date)
    } catch (e: Exception) {
        e.printStackTrace()
        log(3, "日期转换出错,信息:" + e.message)
    }

    return null
}

/**
 * 得到当前年月日时分秒（String）
 *
 * @return
 */
fun GetNow(): String {
    val currentTime = Date()
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return formatter.format(currentTime)
}

/**
 * 得到当前日期（Date）
 *
 * @return
 */
fun GetNowDate(): Date {
    return Date()
}

/**
 * 转换年月日（Date=》String）
 *
 * @param date
 * @return
 */
fun DateToString(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(date)
}

/**
 * 获取两个日期中的所有日期
 *
 * @param startdate
 * @param enddate
 * @return String
 */
fun findalldates(startdate: String, enddate: String): List<String> {
    val list = ArrayList<String>()
    list.add(startdate)
    val cabstand = Calendar.getInstance()
    cabstand.time = StringToDate(startdate)!!
    val caged = Calendar.getInstance()
    caged.time = StringToDate(enddate)!!
    while (StringToDate(enddate)!!.after(cabstand.time)) {
        cabstand.add(Calendar.DAY_OF_MONTH, 1)
        list.add(DateToString(cabstand.time))

    }
    return list
}

/**
 * 获得当前周的每一天
 *
 * @param mString
 * @return
 */
fun dateeveyweek(mString: String): List<String> {
    val mdate = StringToDate(mString)
    val b = mdate!!.day
    var fDate: Date
    val list = ArrayList<String>()
    val fTime = mdate.time - b * 24 * 3600000
    for (a in 1..7) {
        fDate = Date()
        fDate.time = fTime + a * 24 * 3600000
        list.add(DateToString(fDate))
    }
    return list
}

/**
 * 获取一周的开头和结尾
 *
 * @param mString
 * @return List<String>
</String> */
fun dateToweek(mString: String): List<String> {
    val mdate = StringToDate(mString)
    val b = mdate!!.day
    var fDate: Date
    val list = ArrayList<String>()
    val fTime = mdate.time - b * 24 * 3600000
    for (a in 1..7) {
        fDate = Date()
        fDate.time = fTime + a * 24 * 3600000
        if (a == 1) {
            list.add(DateToString(fDate))
        }
        if (a == 7) {
            list.add(DateToString(fDate))
        }

    }
    return list
}