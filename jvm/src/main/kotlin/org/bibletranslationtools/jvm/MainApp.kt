package org.bibletranslationtools.jvm

import javafx.scene.image.Image
import javafx.stage.Stage
import org.bibletranslationtools.jvm.ui.main.MainView
import tornadofx.*

class MainApp: App(MainView::class) {
    override fun start(stage: Stage) {
        stage.icons.add(
            Image(javaClass.getResource("/launcher.png").openStream())
        )
        super.start(stage)
    }
}

fun main(args: Array<String>) {
    launch<MainApp>(args)
}
