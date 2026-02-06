package org.example.project.components

import org.example.project.R

actual fun Reaction.getAnimation(): Int? {
   return when(this){
        Reaction.ANGRY -> R.raw.angry_anime
        Reaction.COOL -> R.raw.cool_anime
        Reaction.HA_HA -> R.raw.ha_ha_anime
        Reaction.HEART -> R.raw.heart_emoji
        Reaction.SAD -> R.raw.sad_anime
        Reaction.SMILE -> R.raw.smile_anime
        Reaction.THUMBS_UP -> R.raw.thumbs_up_anime
       Reaction.FIRE -> R.raw.fire_emoji
       Reaction.IDEA -> R.raw.idea_emoji
       Reaction.JOCKER -> R.raw.jocker_face_emoji
       Reaction.PARTY -> R.raw.party_emoji
       Reaction.YAWN -> R.raw.yawn_emoji
   }
}

actual fun Reaction.showReactionAnimation(): Int? {
    return when(this){
        Reaction.ANGRY -> R.raw.angry_anime
        Reaction.COOL -> R.raw.cool_anime
        Reaction.HA_HA -> R.raw.ha_ha_anime
        Reaction.HEART -> R.raw.heart_sprinkle
        Reaction.SAD -> R.raw.sad_anime
        Reaction.SMILE -> R.raw.smile_anime
        Reaction.THUMBS_UP -> R.raw.thumbs_up_anime
        Reaction.FIRE -> R.raw.fire_emoji
        Reaction.IDEA -> R.raw.idea_emoji
        Reaction.JOCKER -> R.raw.jocker_face_emoji
        Reaction.PARTY -> R.raw.party_emoji
        Reaction.YAWN -> R.raw.yawn_emoji
    }
}