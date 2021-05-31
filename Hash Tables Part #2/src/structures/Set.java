package structures;

import interfaces.ISet;

import java.util.Iterator;


/**
 * This class represents a Set that utilizes a Hash Table.
 * @param <T>
 * @author alisallavore
 * @version 1.0
 */
public class Set<T> implements ISet<T>
{

    private HashTable<T> table;

    /**
     * Default constructor that creates a new Hash Table
     */
    public Set() {
         table = new HashTable<>();
    }

    @Override
    public void add(T element)
    {
        table.add(element);
    }

    @Override
    public void remove(T element)
    {
        table.remove(element);
    }

    @Override
    public boolean contains(T element)
    {
        return table.contains(element);
    }

    @Override
    public int size()
    {
        return table.size();
    }

    @Override
    public boolean isEmpty()
    {
        return table.isEmpty();
    }

    @Override
    public void clear()
    {
        table.clear();
    }

    @Override
    public T get(T element)
    {
        return table.get(element);
    }

    @Override
    public Iterator<T> iterator()
    {
        return table.iterator();
    }

    @Override
    public ISet<T> union(ISet<T> other)
    {
        //create a new set
        Set<T> joined = new Set<>();

        //loop over this and other and add all elements to the new set.
        for(T element : table){
            joined.add(element);
        }

        for(T otherElement : other){
            joined.add(otherElement);
        }

        return joined;
    }

    @Override
    public ISet<T> intersects(ISet<T> other)
    {
        //create a new set
        Set<T> joined = new Set<>();

        //loop over this and other and add all same elements to the new set
        for(T element : table){
            for(T otherElement : other){
                if(element.equals(otherElement)){
                    joined.add(element);
                }
            }
        }
        return joined;
    }

    @Override
    public ISet<T> difference(ISet<T> other)
    {
        //create a new set
        Set<T> notIncluded = new Set<>();

        //loop over elements
        for(T element : table){
            //if elements are not in the other set then add it to set
            if(!other.contains(element)){
                notIncluded.add(element);
            }
        }

        return notIncluded;
    }

    @Override
    public boolean isSubset(ISet<T> other)
    {
        //counter to keep track of how many other elements are contained in the table
        int inSet = 0;

        //loop over elements
        for(T otherElement : other){
            //check if table contains the other element
            if(table.contains(otherElement)){
                //increment counter
                inSet++;
            }
        }

        //check if the number of elements in the set is equal to the other set's
        //size
        if(inSet == other.size()){
            return true;
        }

        return false;
    }

    @Override
    public boolean isDisjoint(ISet<T> other)
    {
        int inSet = 0;

        //loop over set
        for(T otherElement : other){
            //check if table contains elements from other set
            if(table.contains(otherElement)){
                inSet++;
            }
        }

        //check if there are no items in other set that are contained in table
        if(inSet == 0){
            return true;
        }

        return false;
    }

    @Override
    public boolean isEmptySet()
    {
        return isEmpty();
    }

    @Override
    public String toString() {
        return "Set{" +
                "table=" + table +
                '}';
    }
}
