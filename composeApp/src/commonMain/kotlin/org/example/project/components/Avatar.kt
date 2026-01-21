package org.example.project.components

import org.jetbrains.compose.resources.DrawableResource
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.*

enum class Avatar(
    val id: String,
    val image: DrawableResource
) {

    AVATAR_01("AVATAR_01", Res.drawable.avatar01),
    AVATAR_02("AVATAR_02", Res.drawable.avatar02),
    AVATAR_03("AVATAR_03", Res.drawable.avatar03),
    AVATAR_04("AVATAR_04", Res.drawable.avatar04),
    AVATAR_05("AVATAR_05", Res.drawable.avatar05),
    AVATAR_06("AVATAR_06", Res.drawable.avatar06),

    AVATAR_07("AVATAR_07", Res.drawable.avatar07),
    AVATAR_08("AVATAR_08", Res.drawable.avatar08),
    AVATAR_09("AVATAR_09", Res.drawable.avatar09),
    AVATAR_10("AVATAR_10", Res.drawable.avatar10),
    AVATAR_11("AVATAR_11", Res.drawable.avatar11),
    AVATAR_12("AVATAR_12", Res.drawable.avatar12),

    AVATAR_13("AVATAR_13", Res.drawable.avatar13),
    AVATAR_14("AVATAR_14", Res.drawable.avatar14),
    AVATAR_15("AVATAR_15", Res.drawable.avatar15),
    AVATAR_16("AVATAR_16", Res.drawable.avatar16),
    AVATAR_17("AVATAR_17", Res.drawable.avatar17),
    AVATAR_18("AVATAR_18", Res.drawable.avatar18),

    AVATAR_19("AVATAR_19", Res.drawable.avatar19),
    AVATAR_20("AVATAR_20", Res.drawable.avatar20),
    AVATAR_21("AVATAR_21", Res.drawable.avatar21),
    AVATAR_22("AVATAR_22", Res.drawable.avatar22),
    AVATAR_23("AVATAR_23", Res.drawable.avatar23),
    AVATAR_24("AVATAR_24", Res.drawable.avatar24);

    companion object {
        fun fromId(id: String): Avatar? =
            entries.firstOrNull { it.id == id }
    }
}