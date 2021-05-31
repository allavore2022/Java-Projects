package structures;

import helpers.KeyValuePair;
import interfaces.ICollection;
import interfaces.IMap;
import interfaces.ISet;
import java.util.Iterator;

/**
 * This class represents a Map that uses a Hash Table.
 * @param <K>
 * @param <V>
 * @author alisallavore
 * @version 1.0
 */
public class Map<K, V> implements IMap<K, V>
{
    private HashTable<KeyValuePair<K,V>> table;

    /**
     * Default constructor for map classes that creates a new Hash Table
     */
    public Map() {
        table = new HashTable<>();
    }

    @Override
    public void add(K key, V value)
    {
        KeyValuePair<K,V> insert = new KeyValuePair<>(key, value);

        //check if the key is already in table
        //if there is then remove it to update the key
        if(keyExists(insert.getKey())){
            table.remove(insert);
        }
        table.add(insert);
    }

    @Override
    public void remove(K key)
    {
        KeyValuePair<K,V> remove = new KeyValuePair<>(key, null);

        table.remove(remove);
    }

    @Override
    public V get(K key)
    {
        KeyValuePair<K,V> getKey = new KeyValuePair<>(key, null);

        //check if the key is not in table
        if(!keyExists(key)){
            return null;
        }
        return table.get(getKey).getValue();

    }

    @Override
    public boolean keyExists(K key)
    {
        KeyValuePair<K,V> search = new KeyValuePair<>(key, null);

        return table.contains(search);
    }

    @Override
    public boolean valueExists(V value)
    {
        //check each keyValuePair in the table
        //if value is equal to the given value
        for(KeyValuePair<K, V> key : table){
            if(key.getValue().equals(value)){
                return true;
            }
        }

        return false;
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
    public Iterator<KeyValuePair<K, V>> iterator()
    {
        return table.iterator();
    }

    @Override
    public ISet<K> keyset()
    {
        //instantiate a new set to hold keys
        Set<K> keys = new Set<>();
        //add each key from the table into set
        for(KeyValuePair<K, V> key : table){
            keys.add(key.getKey());
        }
        return keys;
    }

    @Override
    public ICollection<V> values()
    {
        Set<V> values = new Set<>();
        //add each value from the keyValuePair to the set
        for(KeyValuePair<K, V> key : table){
            values.add(key.getValue());
        }
        return values;
    }

    @Override
    public String toString() {
        return "Map{" +
                "table=" + table +
                '}';
    }
}
