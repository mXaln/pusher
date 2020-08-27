package org.bibletranslationtools.jvm.controls.filedatafilter

import io.reactivex.Observable
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.event.EventTarget
import javafx.scene.control.Control
import javafx.scene.control.Skin
import org.bibletranslationtools.common.data.Grouping
import org.bibletranslationtools.common.data.MediaExtension
import org.bibletranslationtools.common.data.MediaQuality
import org.bibletranslationtools.common.data.ResourceType
import org.bibletranslationtools.jvm.controls.skins.FileDataFilterSkin
import tornadofx.*

const val MAX_CHAPTER_LENGTH = 3

class FileDataFilter : Control() {

    val languageLabelProperty = SimpleStringProperty()
    val languagesProperty = SimpleListProperty<String>()
    val selectedLanguageProperty = SimpleStringProperty()

    val resourceTypeLabelProperty = SimpleStringProperty()
    val resourceTypesProperty = SimpleListProperty<ResourceType>()
    val selectedResourceTypeProperty = SimpleObjectProperty<ResourceType>()

    val bookLabelProperty = SimpleStringProperty()
    val booksProperty = SimpleListProperty<String>()
    val selectedBookProperty = SimpleStringProperty()

    val chapterLabelProperty = SimpleStringProperty()
    val chapterProperty = SimpleStringProperty()

    val mediaExtensionLabelProperty = SimpleStringProperty()
    val mediaExtensionsProperty = SimpleListProperty<MediaExtension>()
    val selectedMediaExtensionProperty = SimpleObjectProperty<MediaExtension>()

    val mediaQualityLabelProperty = SimpleStringProperty()
    val mediaQualitiesProperty = SimpleListProperty<MediaQuality>()
    val selectedMediaQualityProperty = SimpleObjectProperty<MediaQuality>()

    val groupingLabelProperty = SimpleStringProperty()
    val groupingsProperty = SimpleListProperty<Grouping>()
    val selectedGroupingProperty = SimpleObjectProperty<Grouping>()

    val onConfirmActionProperty = SimpleObjectProperty<EventHandler<ActionEvent>>()
    var callbackObserver: Observable<Boolean>? = null

    private val userAgentStyleSheet = javaClass.getResource("/css/file-data-filter.css").toExternalForm()

    init {
        initialize()
    }

    override fun createDefaultSkin(): Skin<*> {
        return FileDataFilterSkin(this)
    }

    override fun getUserAgentStylesheet(): String {
        return userAgentStyleSheet
    }

    private fun initialize() {
        stylesheets.setAll(userAgentStyleSheet)
    }

    fun onConfirmAction(op: () -> Unit) {
        onConfirmActionProperty.set(EventHandler { op.invoke() })
    }

    fun onConfirmCallback(answer: Boolean) {
        callbackObserver = Observable.just(answer)
    }
}

fun EventTarget.filedatafilter(op: FileDataFilter.() -> Unit = {}): FileDataFilter {
    val fileDataFilter = FileDataFilter()
    return opcr(this, fileDataFilter, op)
}
