package org.bibletranslationtools.maui.jvm.ui.filedatacell

import com.jfoenix.controls.JFXComboBox
import com.jfoenix.controls.JFXTextField
import javafx.event.EventTarget
import javafx.scene.control.ListCell
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.bibletranslationtools.maui.jvm.assets.AppResources
import org.bibletranslationtools.maui.common.data.Grouping
import org.bibletranslationtools.maui.common.data.MediaExtension
import org.bibletranslationtools.maui.common.data.MediaQuality
import org.bibletranslationtools.maui.common.data.ResourceType
import org.bibletranslationtools.maui.jvm.controls.filedatafilter.MAX_CHAPTER_LENGTH
import org.bibletranslationtools.maui.jvm.ui.FileDataItem
import org.bibletranslationtools.maui.jvm.ui.main.MainViewModel
import tornadofx.*

class FileDataCell(private val fileDataItem: FileDataItem) : VBox() {

    private val mainViewModel = find<MainViewModel>()

    init {
        importStylesheet(AppResources.load("/css/file-data-cell.css"))
        addClass("file-data-cell")

        hbox {
            label(fileDataItem.file.name)
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

                        selectionModel.select(fileDataItem.language)
                        bindSelected(fileDataItem.languageProperty)
                        fileDataItem.languageProperty.onChange {
                            selectionModel.select(it)
                        }

                        isDisable = !fileDataItem.initLanguage.isNullOrEmpty()
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["resourceType"])
                add(
                    JFXComboBox<ResourceType>(mainViewModel.resourceTypes).apply {
                        addClass("file-data-cell__dropdown")

                        selectionModel.select(fileDataItem.resourceType)
                        bindSelected(fileDataItem.resourceTypeProperty)
                        fileDataItem.resourceTypeProperty.onChange {
                            selectionModel.select(it)
                        }

                        isDisable = fileDataItem.initResourceType != null
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["book"])
                add(
                    JFXComboBox<String>(mainViewModel.books).apply {
                        addClass("file-data-cell__dropdown")

                        selectionModel.select(fileDataItem.book)
                        bindSelected(fileDataItem.bookProperty)
                        fileDataItem.bookProperty.onChange {
                            selectionModel.select(it)
                        }

                        isDisable = !fileDataItem.initBook.isNullOrEmpty()
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["chapter"])
                add(
                    JFXTextField().apply {
                        addClass("file-data-cell__chapter")

                        text = fileDataItem.chapter?.toString() ?: ""
                        fileDataItem.chapterProperty.bindBidirectional(textProperty())

                        filterInput {
                            it.controlNewText.isInt() && it.controlNewText.length <= MAX_CHAPTER_LENGTH
                        }

                        isDisable = !fileDataItem.initChapter.isNullOrEmpty()
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["mediaExtension"])
                add(
                    JFXComboBox<MediaExtension>(mainViewModel.mediaExtensions).apply {
                        addClass("file-data-cell__dropdown")
                        enableWhen(fileDataItem.mediaExtensionAvailable)

                        selectionModel.select(fileDataItem.mediaExtension)
                        bindSelected(fileDataItem.mediaExtensionProperty)
                        fileDataItem.mediaExtensionProperty.onChange {
                            selectionModel.select(it)
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
                        enableWhen(fileDataItem.mediaQualityAvailable)

                        selectionModel.select(fileDataItem.mediaQuality)
                        bindSelected(fileDataItem.mediaQualityProperty)
                        fileDataItem.mediaQualityProperty.onChange {
                            selectionModel.select(it)
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

                        selectionModel.select(fileDataItem.grouping)
                        bindSelected(fileDataItem.groupingProperty)
                        fileDataItem.groupingProperty.onChange {
                            selectionModel.select(it)
                        }

                        isDisable = fileDataItem.initGrouping != null

                        setCellFactory {
                            object : ListCell<Grouping>() {
                                override fun updateItem(item: Grouping?, empty: Boolean) {
                                    super.updateItem(item, empty)
                                    text = item?.toString() ?: ""
                                    if (mainViewModel.restrictedGroupings(fileDataItem).contains(item)) {
                                        isDisable = true
                                        opacity = 0.5
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

fun EventTarget.filedatacell(
    data: FileDataItem,
    op: FileDataCell.() -> Unit = {}
): FileDataCell {
    val fileDataItem = FileDataCell(data)
    return opcr(this, fileDataItem, op)
}
