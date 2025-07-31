package ru.itis.t_trips.data.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.t_trips.data.local.storage.BasicUserDataStorageImpl
import ru.itis.t_trips.data.local.storage.TokenStorageImpl
import ru.itis.t_trips.data.repository.AuthRepositoryImpl
import ru.itis.t_trips.data.repository.ExpenseRepositoryImpl
import ru.itis.t_trips.data.repository.InvitationsRepositoryImpl
import ru.itis.t_trips.data.repository.PictureRepositoryImpl
import ru.itis.t_trips.data.repository.TripRepositoryImpl
import ru.itis.t_trips.data.repository.UserRepositoryImpl
import ru.itis.t_trips.domain.localdatastorecontract.BasicUserDataStorage
import ru.itis.t_trips.domain.repository.AuthRepository
import ru.itis.t_trips.domain.repository.ExpenseRepository
import ru.itis.t_trips.domain.repository.InvitationsRepository
import ru.itis.t_trips.domain.repository.PictureRepository
import ru.itis.t_trips.domain.repository.TripRepository
import ru.itis.t_trips.domain.repository.UserRepository
import ru.itis.t_trips.network.tokenlogic.TokenStorage

@Module
@InstallIn(SingletonComponent::class)
internal interface DataBinderModule {

    @Binds
    @Reusable
    fun bindUserRepositoryToImplementation(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Reusable
    fun bindAuthRepositoryToImplementation(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Reusable
    fun bindTripRepositoryToImplementation(impl: TripRepositoryImpl): TripRepository

    @Binds
    @Reusable
    fun bindExpenseRepositoryToImplementation(impl: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @Reusable
    fun bindInvitationsRepositoryToImplementation(impl: InvitationsRepositoryImpl): InvitationsRepository

    @Binds
    @Reusable
    fun bindPictureRepositoryToImplementation(impl: PictureRepositoryImpl): PictureRepository

    @Binds
    @Reusable
    fun bindTokenStorageToImplementation(impl: TokenStorageImpl): TokenStorage

    @Binds
    @Reusable
    fun bindBasicUserDataStorageToImplementation(impl: BasicUserDataStorageImpl): BasicUserDataStorage
}