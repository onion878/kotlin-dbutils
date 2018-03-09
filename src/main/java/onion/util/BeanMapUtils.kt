package onion.util

import java.lang.reflect.Modifier
import java.util.*

@Throws(Exception::class)
fun mapToObject(beanClass: Class<*>, map: Map<String, Any>?): Any? {
    if (map == null || map.isEmpty()) {
        return null
    }
    val obj = beanClass.newInstance()
    //获取关联的所有类，本类以及所有父类
    val ret = true
    var oo: Class<*>? = obj.javaClass
    val clazz = ArrayList<Class<*>>()
    while (ret) {
        clazz.add(oo!!)
        oo = oo!!.superclass
        if (oo == null || oo == Any::class.java) {
            break
        }
    }
    for (i in clazz.indices) {
        val fields = clazz[i].declaredFields
        for (field in fields) {
            val mod = field!!.modifiers
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue
            }
            //由字符串转换回对象对应的类型
            if (field != null) {
                field.isAccessible = true
                field.set(obj, map[field.name])
            }
        }
    }
    return obj
}

@Throws(Exception::class)
fun objectToMap(obj: Any?): Map<String, Any>? {
    if (obj == null) {
        return null
    }
    //获取关联的所有类，本类以及所有父类
    val ret = true
    var oo: Class<*>? = obj.javaClass
    val clazz = ArrayList<Class<*>>()
    while (ret) {
        clazz.add(oo!!)
        oo = oo!!.superclass
        if (oo == null || oo == Any::class.java) {
            break
        }
    }
    val map = HashMap<String, Any>()
    for (i in clazz.indices) {
        val declaredFields = clazz[i].declaredFields
        for (field in declaredFields) {
            val mod = field.modifiers
            //过滤 static 和 final 类型
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue
            }
            field.isAccessible = true
            map[field.name] = field.get(obj)
        }
    }
    return map
}
