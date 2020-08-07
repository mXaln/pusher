package org.bibletranslationtools.mappers

interface IMapper<E, T> {
    fun fromEntity(type: E): T
    fun toEntity(type: T): E
}
