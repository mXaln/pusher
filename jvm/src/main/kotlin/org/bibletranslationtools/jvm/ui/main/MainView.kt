package org.bibletranslationtools.jvm.ui.main

import org.bibletranslationtools.assets.AppResources
import tornadofx.*

class MainView : View() {
    private val viewModel: MainViewModel by inject()

    init {
        title = messages["appName"]
        importStylesheet(AppResources.load("/css/main.css"))
    }

    override val root = stackpane {
        addClass("main")
    }
}
