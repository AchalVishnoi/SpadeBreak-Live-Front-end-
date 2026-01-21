package org.example.project.components

import org.jetbrains.compose.resources.DrawableResource
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.*

enum class Suit { CLUBS, DIAMONDS, HEARTS, SPADES }

enum class Rank(val value: Int, val display: String) {
    TWO(2, "2"),
    THREE(3, "3"),
    FOUR(4, "4"),
    FIVE(5, "5"),
    SIX(6, "6"),
    SEVEN(7, "7"),
    EIGHT(8, "8"),
    NINE(9, "9"),
    TEN(10, "10"),
    JACK(11, "J"),
    QUEEN(12, "Q"),
    KING(13, "K"),
    ACE(14, "A")
}

enum class Card(
    val id: String,
    val suit: Suit,
    val rank: Rank,
    val image: DrawableResource
) {

    CLUBS_2("2_of_clubs", Suit.CLUBS, Rank.TWO, Res.drawable._2_of_clubs),
    CLUBS_3("3_of_clubs", Suit.CLUBS, Rank.THREE, Res.drawable._3_of_clubs),
    CLUBS_4("4_of_clubs", Suit.CLUBS, Rank.FOUR, Res.drawable._4_of_clubs),
    CLUBS_5("5_of_clubs", Suit.CLUBS, Rank.FIVE, Res.drawable._5_of_clubs),
    CLUBS_6("6_of_clubs", Suit.CLUBS, Rank.SIX, Res.drawable._6_of_clubs),
    CLUBS_7("7_of_clubs", Suit.CLUBS, Rank.SEVEN, Res.drawable._7_of_clubs),
    CLUBS_8("8_of_clubs", Suit.CLUBS, Rank.EIGHT, Res.drawable._8_of_clubs),
    CLUBS_9("9_of_clubs", Suit.CLUBS, Rank.NINE, Res.drawable._9_of_clubs),
    CLUBS_10("10_of_clubs", Suit.CLUBS, Rank.TEN, Res.drawable._10_of_clubs),
    CLUBS_J("jack_of_clubs", Suit.CLUBS, Rank.JACK, Res.drawable.jack_of_clubs2),
    CLUBS_Q("queen_of_clubs", Suit.CLUBS, Rank.QUEEN, Res.drawable.queen_of_clubs2),
    CLUBS_K("king_of_clubs", Suit.CLUBS, Rank.KING, Res.drawable.king_of_clubs2),
    CLUBS_A("ace_of_clubs", Suit.CLUBS, Rank.ACE, Res.drawable.ace_of_clubs),


    DIAMONDS_2("2_of_diamonds", Suit.DIAMONDS, Rank.TWO, Res.drawable._2_of_diamonds),
    DIAMONDS_3("3_of_diamonds", Suit.DIAMONDS, Rank.THREE, Res.drawable._3_of_diamonds),
    DIAMONDS_4("4_of_diamonds", Suit.DIAMONDS, Rank.FOUR, Res.drawable._4_of_diamonds),
    DIAMONDS_5("5_of_diamonds", Suit.DIAMONDS, Rank.FIVE, Res.drawable._5_of_diamonds),
    DIAMONDS_6("6_of_diamonds", Suit.DIAMONDS, Rank.SIX, Res.drawable._6_of_diamonds),
    DIAMONDS_7("7_of_diamonds", Suit.DIAMONDS, Rank.SEVEN, Res.drawable._7_of_diamonds),
    DIAMONDS_8("8_of_diamonds", Suit.DIAMONDS, Rank.EIGHT, Res.drawable._8_of_diamonds),
    DIAMONDS_9("9_of_diamonds", Suit.DIAMONDS, Rank.NINE, Res.drawable._9_of_diamonds),
    DIAMONDS_10("10_of_diamonds", Suit.DIAMONDS, Rank.TEN, Res.drawable._10_of_diamonds),
    DIAMONDS_J("jack_of_diamonds", Suit.DIAMONDS, Rank.JACK, Res.drawable.jack_of_diamonds2),
    DIAMONDS_Q("queen_of_diamonds", Suit.DIAMONDS, Rank.QUEEN, Res.drawable.queen_of_diamonds2),
    DIAMONDS_K("king_of_diamonds", Suit.DIAMONDS, Rank.KING, Res.drawable.king_of_diamonds2),
    DIAMONDS_A("ace_of_diamonds", Suit.DIAMONDS, Rank.ACE, Res.drawable.ace_of_diamonds),


    HEARTS_2("2_of_hearts", Suit.HEARTS, Rank.TWO, Res.drawable._2_of_hearts),
    HEARTS_3("3_of_hearts", Suit.HEARTS, Rank.THREE, Res.drawable._3_of_hearts),
    HEARTS_4("4_of_hearts", Suit.HEARTS, Rank.FOUR, Res.drawable._4_of_hearts),
    HEARTS_5("5_of_hearts", Suit.HEARTS, Rank.FIVE, Res.drawable._5_of_hearts),
    HEARTS_6("6_of_hearts", Suit.HEARTS, Rank.SIX, Res.drawable._6_of_hearts),
    HEARTS_7("7_of_hearts", Suit.HEARTS, Rank.SEVEN, Res.drawable._7_of_hearts),
    HEARTS_8("8_of_hearts", Suit.HEARTS, Rank.EIGHT, Res.drawable._8_of_hearts),
    HEARTS_9("9_of_hearts", Suit.HEARTS, Rank.NINE, Res.drawable._9_of_hearts),
    HEARTS_10("10_of_hearts", Suit.HEARTS, Rank.TEN, Res.drawable._10_of_hearts),
    HEARTS_J("jack_of_hearts", Suit.HEARTS, Rank.JACK, Res.drawable.jack_of_hearts2),
    HEARTS_Q("queen_of_hearts", Suit.HEARTS, Rank.QUEEN, Res.drawable.queen_of_hearts2),
    HEARTS_K("king_of_hearts", Suit.HEARTS, Rank.KING, Res.drawable.king_of_hearts2),
    HEARTS_A("ace_of_hearts", Suit.HEARTS, Rank.ACE, Res.drawable.ace_of_hearts),


    SPADES_2("2_of_spades", Suit.SPADES, Rank.TWO, Res.drawable._2_of_spades),
    SPADES_3("3_of_spades", Suit.SPADES, Rank.THREE, Res.drawable._3_of_spades),
    SPADES_4("4_of_spades", Suit.SPADES, Rank.FOUR, Res.drawable._4_of_spades),
    SPADES_5("5_of_spades", Suit.SPADES, Rank.FIVE, Res.drawable._5_of_spades),
    SPADES_6("6_of_spades", Suit.SPADES, Rank.SIX, Res.drawable._6_of_spades),
    SPADES_7("7_of_spades", Suit.SPADES, Rank.SEVEN, Res.drawable._7_of_spades),
    SPADES_8("8_of_spades", Suit.SPADES, Rank.EIGHT, Res.drawable._8_of_spades),
    SPADES_9("9_of_spades", Suit.SPADES, Rank.NINE, Res.drawable._9_of_spades),
    SPADES_10("10_of_spades", Suit.SPADES, Rank.TEN, Res.drawable._10_of_spades),
    SPADES_J("jack_of_spades", Suit.SPADES, Rank.JACK, Res.drawable.jack_of_spades2),
    SPADES_Q("queen_of_spades", Suit.SPADES, Rank.QUEEN, Res.drawable.queen_of_spades2),
    SPADES_K("king_of_spades", Suit.SPADES, Rank.KING, Res.drawable.king_of_spades2),
    SPADES_A("ace_of_spades", Suit.SPADES, Rank.ACE, Res.drawable.ace_of_spades);
}
