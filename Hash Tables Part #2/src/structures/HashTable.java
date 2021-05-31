package structures;

import interfaces.ICollection;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class represents a hash table that utilizes separate chaining to handle collisions.
 * @param <T>
 * @author alisallavore
 * @version 1.0
 */
public class HashTable<T> implements ICollection<T>, Iterable<T>{

    private static final int DEFAULT_TABLE_SIZE = 10;
    private static final double MAX_LOAD_FACTOR = 2.5;
    private static final double RESIZE_FACTOR = 1.5;

    private Node<T>[] array;
    private int size;
    private int modCount;

    /**
     * Constructor that creates an array with the default table
     * size 10.
     */
    public HashTable() {
        array = new Node[DEFAULT_TABLE_SIZE];
    }

    /**
     * Adds an element to the collection. No specific ordering
     * is required.
     *
     * After the load factor has exceeded 250% the table should rehash
     * all elements into a table that is 50% larger than the previous
     * table size.
     *
     * @param element the new element to put in the collection
     */
    @Override
    public void add(T element) {

        //check to see if load factor is above 250%
        double loadFactor = (double)size / array.length;
        //if yes then resize table
        if(loadFactor >= MAX_LOAD_FACTOR){
            resize();
        }

        //find where the element should be
        int hashCode = Math.abs(element.hashCode());
        int index = hashCode % array.length;

        //check if current table index is null
        //if there is already a node we will add it to the end of the list
        //traverse to find the end of the list then change the last node's pointer
        //to the inserted node
        if(array[index] != null) {
            if(array[index].data.equals(element)){
                return;
//                throw new IllegalArgumentException("Duplicate elements cannot be added.");
            } else {
                //create a temporary node to traverse list
                Node<T> current = array[index];
                while (current.next != null) {

                    //while checking each node in the "linked list"
                    // check if there is a duplicate
                    //if there is return the list
                    if (current.data.equals(element)) {
                        return;
                    }
                    //if there is not a duplicate
                    //append to the end of the "linked list"

                    //increment current index
                    current = current.next;
                }
                if(current.data.equals(element)){
                    return;
//                    throw new IllegalArgumentException("Duplicate elements cannot be added.");
                }
                current.next = new Node(element, null);
                size++;
                modCount++;
            }
        }
        //if it is null then we will insert the node
        else {
            //assign the empty spot
            //assign next to null since there is nothing in the spot
            array[index] = new Node(element, null);
            size++;
            modCount++;
        }

    }

    /**
     * Helper method that resizes the table to 50% larger
     * once load factor exceeds 250%
     */
    private void resize() {
        size = 0;

        Node<T>[] oldTable = array;
        array = new Node[(int) (oldTable.length * RESIZE_FACTOR)];

        //loop over elements in the old table, and if not removed, rehash them
        for(int i = 0; i < oldTable.length; i++){
            if (oldTable[i] != null){

                Node<T> current = oldTable[i];
                while (current != null) {

                    add(current.data);

                    //increment current index
                    current = current.next;
                }
            }
        }
    }

    /**
     * Finds and removes an element from the collection.
     *
     * @throws NoSuchElementException thrown when the
     * element is not found in the collection
     * @param element the element to remove
     */
    @Override
    public void remove(T element) {
        int hashCode = Math.abs(element.hashCode());
        int index = hashCode % array.length;

        Node<T> current = array[index];

        //check if current index is empty
        if(array[index] == null){
            throw new NoSuchElementException("This index is empty");
        //check if "head" of list is equal to the search element
        } else if(array[index].data.equals(element)) {
            array[index] = array[index].next;
            size--;
            modCount++;
        } else {
            //check if we reached the end of the list
            //check if current next node is equals to the search element
            while (current.next != null && !current.next.data.equals((element))) {
                current = current.next;
            }
            if(current.next == null){
                throw new NoSuchElementException("This item is not in list");
            } else {
                current.next = current.next.next;
                size--;
                modCount++;
            }
        }
    }

    /**
     * Reports whether the collection contains an element
     *
     * @param element the element to search for.
     * @return true if the element is found, otherwise false
     */
    @Override
    public boolean contains(T element) {

        int hashCode = Math.abs(element.hashCode());
        int index = hashCode % array.length;

        Node<T> current = array[index];

        while(current != null){
            if (current.data.equals(element)){
                return true;
            }

            current = current.next;
        }

        return false;
    }

    /**
     * Returns the number of elements in the collection.
     *
     * @return the number of elements
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Reports whether the collection is empty or not.
     *
     * @return true if the collection is empty, otherwise false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the collection.
     */
    @Override
    public void clear() {
        array = new Node[DEFAULT_TABLE_SIZE];
        size = 0;
        modCount++;
    }

    /**
     * Generates a toString for the hash table
     * @return String
     */
    @Override
    public String toString() {
        return "HashTable{" +
                "array=" + Arrays.toString(array) +
                ", size=" + size +
                ", modCount=" + modCount +
                '}';
    }

    /**
     * Returns an element in the collection that matches the
     * input parameter according the equals method of the parameter.
     *
     * @param element an element to search for
     * @return a matching element
     */
    @Override
    public T get(T element) {
        int hashCode = Math.abs(element.hashCode());
        int index = hashCode % array.length;

        while(array[index] != null){
            if (array[index].data.equals(element)){
                return array[index].data;
            }

            array[index] = array[index].next;
        }

        return null;
    }



    /**
     * Returns an iterator over the collection.
     *
     * @return an object using the Iterator<T> interface
     */
    @Override
    public Iterator<T> iterator() {
        return new HashTableIterator();
    }

    public class HashTableIterator implements Iterator<T>{

        private Node<T> current;
        private int currentIndex;
        private int savedModCount;

        /**
         * Iterator constructor that begins the loop of going through array.
         */
        public HashTableIterator(){
            //get head of first list -> current

            savedModCount = modCount;

            checkConcurrentChanges();

            //loop over array to traverse to a head of a linked list
            //that is not null
            currentIndex = -1;
            findNextList();
        }

        @Override
        public boolean hasNext() {
            checkConcurrentChanges();
            //return true if the element at index "current" is not null
            //check if node array has

            //go through current linked list, unless its at the end (.next = null),
            // then you'll have to go to the next array index

            return current != null;
        }

        @Override
        public T next() {
            checkConcurrentChanges();

            //move pointer to either next node or array index

            T result = current.data;
            //"increment" current
            current = current.next;
            if(current == null){
                findNextList();
            }
            return result;
        }

        private void checkConcurrentChanges(){
            if(modCount != savedModCount){
                throw new ConcurrentModificationException("You cannot change the structure while iterating");
            }
        }

        private void findNextList(){
            for(int i = currentIndex+1; i < array.length; i++ ){
                //head contains an element
                if(array[i] != null){
                    //keep reference to the head of the list
                    current = array[i];
                    currentIndex = i;
                    break;
                }
            }
        }

        @Override
        public String toString() {
            return "HashTableIterator{" +
                    "current=" + current +
                    ", currentIndex=" + currentIndex +
                    ", savedModCount=" + savedModCount +
                    '}';
        }
    }

    /**
     * An inner class to store elements in the table.
     */
    private static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data, Node next){
            this.data = data;
            this.next = next;
        }

        @Override
        public String toString(){
            String nextElement = "null";
            if(next != null){
                nextElement = next.data.toString();
            }
            return data + " --> " + nextElement;
        }
    }
}
