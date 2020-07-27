package org.bibletranslationtools.jvm

import org.bibletranslationtools.jvm.ui.main.MainView
import tornadofx.*

class MainApp: App(MainView::class)

fun main(args: Array<String>) {
    launch<MainApp>(args)
}
