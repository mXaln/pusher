package org.bibletranslationtools.jvm.ui

import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.bibletranslationtools.common.data.FileData
import org.bibletranslationtools.common.data.Grouping
import org.bibletranslationtools.common.data.MediaExtension
import org.bibletranslationtools.common.data.MediaQuality
import org.bibletranslationtools.common.data.ResourceType
import org.bibletranslationtools.common.extensions.CompressedExtensions
import tornadofx.*
import java.util.concurrent.Callable

class FileDataItem(data: FileData) {

    val file = data.file

    val initLanguage = data.language
    val languageProperty = SimpleStringProperty(initLanguage)
    var language: String? by languageProperty

    val initResourceType = data.resourceType
    val resourceTypeProperty = SimpleObjectProperty<ResourceType>(initResourceType)
    var resourceType by resourceTypeProperty

    val initBook = data.book
    val bookProperty = SimpleStringProperty(initBook)
    var book by bookProperty

    val initChapter = data.chapter?.toString()
    val chapterProperty = SimpleStringProperty(initChapter)
    var chapter by chapterProperty

    val initMediaExtension = data.mediaExtension
    val mediaExtensionProperty = SimpleObjectProperty<MediaExtension>(initMediaExtension)
    var mediaExtension by mediaExtensionProperty

    val initMediaQuality = data.mediaQuality
    val mediaQualityProperty = SimpleObjectProperty<MediaQuality>(initMediaQuality)
    var mediaQuality by mediaQualityProperty

    val initGrouping = data.grouping
    val groupingProperty = SimpleObjectProperty<Grouping>(initGrouping)
    var grouping by groupingProperty

    val isContainerProperty = SimpleBooleanProperty(data.isContainer)
    val isContainer by isContainerProperty

    val isCompressedProperty = SimpleBooleanProperty(data.isCompressed)
    val isCompressed by isCompressedProperty

    val isContainerAndCompressed: BooleanBinding = Bindings.createBooleanBinding(
            Callable {
                mediaExtension?.let {
                    isContainer && CompressedExtensions.isSupported(mediaExtension.toString())
                } ?: false
            },
            mediaExtensionProperty
        )

    val mediaExtensionAvailable = SimpleBooleanProperty(isContainer)
    val mediaQualityAvailable: BooleanBinding = Bindings.createBooleanBinding(
            Callable {
                isContainerAndCompressed.value || isCompressed
            },
            mediaExtensionProperty
        )

    override fun equals(other: Any?): Boolean {
        return (other as? FileDataItem)?.let {
            it.file == this.file
        } ?: false
    }

    override fun hashCode() = file.hashCode()
}
