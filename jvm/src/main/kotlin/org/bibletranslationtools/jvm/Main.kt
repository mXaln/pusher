package org.bibletranslationtools.jvm

import org.bibletranslationtools.jvm.ui.main.MainView
import tornadofx.*

class MyApp: App(MainView::class)

fun main(args: Array<String>) {
    launch<MyApp>(args)
}
