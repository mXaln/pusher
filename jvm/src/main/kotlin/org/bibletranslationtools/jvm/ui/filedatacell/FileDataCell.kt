package org.bibletranslationtools.jvm.ui.filedatacell

import com.jfoenix.controls.JFXComboBox
import com.jfoenix.controls.JFXTextField
import javafx.event.EventTarget
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.bibletranslationtools.assets.AppResources
import org.bibletranslationtools.common.data.Grouping
import org.bibletranslationtools.common.data.MediaExtension
import org.bibletranslationtools.common.data.MediaQuality
import org.bibletranslationtools.common.data.ResourceType
import org.bibletranslationtools.jvm.controls.filedatafilter.FileDataFilter
import org.bibletranslationtools.jvm.ui.FileDataItem
import org.bibletranslationtools.jvm.ui.main.MainViewModel
import tornadofx.*

const val MAX_CHAPTER_LENGTH = 3

class FileDataCell(
    private val item: FileDataItem,
    private val filter: FileDataFilter
) : VBox() {

    private val mainViewModel = find<MainViewModel>()

    init {
        importStylesheet(AppResources.load("/css/file-data-cell.css"))
        addClass("file-data-cell")

        hbox {
            label(item.file.name)
            addClass("file-data-cell__title")
        }
        hbox {
            addClass("file-data-cell__options")
            spacing = 10.0
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["language"])
                add(
                    JFXComboBox<String>(mainViewModel.languages).apply {
                        addClass("file-data-cell__dropdown")

                        selectionModel.select(item.language)
                        bindSelected(item.languageProperty)
                        filter.selectedLanguageProperty.onChange {
                            selectionModel.select(it)
                        }
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["resourceType"])
                add(
                    JFXComboBox<ResourceType>(mainViewModel.resourceTypes).apply {
                        addClass("file-data-cell__dropdown")

                        selectionModel.select(item.resourceType)
                        bindSelected(item.resourceTypeProperty)
                        filter.selectedResourceTypeProperty.onChange {
                            selectionModel.select(it)
                        }
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["book"])
                add(
                    JFXComboBox<String>(mainViewModel.books).apply {
                        addClass("file-data-cell__dropdown")

                        selectionModel.select(item.book)
                        bindSelected(item.bookProperty)
                        filter.selectedBookProperty.onChange {
                            selectionModel.select(it)
                        }
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["chapter"])
                add(
                    JFXTextField().apply {
                        addClass("file-data-cell__chapter")

                        text = item.chapter?.toString() ?: ""
                        item.chapterProperty.bind(textProperty())
                        filter.chapterProperty.onChange {
                            text = it
                        }

                        filterInput {
                            it.controlNewText.isInt() && it.controlNewText.length <= MAX_CHAPTER_LENGTH
                        }
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["mediaExtension"])
                add(
                    JFXComboBox<MediaExtension>(mainViewModel.mediaExtensions).apply {
                        addClass("file-data-cell__dropdown")
                        enableWhen(item.mediaExtensionAvailable)

                        selectionModel.select(item.mediaExtension)
                        bindSelected(item.mediaExtensionProperty)
                        filter.selectedMediaExtensionProperty.onChange {
                            if (item.mediaExtensionAvailable.value) {
                                selectionModel.select(it)
                            }
                        }
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["mediaQuality"])
                add(
                    JFXComboBox<MediaQuality>(mainViewModel.mediaQualities).apply {
                        addClass("file-data-cell__dropdown")
                        enableWhen(item.mediaQualityAvailable)

                        selectionModel.select(item.mediaQuality)
                        bindSelected(item.mediaQualityProperty)
                        filter.selectedMediaQualityProperty.onChange {
                            if (item.mediaQualityAvailable.value) {
                                selectionModel.select(it)
                            }
                        }
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["grouping"])
                add(
                    JFXComboBox<Grouping>(mainViewModel.groupings).apply {
                        addClass("file-data-cell__dropdown")

                        selectionModel.select(item.grouping)
                        bindSelected(item.groupingProperty)
                        filter.selectedGroupingProperty.onChange {
                            selectionModel.select(it)
                        }
                    }
                )
            }
        }
    }
}

fun EventTarget.filedatacell(data: FileDataItem, filter: FileDataFilter, op: FileDataCell.() -> Unit = {}): FileDataCell {
    val fileDataItem = FileDataCell(data, filter)
    return opcr(this, fileDataItem, op)
}
