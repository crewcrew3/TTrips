package ru.itis.t_trips.data.repository

import retrofit2.HttpException
import ru.itis.t_trips.data.remote.mapper.DebtResponseMapper
import ru.itis.t_trips.data.remote.mapper.TripResponseMapper
import ru.itis.t_trips.data.remote.mapper.UserProfileResponseMapper
import ru.itis.t_trips.domain.model.DebtModel
import ru.itis.t_trips.domain.model.ReportModel
import ru.itis.t_trips.domain.model.TripModel
import ru.itis.t_trips.domain.repository.TripRepository
import ru.itis.t_trips.network.api.TripApi
import ru.itis.t_trips.network.api.UserApi
import ru.itis.t_trips.network.model.request.trip.AddTripRequest
import ru.itis.t_trips.network.model.request.user.PhoneNumberRequest
import ru.itis.t_trips.utils.ExceptionCode
import ru.itis.t_trips.utils.runCatchingNonCancellable
import java.net.HttpURLConnection
import javax.inject.Inject

internal class TripRepositoryImpl @Inject constructor(
    private val tripApi: TripApi,
    private val userApi: UserApi,
    private val tripResponseMapper: TripResponseMapper,
    private val userProfileResponseMapper: UserProfileResponseMapper,
    private val debtResponseMapper: DebtResponseMapper,
) : TripRepository {

    override suspend fun getTripList(onlyCreator: Boolean, onlyArchive: Boolean): List<TripModel> {
        return runCatchingNonCancellable {
            tripApi.getTripList(onlyCreator = onlyCreator, onlyArchive = onlyArchive).map(tripResponseMapper::map)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun addTrip(
        title: String,
        startDate: String,
        endDate: String,
        budget: Double
    ): TripModel {
        return runCatchingNonCancellable {
            tripApi.addTrip(
                AddTripRequest(
                    title = title,
                    startDate = startDate,
                    endDate = endDate,
                    budget = budget,
                )
            ).let(tripResponseMapper::map)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_BAD_REQUEST -> throw IllegalArgumentException(ExceptionCode.INVALID_DATA)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun inviteMemberForTrip(tripId: Int, phoneNumber: String) {
        runCatchingNonCancellable {
            tripApi.inviteMemberForTrip(
                tripId = tripId,
                request = PhoneNumberRequest(
                    phoneNumber = phoneNumber
                )
            )
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_BAD_REQUEST -> throw IllegalArgumentException(ExceptionCode.INVALID_DATA)
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(ExceptionCode.FORBIDDEN)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }
    }

    override suspend fun getTripInfo(tripId: Int): TripModel {
        return runCatchingNonCancellable {
            tripApi.getTripInfo(tripId).let(tripResponseMapper::map)
        }.onFailure {exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(ExceptionCode.FORBIDDEN)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun finishTrip(tripId: Int) {
        runCatchingNonCancellable {
            tripApi.finishTrip(tripId)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(ExceptionCode.FORBIDDEN)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }
    }

    override suspend fun getTripMembersIds(tripId: Int): List<Int> {
        return runCatchingNonCancellable {
            tripApi.getTripMembers(tripId).mapNotNull { trip -> trip?.userId }
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(ExceptionCode.FORBIDDEN)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun deleteTrip(tripId: Int) {
        runCatchingNonCancellable {
            tripApi.deleteTrip(tripId)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(ExceptionCode.FORBIDDEN)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }
    }

    override suspend fun getTripReport(tripId: Int): ReportModel {
        return runCatchingNonCancellable {
            val trip = tripApi.getTripInfo(tripId).let(tripResponseMapper::map)
            val debtsMap = tripApi.getTripDebts(tripId)
                .filterNotNull()
                .groupBy { requireNotNull(it.debtorId) } //группируем по должнику
                .map { entry ->
                    userApi.getUserById(entry.key)
                        .let(userProfileResponseMapper::map) to entry.value.sumOf {
                        requireNotNull(
                            it.amount
                        )
                    }
                }.toMap()
            ReportModel(
                trip = trip,
                debtsMap = debtsMap
            )
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(ExceptionCode.FORBIDDEN)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }.getOrThrow()
    }

    //долги где конкретный юзер либо должник либо кредитор
    override suspend fun getUsersDebts(tripId: Int, userId: Int, isDebtor: Boolean): List<DebtModel> {
        return runCatchingNonCancellable {
            tripApi.getTripDebts(tripId).filter {
                if (isDebtor) {
                    requireNotNull(requireNotNull(it).debtorId) == userId
                } else {
                    requireNotNull(requireNotNull(it).creditorId) == userId
                }
            }.map { debt ->
                requireNotNull(debt) { ExceptionCode.DEBT_RESPONSE }
                debtResponseMapper.map(
                    tripId = debt.tripId,
                    amount = debt.amount,
                    inputCreditor = userApi.getUserById(requireNotNull(debt.creditorId)),
                    inputDebtor = userApi.getUserById(requireNotNull(debt.debtorId)),
                )
            }
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(ExceptionCode.FORBIDDEN)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }.getOrThrow()
    }
}