class Node {
    Node next;
    int data;

    public Node(int data, Node nextNode) {
        this.data = data;
        this.next = nextNode;
    }

    public String toString() {
        return Integer.toString(this.data);
    }
}