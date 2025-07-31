package ru.itis.t_trips.domain.repository

import ru.itis.t_trips.domain.model.DebtModel
import ru.itis.t_trips.domain.model.ReportModel
import ru.itis.t_trips.domain.model.TripModel

interface TripRepository {
    suspend fun getTripList(onlyCreator: Boolean, onlyArchive: Boolean): List<TripModel>
    suspend fun addTrip(
        title: String,
        startDate: String,
        endDate: String,
        budget: Double
    ): TripModel
    suspend fun inviteMemberForTrip(tripId: Int, phoneNumber: String)
    suspend fun getTripInfo(tripId: Int): TripModel
    suspend fun finishTrip(tripId: Int)
    suspend fun getTripMembersIds(tripId: Int): List<Int>
    suspend fun deleteTrip(tripId: Int)
    suspend fun getTripReport(tripId: Int): ReportModel
    suspend fun getUsersDebts(tripId: Int, userId: Int, isDebtor: Boolean): List<DebtModel>
}