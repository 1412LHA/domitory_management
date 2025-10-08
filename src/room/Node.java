package room;

public class Node {
    Room info;
    Node next;

    Node() {
    }

    Node(Room x, Node y) {
        this.info = x;
        this.next = y;
    }

    Node(Room x) {
        this(x, null);
    }
}
