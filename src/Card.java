import java.util.Comparator;

/********************************* CARD ********************************
 * Created by Elizabeth on 3/7/16.
 *
 * This file creates a Card object and two Comparators: suit comparator
 * rank comparator for comparing a card's suit and rank.
 *
 ***********************************************************************
 */
public class Card {


    /********************************************************
     ************** PRIVATE INSTANCE VARIABLES *****************
     ********************************************************/
    private String val; // the numerical value of the card
    private String st; // the suit


    /********************************************************
     ******************** CONSTRUCTOR ***********************
     ********************************************************/

    /**
     * Constructor that creates a Card object out of two string parameters,
     * namely value and suit
     * */
    public Card (String value, String suit) {
        // making sure the first letter is always capitalized
        val = value;
        st = suit;

    }


    /*********************************************************
     **************** PUBLIC INSTANCE METHODS ****************
     *********************************************************/

    /**
     * Getter to return the value of this Card object
     *
     * @return String
     * */
    public String getValue() {
        return val;
    }

    /**
     * Getter to return the rank of this Card object
     * The rank is simply a numeric representation of the value
     * A:1, J:11, Q:12, K:13
     *
     * @return
     */
    public int getRank() {
        int rank;
        if (val.equals("A"))
            rank = 1;
        else if(val.equals("T"))
            rank = 10;
        else if(val.equals("J"))
            rank = 11;
        else if(val.equals("Q"))
            rank = 12;
        else if(val.equals("K"))
            rank = 13;
        else
            rank = Integer.parseInt(val);
        return rank;
    }


    /**
     * Getter to return the suit of this Card object
     *
     * @return String
     * */
    public String getSuit() {
        return st;
    }

    /**
     * Get the rank of suits as integers.
     * The higher the rank the better, so the order from lowest to highest: clubs, diamonds, hearts, spades
     *
     * @return
     */
    public int getSuitRank() {
        // clubs, diamonds, hearts, spades
        if (st.equals("c")) {
            return 1;
        } else if (st.equals("d"))
            return 2;
        else if(st.equals("h"))
            return 3;
        else
            return 4;
    }

    /**
     * String representation of a Card object
     *
     * @return String
     * */
    public String toString() {
        return val + st;
    }


    /*********************************************************
     ****************** MAIN METHOD **************************
     *********************************************************/
    public static void main (String [] args) {
        Card c1 = new Card("ace","Spades");
        System.out.println("Card 1 is: " + c1);

        Card c2 = new Card("3","Clubs");
        System.out.println("Card 2 is: " + c2);

        Card c3 = new Card("Jack","diamonds");
        System.out.println("Card 3 is: " + c3);


        // test code for getSuit()
        System.out.println("\nTesting getSuit():");
        System.out.println("Card 2 is expected to be in \"Clubs\"\tgot: " + c2.getSuit());
        System.out.println("Card 3 is expected to be in \"Diamonds\"\tgot: " + c3.getSuit());

        // test code for getValue()
        System.out.println("\nTesting getValue():");
        System.out.println("The expected value of Card 3 is \"Jack\"\tgot: " + c3.getValue());

    }
} // end of Card class

/**
 * valueComparator allows us to compare ranks of cards
 */
class valueComparator implements Comparator<Object>{
    public int compare(Object card1, Object card2) throws ClassCastException{
        // verify two Card objects are passed in
        if (!((card1 instanceof Card) && (card2 instanceof Card))){
            throw new ClassCastException("A Card object was expected.  Parameter 1 class: " + card1.getClass()
                    + " Parameter 2 class: " + card2.getClass());
        }

        int rank1 = ((Card) card1).getRank();
        int rank2 = ((Card) card2).getRank();

        return rank1 - rank2;
    }
}

/**
 * suitComparator allows us to compare the suits of cards
 */
class suitComparator implements Comparator<Object> {
    public int compare(Object card1, Object card2) throws ClassCastException{
        // verify two Card objects are passed in
        if (!((card1 instanceof Card) && (card2 instanceof Card))){
            throw new ClassCastException("A Card object was expected.  Parameter 1 class: " + card1.getClass()
                    + " Parameter 2 class: " + card2.getClass());
        }

        int suit1 = ((Card)card1).getSuitRank();
        int suit2 = ((Card)card2).getSuitRank();

        return suit1 - suit2;
    }
}