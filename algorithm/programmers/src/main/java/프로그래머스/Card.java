package 프로그래머스;

/**
 * @author : ysk
 */
class Card {


    int data;

    Card(int data) {
        this.data = data;
    }

    void play(Card otherCard) {
        if (this.data < otherCard.data) {
            System.out.println("Games up, you lose");
        } else if (this.data > otherCard.data) {
            System.out.println("you win");
        } else {
            System.out.println("you two halve the");
        }
    }

}
