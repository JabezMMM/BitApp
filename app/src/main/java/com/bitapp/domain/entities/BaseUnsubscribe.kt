package com.bitapp.domain.entities

class BaseUnsubscribe(
         val event: String,
         val chanId: String
) {
    companion object {
        const val UNSUBSCRIBE_EVENT = "unsubscribe"
    }
}