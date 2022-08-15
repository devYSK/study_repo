package week04.problem2;

import org.w3c.dom.Node;

/**
 * @author : ysk
 */
public class Main {

    public static void main(String[] args) {
        ListNode head = new ListNode();


        for (int i = 0 ; i < 10; i++) {
            ListNode nextNode = new ListNode(i + 1);
            head.add(head, nextNode, i);
        }

        head.printAllNode(head);

        for (int i = 0; i < 9; i++) {
            head.remove(head, 1);
        }

        head.printAllNode(head);
    }
}
