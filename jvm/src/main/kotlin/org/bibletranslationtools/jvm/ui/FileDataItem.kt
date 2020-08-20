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

    val languageProperty = SimpleStringProperty(data.language)
    var language by languageProperty

    val resourceTypeProperty = SimpleObjectProperty<ResourceType>(data.resourceType)
    var resourceType by resourceTypeProperty

    val bookProperty = SimpleStringProperty(data.book)
    var book by bookProperty

    val chapterProperty = SimpleStringProperty(data.chapter?.toString())
    var chapter by chapterProperty

    val mediaExtensionProperty = SimpleObjectProperty<MediaExtension>(data.mediaExtension)
    var mediaExtension by mediaExtensionProperty

    val mediaQualityProperty = SimpleObjectProperty<MediaQuality>(data.mediaQuality)
    var mediaQuality by mediaQualityProperty

    val groupingProperty = SimpleObjectProperty<Grouping>(data.grouping)
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
}
