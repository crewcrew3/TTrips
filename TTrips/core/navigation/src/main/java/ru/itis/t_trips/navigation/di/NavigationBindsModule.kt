package ru.itis.t_trips.navigation.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.t_trips.navigation.navigator.AuthNavigatorImpl
import ru.itis.t_trips.navigation.navigator.NavigatorImpl
import ru.itis.t_trips.navigation.navigator.NotificationNavigatorImpl
import ru.itis.t_trips.navigation.navigator.ProfileNavigatorImpl
import ru.itis.t_trips.navigation.navigator.TripNavigatorImpl
import ru.itis.t_trips.navigation_api.navigator.AuthNavigator
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.navigation_api.navigator.NotificationNavigator
import ru.itis.t_trips.navigation_api.navigator.ProfileNavigator
import ru.itis.t_trips.navigation_api.navigator.TripNavigator

@Module
@InstallIn(SingletonComponent::class)
internal interface NavigationBindsModule {

    @Binds
    @Reusable
    fun bindNavigatorToImpl(impl: NavigatorImpl): Navigator

    @Binds
    @Reusable
    fun bindAuthNavigatorToImpl(impl: AuthNavigatorImpl): AuthNavigator

    @Binds
    @Reusable
    fun bindTripNavigatorToImpl(impl: TripNavigatorImpl): TripNavigator

    @Binds
    @Reusable
    fun bindProfileNavigatorToImpl(impl: ProfileNavigatorImpl): ProfileNavigator

    @Binds
    @Reusable
    fun bindNotificationNavigatorToImpl(impl: NotificationNavigatorImpl): NotificationNavigator
}