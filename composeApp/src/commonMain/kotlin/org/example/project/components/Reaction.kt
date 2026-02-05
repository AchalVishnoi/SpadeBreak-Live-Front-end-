package org.example.project.components

enum class Reaction {
    ANGRY,
    COOL,
    HA_HA,
    HEART,
    SAD,
    SMILE,
    THUMBS_UP,
    FIRE,
    IDEA,
    JOCKER,
    PARTY,
    YAWN
}

expect fun Reaction.getAnimation(): Int?

expect fun Reaction.showReactionAnimation():Int?

