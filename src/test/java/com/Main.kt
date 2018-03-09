package com

import onion.util.PropertyReader
import onion.util.db.JOOQDSL
import onion.util.log


class Main


fun main(args: Array<String>) {
    val string = PropertyReader.get("jdbcUrl")
    log(2,"134")
    val s = JOOQDSL.GetAutoDSL().select().from("roles");
    JOOQDSL.GetAutoDSL().select().from("roles").fetch().forEach { r -> println(r.get("id")) }
    JOOQDSL.Commit()
    println("Hello")
}


