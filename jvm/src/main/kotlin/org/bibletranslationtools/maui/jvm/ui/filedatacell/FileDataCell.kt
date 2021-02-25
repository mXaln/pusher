package org.bibletranslationtools.maui.jvm.ui.filedatacell

import javafx.scene.control.ListCell
import org.bibletranslationtools.maui.jvm.ui.FileDataItem

class FileDataCell: ListCell<FileDataItem>() {

    private val cellView = FileDataView()

    override fun updateItem(item: FileDataItem?, empty: Boolean) {
        super.updateItem(item, empty)

        graphic = if (empty) {
            null
        } else {
            cellView.fileDataItemProperty.set(null)
            cellView.fileDataItemProperty.set(item)
            cellView
        }
    }
}
