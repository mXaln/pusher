package org.bibletranslationtools.common.data

import java.lang.IllegalArgumentException

enum class MediaQuality(val quality: String) {
    HI("hi"),
    LOW("low");

    companion object {
        fun of(quality: String) =
            values().singleOrNull {
                it.name == quality.toUpperCase() || it.quality == quality
            } ?: throw IllegalArgumentException("There is no quality $quality")
    }
}
