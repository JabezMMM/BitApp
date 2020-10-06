package com.bitapp.data.entity

import com.bitapp.domain.entities.BaseUnsubscribe

class UnsubscribeRequest(
        override val event: String,
        override val chanId: String) : BaseUnsubscribeRequest(event, chanId)

fun BaseUnsubscribe.toUnsubscribeRequest() =
        UnsubscribeRequest(
                event = event,
                chanId = chanId
        )