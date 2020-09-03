package org.bibletranslationtools.mappers

import org.bibletranslationtools.common.data.FileData
import org.bibletranslationtools.jvm.ui.FileDataItem

class FileDataMapper : IMapper<FileData, FileDataItem> {

    override fun fromEntity(type: FileData): FileDataItem {
        return FileDataItem(type)
    }

    override fun toEntity(type: FileDataItem): FileData {
        return FileData(
            type.file,
            type.language,
            type.resourceType,
            type.book,
            if (type.chapter.isNotBlank()) type.chapter.toInt() else null,
            type.mediaExtension,
            type.mediaQuality,
            type.grouping
        )
    }
}
