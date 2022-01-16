import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Map<Type> {
    // this class mapped character keys to type value

    public class Node<Type>{
        char key;
        Type value;
        Node<Type> next;

        Node(char key, Type value) {
            this.key = key;
            this.value = value;
        }

        public Node() {
        }
    }

    private Node<Type> head;
    private int size = 0;


    public void put(char key, Type value) {
        Node<Type> entry = new Node<>(key , value);

        if (head == null) {
            head = entry;
        }else if(key == head.key){
            entry.next = head.next;
            head = entry;
            return;
        }else{
            Node<Type> p = head;
            Node<Type> beforeP = head;

            while(p.next != null){
                beforeP = p;
                p = p.next;
                if(p.key == key){
                    entry.next = p.next;
                    beforeP.next = entry;
                    return;
                }
            }
            p.next = entry;
        }

        size++;
    }

    public Type getValue(char key) {
        if(head == null){
            return null;
        }
        Node<Type> p = head;
        while (p != null) {
            if (p.key == key) {
                Type value = p.value;
                return value;
            }
            p = p.next;
        }

        return null;
    }

    public char[] getAllKeys() {
        Node<Type> p = head;
        char[] output = new char[size];
        int i = 0;

        while (p != null){
            output[i] = p.key;
            p = p.next;
            i++;
        }

        return output;
    }
}
