package ru.itis.t_trips

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.t_trips.domain.localdatastorecontract.BasicUserDataStorage

@EntryPoint
@InstallIn(SingletonComponent::class)
interface StorageEntryPoint {
    fun basicUserDataStorage(): BasicUserDataStorage
}
