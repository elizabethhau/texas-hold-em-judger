import com.sun.deploy.util.StringUtils;

import java.util.*;

/************************** TEXAS HOLD'EM *********************************************
 * Created by Elizabeth Hau on 3/7/16.
 *
 * TEXAS HOLD'EM RULES:
 * The winner of a round of Texas Hold'em is the player who can construct
 * the best five card hand from the cards in his or her hand, and the community cards.
 *
 * This program accepts as its input a collection of hands of cards,
 * and selects the winner from among those hands.
 *
 * This program assumes that the input format and strings are correct.
 ***************************************************************************************
 */
public class TexasHold_em {

    /********************************************************
     ************** PRIVATE CLASS VARIABLES *****************
     ********************************************************/
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;

    /*********************************************************
     ****************** PUBLIC CLASS METHODS *****************
     *********************************************************/

    /**
     * Given a hand represented by an ArrayList of Cards, returns a String array with the first
     * item being an integer (converted to string) representing the rank of the hand (with a
     * royal flush having a rank of 1, so the lower the better), and the second element in
     * the array containing detailed information about the hand
     *
     * @param availCards
     * @return
     */
    public static String[] evaluateHand(ArrayList<Card> availCards) {
        String hand;
        String[] result = new String[2]; // to return

        // counters for ranks and suits such that each index corresponds to a rank and the value
        // is the number of cards with the rank. e.g. rankCounter[0] = # of Aces in availCards
        // and suitCounter[0] = # of spades in availCards
        int[] rankCounter = new int[13]; // A, 2, 3, 4, 5, ..., J, Q, K
        int[] suitCounter = new int[4]; // c, d, h, s

        // initialize the counters
        for(int i = 0; i < rankCounter.length; i++) {
            rankCounter[i] = 0;
        }

        for (int j = 0; j < suitCounter.length; j++) {
            suitCounter[j] = 0;
        }

        // populate the counters
        for(Card c : availCards) {
            rankCounter[c.getRank()-1]++;
            suitCounter[c.getSuitRank()-1]++;
        }

        Card[] cards = availCards.toArray(new Card[availCards.size()]);
        sortbyRank(cards);
        sortBySuit(cards);


        // check each type of possible hand, in their ranks from best to least
        hand = checkRoyalFlush(rankCounter, suitCounter, cards);
        if (hand != null && hand != "") {
            result[0] = "1"; // best possible play
            result[1] = hand;
        }

        if (hand == null || hand == "") {
            hand = checkStraightFlush(suitCounter, cards);
            if (hand != null && hand != "") {
                result[0] = "2"; // second best possible play
                result[1] = hand;
            }
        }

        if (hand == null || hand == "") {
            hand = checkFourOfAKind(rankCounter);
            if (hand != null && hand != "") {
                result[0] = "3"; // third best possible play
                result[1] = hand;
            }
        }

        if (hand == null || hand == "") {
            hand = checkFullHouse(rankCounter);
            if (hand != null && hand != "") {
                result[0] = "4"; // fourth best possible play
                result[1] = hand;
            }
        }

        if (hand == null || hand == "") {
            hand = checkFlush(suitCounter, cards);
            if (hand != null && hand != "") {
                result[0] = "5"; // fifth best possible play
                result[1] = hand;
            }
        }

        if (hand == null || hand == "") {
            hand = checkStraight(rankCounter);
            if (hand != null && hand != "") {
                result[0] = "6"; // sixth best possible play
                result[1] = hand;
            }
        }

        if(hand == null || hand == "") {
            hand = checkThreeOfAKind(rankCounter);
            if (hand != null && hand != "") {
                result[0] = "7"; // seventh best possible play
                result[1] = hand;
            }
        }

        if (hand == null || hand == "") {
            hand = checkTwoPair(rankCounter);
            if (hand != null && hand != "") {
                result[0] = "8"; // eighth best possible play
                result[1] = hand;
            }
        }

        if (hand == null || hand == "") {
            hand = checkOnePair(rankCounter);
            if (hand != null && hand != "") {
                result[0] = "9"; // niinth best possible play
                result[1] = hand;
            }
        }

        if (hand == null || hand == "") {
            hand = getHighCard(rankCounter);
            if (hand != null && hand != "") {
                result[0] = "10";
                result[1] = hand;
            }
        }

        return result;
    }


    /**
     * Given a hashmap containing information about each player and their best hand, return the winner(s)
     * as a list (in case there is a tie)
     * TODO: fix it so that it allows more than two players to tie...
     * @param bestPlaysForAllPlayer
     * @return
     */
    public static ArrayList<String> chooseWinner (HashMap<Integer, String[]> bestPlaysForAllPlayer) {

        // keeps track of potential winners at all times
        Queue<String> potential_winners = new PriorityQueue<>();

        // information about the best player so far used to compare across players
        String bestPlayerSoFar = null;
        int rankOfBestHandSoFar = Integer.MAX_VALUE; // the lower the better
        String bestHandSoFar = "";

        // compare each player to the best so far
        for (Map.Entry<Integer, String[]> entry : bestPlaysForAllPlayer.entrySet()) {
            int pid = entry.getKey();
            String[] hand = entry.getValue();
            int rankOfBest = Integer.parseInt(hand[0]);

            // if the current player's rank is better, update best so far
            if (rankOfBest < rankOfBestHandSoFar) {
                rankOfBestHandSoFar = rankOfBest;
                bestPlayerSoFar = String.valueOf(pid);
                bestHandSoFar = hand[1];
                while (!potential_winners.isEmpty()) {
                    potential_winners.remove();
                }
                potential_winners.add(String.valueOf(pid));

            } else if (rankOfBest == rankOfBestHandSoFar) { // if the ranks of their hands are the same, compare the ranks of the cards

                // parse ranks of best so far
                String[] bestSoFar = bestHandSoFar.split(" ");
                int[] ranksOfBestSoFar = new int[bestSoFar.length-1];
                for (int i = 1; i < bestSoFar.length; i++) {
                    ranksOfBestSoFar[i-1] = Integer.parseInt(bestSoFar[i]);
                }

                // parse current hand
                String[] curr_hand = hand[1].split(" ");
                int[] rankOfCurrHand = new int[curr_hand.length-1];
                for (int i = 1; i < rankOfCurrHand.length; i++) {
                    rankOfCurrHand[i-1] = Integer.parseInt(curr_hand[i]);
                }

                boolean allSameRank = true;

                // compare the ranks of the cards of the two hands
                for (int i = 0; i < ranksOfBestSoFar.length ; i++) {
                    if (rankOfCurrHand[i] > ranksOfBestSoFar[i]) {
                        bestPlayerSoFar = String.valueOf(pid);
                        bestHandSoFar = hand[1];

                        while (!potential_winners.isEmpty()) {
                            potential_winners.remove();
                        }
                        potential_winners.add(String.valueOf(pid));
                        break;
                    }

                    // if the last cards are the same and all cards have been the same so far, we have a tie!
                    if (i == ranksOfBestSoFar.length-1 && allSameRank) {
                        if (!potential_winners.contains(bestPlayerSoFar))
                            potential_winners.add(bestPlayerSoFar);
                        potential_winners.add(String.valueOf(pid));

                    }
                }
            }
        }
        return new ArrayList<String>(potential_winners);
    }


    /*********************************************************
     ****************** PRIVATE CLASS METHODS ****************
     *********************************************************/

    /**
     * This private helper method checks whether a given set of cards contain a royal flush,
     * i.e. Ace, K, Q, J, 10 all in the same suit
     *
     * @param rankCounter
     * @param suitCounter
     * @param cards
     * @return
     */
    private static String checkRoyalFlush(int[] rankCounter, int[] suitCounter, Card[] cards) {
        String result = "";
        // Check for Royal Flush (10 - Ace of the same suit).
        // check if there are 5 of one suit, if not royal is impossible
        if ((rankCounter[9] >= 1 &&       /* 10 */
                rankCounter[10] >= 1 &&   /* Jack */
                rankCounter[11] >= 1 &&  /* Queen */
                rankCounter[12] >= 1 &&  /* King */
                rankCounter[0] >= 1)    /* Ace */
                && (suitCounter[0] > 4 || suitCounter[1] > 4 ||
                suitCounter[2] > 4 || suitCounter[3] > 4)){

            // now loop through the rankCounter for an ace and check subsequent cards.
            // Loop through the aces first since they are the first card to
            // appear in the sorted array of 7 cards.
                royalSearch:
                    for (int i=0;i<3;i++){
                        // Check if first card is the ace.
                        // Ace must be in position 0, 1 or 2
                        if (cards[i].getRank() == 1){
                            // because the ace could be the first card in the array
                            // but the remaining 4 cards could start at position 1,
                            // 2 or 3 loop through checking each possibility.
                            for (int j=1;j<4-i;j++){
                                if ((cards[i+j].getRank() == 10 &&
                                        cards[i+j+1].getRank() == 11 &&
                                        cards[i+j+2].getRank() == 12 &&
                                        cards[i+j+3].getRank() == 13)
                                        &&
                                        (cards[i].getSuitRank() == cards[i+j].getSuitRank() &&
                                                cards[i].getSuitRank() == cards[i+j+1].getSuitRank() &&
                                                cards[i].getSuitRank() == cards[i+j+2].getSuitRank() &&
                                                cards[i].getSuitRank() == cards[i+j+3].getSuitRank())){
                                    // Found royal flush, break and return.
                                        result = "royal flush";
                                        break royalSearch;
                                    }
                                }
                            }
                    }
        }
        return result;
    }

    /**
     * Straight flush: 5 consecutive cards of the same suit.
     *
     * This private helper method checks whether the set of cards contains a straight flush,
     * but doesn't check for the royal flush. This method returns a String indicating whether
     * straight flush has been found along with the 5 cards that made the straight flush.
     *
     * @param suitCounter
     * @param cards
     * @return
     */
    private static String checkStraightFlush(int[] suitCounter, Card[] cards){
        String result = "";

        if (suitCounter[0] > 4 || suitCounter[1] > 4 ||
                suitCounter[2] > 4 || suitCounter[3] > 4){

            // minimum requirements for a straight flush have been met.
            // Loop through available cards looking for 5 consecutive cards of the same suit,
            // start in reverse to get the highest value straight flush
            for (int i=cards.length-1;i>3;i--){
                if ((cards[i].getRank()-ONE == cards[i-ONE].getRank() &&
                        cards[i].getRank()-TWO == cards[i-TWO].getRank() &&
                        cards[i].getRank()-THREE == cards[i-THREE].getRank() &&
                        cards[i].getRank()-FOUR == cards[i-FOUR].getRank())
                        &&
                        (cards[i].getSuit().equals(cards[i-ONE].getSuit())  &&
                                cards[i].getSuit().equals(cards[i-TWO].getSuit()) &&
                                cards[i].getSuit().equals(cards[i-THREE].getSuit()) &&
                                cards[i].getSuit().equals(cards[i-FOUR].getSuit()))){
                    // Found straight flush, break and return.
                    result = "straight_flush " + cards[i].getRank() + " " + cards[i-ONE].getRank() +
                            " " + cards[i-TWO].getRank() + " " + cards[i-THREE].getRank() + " " + cards[i-FOUR].getRank();
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Four of a Kind: four cards of the same rank
     *
     * This method returns a String indicating a four of a kind is found (if found)
     * along with the rank of the four cards. Since I am evaluating all 7 cards at
     * at the same time, there is no way to keep track of which two cards were originally
     * in the player's hands, so can't determine which card is the kicker.
     *
     * @param rankCounter
     * @return
     */
    private static String checkFourOfAKind(int[] rankCounter) {
        String result = "";

        // loop through the rank counters and if any rank has exactly four cards
        // return a string indicating four of a kind has been found along with the rank
        // of the cards
        for (int i = 0; i < rankCounter.length; i++) {
            if(rankCounter[i] == FOUR) {
                result = "four_of_a_kind " + (i+1);
                break;
            }
        }
        return result;
    }

    /**
     * Full House: Three cards of the same rank (three of a kind), and two cards of a different,
     * matching rank (a pair).
     *
     * This method returns a String indicating whether the cards could make a full house.
     * If it can, it also returns the ranks of the three of a kind and the pair for further
     * comparison in case of a tie
     *
     * @param rankCounter
     * @return
     */
    private static String checkFullHouse(int[] rankCounter) {
        String result = "";
        boolean hasThree = false; // keeps track of whether we've found a three of a kind
        boolean hasPair = false; // keeps track of whether found a pair
        int threeRank= -1; // the rank of the three of the kind (if found)
        int pairRank = -1; // the rank of the pair (if found)

        // loop through the rank counters and try to find ranks that have a count of two or three
        for (int i = rankCounter.length-1; i > 0; i--) {
            if (!hasThree || !hasPair) {
                if (rankCounter[i] == THREE) {
                    hasThree = true;
                    threeRank = i+1;
                } else if (rankCounter[i] == TWO) {
                    hasPair = true;
                    pairRank = i+1;
                }
            } else {
                result = "full_house " + threeRank + " " + pairRank;
                break;
            }
        }
        return result;
    }


    /**
     * Flush: Five cards of the same suit.
     *
     * In the event of a tie: The player holding the highest ranked card wins.
     * If necessary, the second-highest, third-highest, fourth-highest, and fifth-highest cards
     * can be used to break the tie. If all five cards are the same ranks, the pot is split.
     * The suit itself is never used to break a tie in poker.
     *
     * @param suitCounter
     * @param cards
     * @return
     */
    private static String checkFlush(int[] suitCounter, Card[] cards) {
        ArrayList<Integer> rank_list = new ArrayList<>(); // list of the ranks of the cards

        String result = "";
        String suit = "";

        // if any of the suits have more than 4 cards (>= 5 cards), we have a Flush!
        if (suitCounter[0] > 4 || suitCounter[1] > 4 || suitCounter[2] > 4 ||
                suitCounter[3] > 4) {
            result = "flush";

            if (suitCounter[0] > 4) {
                suit = "c";
            }

            if (suitCounter[1] > 4) {
                suit = "d";
            }

            if (suitCounter[2] > 4) {
                suit = "h";
            }

            if (suitCounter[3] > 4) {
                suit = "s";
            }

            // get the ranks of the cards
            for (int i = cards.length-1; i >= 0; i--) {
                if(cards[i].getSuit().equals(suit)) {
                    rank_list.add(cards[i].getRank());
                }
            }

            assert(rank_list.size() == 5); // make sure the rank list contains 5 cards

            for (int rank : rank_list) {
                result += " " + rank;
            }
        }
        return result;
    }

    /**
     * Straight: Five cards in sequence.
     *
     * In the event of a tie: Highest ranking card at the top of the sequence wins.
     *
     * Note: The Ace may be used at the top or bottom of the sequence, and is the only card
     * which can act in this manner. A,K,Q,J,T is the highest (Ace high) straight;
     * 5,4,3,2,A is the lowest (Five high) straight.
     *
     * @param rankCounter
     * @return
     */
    private static String checkStraight (int[] rankCounter) {
        String result = "";

        // check for Ace high. Let the high ace be represented by the number 14
        if (rankCounter[0] > 0 && rankCounter[9] > 0 && rankCounter[10] > 0 && rankCounter[11] > 0 && rankCounter[12] > 0) {
            result = "straight 14 13 12 11 10";
        } else {
            for (int i = rankCounter.length-1; i > 3; i--) {
                if (rankCounter[i] > 0 && rankCounter[i-1] > 0 && rankCounter[i-2] > 0 && rankCounter[i-3] > 0 && rankCounter[i-4] > 0) {
                    result = "straight " + (i+1) + " " + i + " " + (i-1) + " " + (i-2) + " " + (i-3);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Three of a kind: Three cards of the same rank, and two unrelated side cards.
     *
     * In the event of a tie: Highest ranking three of a kind wins.
     *
     * Since we don't know which two cards belonged to the player, can't REALLY determine
     * the two side cards...
     * Search from the back so that if we have two three of a kinds, choose the one with the higher rank
     *
     * @param rankCounter
     * @return
     */
    private static String checkThreeOfAKind(int[] rankCounter) {
        String result = "";
        for (int i = rankCounter.length-1; i >= 0; i--) {
            if(rankCounter[i] == THREE) {
                result = "three_of_a_kind " + (i+1);
                break;
            }
        }
        return result;
    }

    /**
     * Two pair: Two cards of a matching rank, another two cards of a different matching rank, and one side card.
     *
     * In the event of a tie: Highest pair wins. If players have the same highest pair, highest second pair wins.
     *
     * @param rankCounter
     * @return
     */
    private static String checkTwoPair(int[] rankCounter) {
        String result = "";
        int numPairs = 0;
        ArrayList<Integer> rank_list = new ArrayList<>();

        for (int i = rankCounter.length-1; i >= 0; i--) {
            if (rankCounter[i] == TWO) {
                numPairs++;
                rank_list.add(i+1);
                if (numPairs == TWO) {
                    result = "two_pair";

                    for (int j = 0; j < 2; j++) {
                        int rank = rank_list.get(j);
                        result += " " + rank;
                    }
                    break;
                }
            }
        }
        return result;
    }

    /**
     * One pair: Two cards of a matching rank, and three unrelated side cards.
     *
     * In the event of a tie: Highest pair wins.
     *
     * @param rankCounter
     * @return
     */
    private static String checkOnePair (int[] rankCounter) {
        String result = "";
        for (int i = rankCounter.length-1; i >= 0; i--) {
            if (rankCounter[i] == TWO) {
                result = "one_pair " + (i+1);
                break;
            }
        }
        return result;
    }

    /**
     * High card: Any hand that does not qualify under a category listed above.
     *
     * The High Card is the card with the highest rank.
     *
     * This method only returns a string indicating it's a high card and the rank of the highest ranked card
     *
     * @param rankCounter
     * @return
     */
    private static String getHighCard (int[] rankCounter) {
        String result = "";
        for (int i = rankCounter.length-1; i >= 0; i--) {
            if (rankCounter[i] == ONE) {
                result = "high_card " + (i+1);
                break;
            }
        }
        return result;
    }

    /**
     * Given a String with spaces separating each card, return an ArrayList of cards
     *
     * @param s
     * @return
     */
    private static ArrayList<Card> listOfCards(String s) {
        ArrayList<Card> cards = new ArrayList<>();
        String[] allCards = s.split(" ");

        // for each card, create a new instance of Card and add it to the list
        for (int i = 0; i < allCards.length; i++) {
            char[] value_suit = allCards[i].toCharArray();
            String value = String.valueOf(value_suit[0]);
            String suit = String.valueOf(value_suit[1]);
            Card c = new Card(value, suit);
            cards.add(c);
        }
        return cards;
    }

    /**
     * Sort a hand based on the values in ascending order
     *
     * @param hand
     */
    private static void sortbyRank(Card[] hand) {
        Arrays.sort(hand, new valueComparator());
    }


    /**
     * Sort a hand based on the suit (ascending order), so clubs, diamonds, hearts, spades
     *
     * @param hand
     */
    private static void sortBySuit(Card[] hand) {
        Arrays.sort(hand, new suitComparator());
    }


    /*********************************************************
     ****************** MAIN METHOD **************************
     *********************************************************/

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numPlayers;

        try {
            System.out.println("Please enter the number of players (a number between 1 and 24):");
            numPlayers = Integer.parseInt(scanner.nextLine());
            if (numPlayers <= 0 || numPlayers > 24) {
                System.out.println("ERROR. Number of players is out of the range. Please enter a number between 1 and 23");
            }
            System.out.println("Please provide the community cards (5 community cards, separated by space):");
            String communityCards = scanner.nextLine();
            ArrayList<Card> communityCardsList = listOfCards(communityCards);
            Card[] comCards = communityCardsList.toArray(new Card[communityCardsList.size()]);

            HashMap<Integer, ArrayList<Card>> playerHands = new HashMap<>();
            for (int i = 0; i < numPlayers; i++) {
                System.out.println("Please provide information for player " + i);
                String playerInfo = scanner.nextLine();
                String[] infoArray = playerInfo.split(" ");
                if(infoArray.length != 3) {
                    System.out.println("ERROR. Incorrect number of information provided");
                    return;
                } else {
                    int pid = Integer.parseInt(infoArray[0]); // player id
                    char[] firstCard = infoArray[1].toCharArray();
                    char[] secondCard = infoArray[2].toCharArray();
                    Card c1 = new Card(String.valueOf(firstCard[0]), String.valueOf(firstCard[1]));
                    Card c2 = new Card(String.valueOf(secondCard[0]), String.valueOf(secondCard[1]));
                    ArrayList<Card> cards = new ArrayList<>();
                    cards.add(c1);
                    cards.add(c2);
                    for (Card c: comCards) {
                        cards.add(c);
                    }
                    playerHands.put(pid, cards);

                }
            }

            HashMap<Integer, String[]> bestPlaysForAllPlayers = new HashMap<>();
            // for each player, get their best possible hand
            for (Map.Entry<Integer, ArrayList<Card>> entry : playerHands.entrySet()) {
                ArrayList<Card> list = entry.getValue();
                int pid = entry.getKey();
                String[] bestPlay = evaluateHand(list);
//                System.out.println("best play for player " + pid + " is:" + bestPlay[1]);
                bestPlaysForAllPlayers.put(pid, bestPlay);
            }

            ArrayList<String> winners = chooseWinner(bestPlaysForAllPlayers);

            System.out.print(StringUtils.join(winners, " "));
        } catch (NumberFormatException e) {
            System.out.println("Please enter an integer for the number of players.");
        }

        /******************** TEST CASES ****************************/
        /* One winner */
        // Three of a kind
        /*  3
            2h 2d Qs 5c Kh
            0 2c As
            1 Kd 5h
            2 Jc Jd
         */

        /* Two way tie */
        /*  4
            5d 4h Ad 8s 9h
            0 Qc Kc
            1 Ah 2c
            2 Ac 3h
            3 Jd 7s
         */

        /* Three way tie */
        /*  4
            5d 4h Ad 8s 9h
            0 As 3c
            1 Ah 2c
            2 Ac 3h
            3 Jd 7s
         */

    }
}
