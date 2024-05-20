public class LinkedItemPile implements ItemPile {
    private Node head = null;
    private Node tail = null;
    private int size = 0;

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int seeTop() {
        if (size > 0) {
            return head.data;
        }
        else {
            return -1;
        }
    }

    @Override
    public int takeFromPile() {
        if (size > 0) {
            Node top = head;
            head = null;
            head = top.next;
            size -= 1;
            return top.data;
        }
        else {
            return -1;
        }
    }

    @Override
    public int addToPile(int item) {
        if (item >= 0) {
            Node newNode = new Node(item, head);
            head = newNode;
            if (tail == null) {
                tail = newNode;
            }
            size += 1;
            return item;
        }
        else {
            return -1;
        }
    }

    @Override
    public int pushDown(int item) {
        Node current = head;
        Node trackNode = head;
        int tracker = 0;
        if (item > 0) {
            while (current != null) {
                if (item <= current.data) {
                    if (current == head) {
                        addToPile(item);
                    }
                    else {
                        Node itemNode = new Node(item, current);
                        while (tracker > 0) {
                            trackNode = trackNode.next;
                            tracker -= 1;
                        }
                        trackNode.next = itemNode;
                        size += 1;
                    }
                    return item;
                }
                else {
                    tracker += 1;
                    if (tracker == getSize()) {
                        Node itemNode = new Node(item, null);
                        tail.next = itemNode;
                        tail = itemNode;
                        size += 1;
                        return item;
                    }
                }
                current = current.next;         
            }
            return -1;
        }
        return -1;
    }

    @Override
    public boolean isInIncreasingOrder() {
        Node current = head;
        boolean ascending = true;
        while (current != null && current.next != null) {
            if (current.data > current.next.data) {
                ascending = false;
                break;
            }
            current = current.next;
        }
        return ascending;
    }
    
    public Node getHead() {
        return head;
    }

    public String toString() {
        if(head == null) { return "Empty LL"; }
        else {
            String result = "<<";
            Node current = head;
            while(current != null) {
                result += " " + current.toString() + " "; 
                current = current.next;
            }
            result += ">>";
            return result;
        }
    }
}
