package tree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : ysk
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Node {

    private int key;
    private int height;
    private Node left;
    private Node right;

    public Node(int key) {
        this.key = key;
    }
}
