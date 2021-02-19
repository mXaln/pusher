package org.bibletranslationtools.maui.jvm.ui

import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.bibletranslationtools.maui.common.data.FileData
import org.bibletranslationtools.maui.common.data.Grouping
import org.bibletranslationtools.maui.common.data.MediaExtension
import org.bibletranslationtools.maui.common.data.MediaQuality
import org.bibletranslationtools.maui.common.data.ResourceType
import org.bibletranslationtools.maui.common.extensions.CompressedExtensions
import tornadofx.*
import java.util.concurrent.Callable

class FileDataItem(data: FileData): Comparable<FileDataItem> {

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

    override fun compareTo(other: FileDataItem): Int {
        return Comparator<FileDataItem> { o1, o2 ->
            val o1String = o1.file.name
            val o2String = o2.file.name

            val o1Len = o1String.length
            val o2Len = o2String.length

            var index1 = 0
            var index2 = 0

            while (index1 < o1Len && index2 < o2Len) {
                var ch1 = o1String[index1]
                var ch2 = o2String[index2]
                val space1 = CharArray(o1Len)
                val space2 = CharArray(o2Len)
                var loc1 = 0
                var loc2 = 0

                do {
                    space1[loc1++] = ch1
                    index1++
                    ch1 = if (index1 < o1Len) {
                        o1String[index1]
                    } else {
                        break
                    }
                } while (Character.isDigit(ch1) == Character.isDigit(space1[0]))

                do {
                    space2[loc2++] = ch2
                    index2++
                    ch2 = if (index2 < o2Len) {
                        o2String[index2]
                    } else {
                        break
                    }
                } while (Character.isDigit(ch2) == Character.isDigit(space2[0]))

                val str1 = String(space1)
                val str2 = String(space2)

                val result: Int = if (Character.isDigit(space1[0]) && Character.isDigit(space2[0])) {
                    val firstNumberToCompare = str1.trim { it <= ' ' }.toInt()
                    val secondNumberToCompare = str2.trim { it <= ' ' }.toInt()
                    firstNumberToCompare.compareTo(secondNumberToCompare)
                } else {
                    str1.compareTo(str2)
                }

                if (result != 0) {
                    return@Comparator result
                }
            }
            return@Comparator o1Len - o2Len
        }
            .compare(this, other)
    }
}
