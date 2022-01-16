public class PQueue {

    private class Node {
        TreeNode treenode;
        Node next;

        public Node(TreeNode treeNode) {
            this.treenode = treeNode;
        }

        public Node() {
        }
    }

    public PQueue() {
    }

    private Node head;
    private int size = 0;


    public boolean isEmpty() {
        return head == null;
    }

    public int getSize() {
        return size;
    }

    public void enqueue(TreeNode treeNode) {
        Node entryNode = new Node(treeNode);
        if (head == null) {
            head = entryNode;
        } else {

            if (entryNode.treenode.size < head.treenode.size
                    || (entryNode.treenode.size == head.treenode.size && entryNode.treenode.exp.charAt(0) < head.treenode.exp.charAt(0))) {

                entryNode.next = head;
                head = entryNode;

            } else {
                Node p = head;

                while (!isAddable(entryNode , p)) {
                    p = p.next;
                }
                entryNode.next = p.next;
                p.next = entryNode;
            }
        }
        size++;
    }

    public TreeNode dequeue() throws QueueException {
        if (isEmpty()) {
            throw new QueueException("the queue is empty");
        }

        TreeNode temp = head.treenode;
        head = head.next;
        size--;

        return temp;
    }

    private boolean isAddable(Node entry, Node p) {
        if (p.next == null) {
            return true;
        } else if (entry.treenode.size >= p.treenode.size && entry.treenode.size < p.next.treenode.size) {
            return true;
        } else return entry.treenode.size == p.next.treenode.size && entry.treenode.exp.charAt(0) < p.next.treenode.exp.charAt(0);
    }
}
