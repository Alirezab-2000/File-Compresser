import java.io.IOException;

public class Hoffman {

   private Map<Integer> numberMap;
   private Map<boolean[]> codeMap;
   char[] keys;
   TreeNode root;

    public Hoffman(Map<Integer> numberMap) throws IOException, QueueException {
        this.numberMap = numberMap;

        keys = numberMap.getAllKeys();
        codeMaker();
    }

    public Map<Integer> getNumberMap() {
        return numberMap;
    }
    public Map<boolean[]> getCodeMap() {
        return codeMap;
    }

    public boolean[] getHoffmanCode(char key){
        return codeMap.getValue(key);
    }
    public char[] getAllKeys (){
        return keys;
    }
    public char getHoffmanKey(boolean[] unCode, int counter){
        TreeNode p = root;
        for (int i = 0; i < counter; i++) {
            boolean bitCode = unCode[i];
            if (bitCode) p = p.right;
            else p = p.left;
        }
        if(p.left == null && p.right == null)
            return p.exp.charAt(0);

        return '\000';
    }
    public int getRepetitionNumber (char key){
        return numberMap.getValue(key);
    }
    public int getCharacterNumber(){
        int answer = 0;
        for(char key : keys){
            answer += numberMap.getValue(key);
        }
        return answer;
    }
    public byte getIdleBit(){
        int idleBit = 0;

        for (char key : keys){
            int repetition = numberMap.getValue(key);
            int codeSize = codeMap.getValue(key).length;

            int bitSize = repetition * codeSize;

            if(bitSize <= idleBit){
                idleBit = idleBit - bitSize;
            }else {
                idleBit = bitSize - idleBit;
                idleBit = 8 - (idleBit % 8);
            }
        }

        return (byte) (idleBit % 8);
    }
    private void inOrder(String s , TreeNode root , Map<boolean[]> codeMap){
        // this method travers the tree in inOrder way and make a boolean array from code and add it to codeMap
        if(root == null){
            return;
        }
        else if(root.left == null && root.right == null){

            boolean[] code = new boolean[s.length()];
            char[] array = s.toCharArray();
            for (int i = 0; i < s.length(); i++) {
                if(array[i] == '0'){
                    boolean b = false;
                    code[i] = b;
                }else {
                    boolean b = true;
                    code[i] = b;
                }
            }
            codeMap.put(root.exp.charAt(0) , code);
        }
        inOrder(s+"0" , root.left , codeMap);
        inOrder(s+"1" , root.right , codeMap);
    }
    private TreeNode hoffmanTreeMaker() throws IOException, QueueException {
        // this method make the hoffman tree
        PQueue pqueue = new PQueue();
        for(char key :  keys){ // adding the treenode to
            int repeat = numberMap.getValue(key);
            TreeNode myTreenode = new TreeNode(String.valueOf(key) , repeat);
            pqueue.enqueue(myTreenode);
        }

        while (pqueue.getSize() > 1){ // making the tree
            TreeNode leftChild = pqueue.dequeue();
            TreeNode rightChild = pqueue.dequeue();
            TreeNode father =
                    new TreeNode(leftChild.exp + rightChild.exp , leftChild.size + rightChild.size);
            father.left = leftChild;
            father.right = rightChild;
            rightChild.parent = father;
            leftChild.parent = father;

            pqueue.enqueue(father);
        }
        root = pqueue.dequeue();
        return root;
    }
    private void codeMaker() throws IOException, QueueException {
        // this method make a Map from character to it code and return it

        TreeNode root = hoffmanTreeMaker();
        codeMap = new Map<>();
        if(root.left == null && root.right == null){
            inOrder("0" , root , codeMap);
        }else {
            inOrder("", root, codeMap);
        }
    }

    public String toString() {
        String output ="";

        for(char key : keys){

            int repetition = numberMap.getValue(key);
            String codeString = "";

            boolean[] code = codeMap.getValue(key);
            for (boolean bit : code){
                if(bit){
                    codeString += "1";
                }else {
                    codeString += "0";
                }
            }
            String line = String.format("%c -> repetition : %-10d code : %-30s" , key , repetition , codeString);
            output+= line + "\r\n";
        }
        return output;
    }

}
