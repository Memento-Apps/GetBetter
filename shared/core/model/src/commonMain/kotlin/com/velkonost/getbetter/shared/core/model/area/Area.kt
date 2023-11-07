package com.velkonost.getbetter.shared.core.model.area

import kotlinx.serialization.Serializable

@Serializable
data class Area(

    val id: Int,

    val name: String,

    val description: String,

    val createdDate: Long,

    val imageUrl: String? = null,

    val emojiId: Int? = null,

    val requiredLevel: Int,

    val isActive: Boolean,

    val isPrivate: Boolean,

    val userTermsOfMembership: TermsOfMembership,

    val isAllowJoin: Boolean = false,

    val isAllowDelete: Boolean = false,

    val isAllowEdit: Boolean = false,

    val isAllowLeave: Boolean = false,

    val experience: Int = 0
)
