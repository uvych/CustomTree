import java.io.Serializable;
import java.util.*;

public class CustomTree extends AbstractList<String> implements Cloneable , Serializable {
    private Entry root;

    private Queue<Entry> removeQueue = new LinkedList<>();  // Queue to deleting tree items

    private ArrayList<Entry> listTree = new ArrayList<>(); // List of tree items values

    private Queue<Entry> addQueue = new LinkedList<>(); // Queue to add tree items

    public CustomTree (){
        this.root = new Entry<>("root");
        root.parent = root;
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
            return listTree.size() ;
    }

    static class Entry<T> implements Serializable{
        String elementName;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;


        public void setAvailableToAddLeftChildren(boolean availableToAddLeftChildren) {
            this.availableToAddLeftChildren = availableToAddLeftChildren;
        }

        public void setAvailableToAddRightChildren(boolean availableToAddRightChildren) {
            this.availableToAddRightChildren = availableToAddRightChildren;
        }

        public boolean isAvailableToAddLeftChildren() {

            return availableToAddLeftChildren;
        }


        public Entry(String name){
            this.elementName = name;
            this.availableToAddLeftChildren = true;
            this.availableToAddRightChildren = true;
        }

        public boolean isAvailableToAddChildren (){
            return this.availableToAddRightChildren || this.availableToAddLeftChildren;
        }

    }

    @Override
    public boolean add(String s) {
        if (root.isAvailableToAddChildren()) {
            Entry entry = new Entry(s);
            addQueue.add(entry);
            entry.parent = root;
            if (root.isAvailableToAddLeftChildren()) {
                root.leftChild = entry;
                root.setAvailableToAddLeftChildren(false);
                listTree.add(entry);
                return true;
            } else {
                root.rightChild = entry;
                root.setAvailableToAddRightChildren(false);
                listTree.add(entry);
                return true;
            }
        } else {
            if (addQueue.peek() != null) {
                root = addQueue.poll();
                return add(s);

            } else if (addQueue.peek() == null){
                for (Entry entry : listTree){
                    addQueue.add(entry);
                    return add(s);
                }
            }
        }
        return true;
    }

    public String getParent(String s){
        String parenName = null;
        for (Entry entry : listTree)
            if (entry.elementName.equals(s)) {
                parenName = entry.parent.elementName;
            }
        return parenName;
    }

    public boolean remove (Object o){
        if(!(o instanceof String)){
            throw new UnsupportedOperationException();
        }
        String s = (String) o;
        // Search for an item and add it ti remove queue
        listTree.stream().filter(entry -> entry.elementName.equals(s)).findFirst().ifPresent(entry -> removeQueue.add(entry));


       // Check left or right child is an item and set the parent flag TRUE;
        assert removeQueue.peek() != null;
        if (removeQueue.peek().elementName.equals(removeQueue.peek().parent.rightChild.elementName)) {
            removeQueue.peek().parent.setAvailableToAddRightChildren(true);
        }
        assert removeQueue.peek() != null;
        if (removeQueue.peek().parent.leftChild.elementName.equals(removeQueue.peek().elementName)) removeQueue.peek().parent.setAvailableToAddLeftChildren(true);

       //addQueue.add(removeQueue.peek().parent);



       while (removeQueue.peek() != null){    // Depth search from the deleted item and remove all elements;
           removeQueue.peek().setAvailableToAddLeftChildren(false);
           assert removeQueue.peek() != null;

           removeQueue.peek().setAvailableToAddRightChildren(false);
           assert removeQueue.peek() != null;

           if (removeQueue.peek().leftChild != null) removeQueue.add(removeQueue.peek().leftChild);

           assert removeQueue.peek() != null;

           if (removeQueue.peek().rightChild != null) {
               assert removeQueue.peek() != null;
               removeQueue.add(removeQueue.peek().rightChild);
           }


           listTree.remove(removeQueue.poll());
       }

        return true;

    }

}
