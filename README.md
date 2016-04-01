Texas Hold'em Judger
====================

This program accepts as its input a collection of hands of cards, and selects the winner from among those hands.

Input Format
------------

The input reads over `stdin`. Some of the lines contain collections of cards. These cards are represented in the format format `<Value><Suit>`. Value will be one of:

* an integer from `2` to `9` for numbered cards less than ten
* `T` for ten
* `J` for jack
* `Q` for queen
* `K` for king
* `A` for ace

Suit will be one of:
* `h` for hearts
* `d` for diamonds
* `s` for spades
* `c` for clubs

Some example cards are `4d` for four of diamonds, `Ts` for ten of spades, and `Ah` for ace of hearts.

Input Data
----------
The first line of input contains a single integer representing the number of players. This number will always be greater than 0 and less than 24.

The next line contains five space-separated cards, each representing one of the community cards.

The following `n` lines, where `n` is the number of players, contain a single integer representing the id of the player, followed by two space-separated cards representing a hand belonging to a player.

An example input is as follows:

```
3
2h 2d Qs 5c Kh
0 2c As
1 Kd 5h
2 Jc Jd
```

Output Format
-------------

The output, printed to `stdout`, should simply be the id of the winning player. In the example above, the correct output would be:

```
0
```

as player zero has the winning hand.

In the event of a tie, the ids of the winning players should be output on one line, space-separated, in ascending order. For example, for the input:
```
4
5d 4h Ad 8s 9h
0 Qc Kc
1 Ah 2c
2 Ac 3h
3 Jd 7s
```

the correct output would be:

```
1 2
```
as those two players have equivalent hands (pair of Aces, with `9h`, `8s`, and `5d`).

Texas Hold'em Rules
-------------------

The winner of a round of Texas Hold'em is the player who can construct the best _five card hand_ from the cards in his or her hand, and the community cards. In the example above, that winning hand would consist of the `2c` and `As` from `player 0`'s hand, as well as the `2h`, `2d`, and `Kh` from the community cards. This hand would win because the three `2`s give the player a 'three of a kind,' while the next best hand, belonging to player two, would only be able to create a 'two pair' (`Kd Kh 5c 5h Qs`).

The ranking of hands is standard across all variants of poker, and [this wikipedia page](https://en.wikipedia.org/wiki/List_of_poker_hands) contains an explanation of the ranking.
