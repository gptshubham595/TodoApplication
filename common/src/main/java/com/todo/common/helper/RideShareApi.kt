package com.todo.common.helper

import java.util.*

data class Ride(
    val id: String,
    val origin: String,
    val destination: String,
    val date: Date,
    val amount: Int
)

class RideSharingApp {
    private val rides = mutableListOf<Ride>()

    fun shareRide(origin: String, destination: String, date: Date, amount: Int) {
        val ride = Ride(UUID.randomUUID().toString(), origin, destination, date, amount)
        rides.add(ride)
        println("Ride shared successfully. Ride ID: ${ride.id}")
    }

    fun viewSharedRides() {
        if (rides.isEmpty()) {
            println("No rides shared yet.")
        } else {
            println("Shared Rides:")
            for (ride in rides) {
                println(
                    "Ride ID: ${ride.id}, From: ${ride.origin}, To: ${ride.destination}, Date: ${ride.date}, Available Seats: ${ride.amount}"
                )
            }
        }
    }
}

fun main() {
    val rideSharingApp = RideSharingApp()

    rideSharingApp.shareRide("City A", "City B", Date(), 300)
    rideSharingApp.shareRide("City C", "City D", Date(), 200)

    rideSharingApp.viewSharedRides()
}
