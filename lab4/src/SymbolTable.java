import java.util.ArrayList;
import java.util.Objects;

// Class to represent entire symbol table
public class SymbolTable <K> {
    // The indicator which tells when to resize the symbolTable
    private final double THRESHOLD = 0.75;

    // ArrayList used for storing the symbolTable elements (array of chains)
    private ArrayList<HashNode<K, Integer> > symbolTable;

    // Current capacity of the symbol table
    private int capacity;

    // Current size of the symbol table
    private int size;

    // Constructor: Initializes capacity, size and empty chains.
    public SymbolTable()
    {
        symbolTable = new ArrayList<>();
        capacity = 10;
        size = 0;

        // Create empty chains
        for (int i = 0; i < capacity; i++)
            symbolTable.add(null);
    }

    // Method used for retrieving the current size of the SymbolTable
    public int size() { return size; }

    // Method used for checking if the SymbolTable is empty
    public boolean isEmpty() { return size() == 0; }

    // Method used for generating a hash code for the given key using the built-in Java function
    private int hashCode (K key) {
        return Objects.hashCode(key);
    }

    // Method used for implementing hash function to find index for a key
    private int getSymbolTableIndex(K key)
    {
        // generate the hash code for the given key
        int hashCode = hashCode(key);
        // find the index for the key corresponding to its hash code
        int index = hashCode % capacity;
        // key.hashCode() could be negative.
        if (index < 0) index = index * -1;
        return index;
    }

    // Method used to check if a key is equal to a node's key and if it has the same hash code
    private boolean elementIsEqualToNode(HashNode<K, Integer> node, K key, int hashCode) {
        return node.key.equals(key) && node.hashCode == hashCode;
    }

    // Method used for resizing the Symbol Table when the size is greater than the THRESHOLD
    private void resize() {
        // save a copy of the current Symbol Table
        ArrayList<HashNode<K, Integer>> oldSymbolTable = new ArrayList<>(symbolTable);

        // empty the current Symbol Table
        symbolTable = new ArrayList<>();
        // double the capacity
        capacity = capacity * 2;
        // reinitialize the size of the Symbol Table
        size = 0;
        // create empty chains again
        for (int i = 0; i < capacity; i++)
            symbolTable.add(null);

        // add again each element to the Symbol Table
        for (HashNode<K, Integer> headNode : oldSymbolTable) {
            while (headNode != null) {
                add(headNode.key);
                // get the next node
                headNode = headNode.next;
            }
        }

    }

    // Method used to remove a given key
    public Integer remove(K key)
    {
        // Apply hash function to find index for given key
        int symbolTableIndex = getSymbolTableIndex(key);
        int hashCode = hashCode(key);
        // Get head of chain
        HashNode<K, Integer> head = symbolTable.get(symbolTableIndex);

        // Search for key in its chain
        HashNode<K, Integer> prev = null;
        while (head != null) {
            // If Key found
            if (elementIsEqualToNode(head, key, hashCode))
                break;

            // Else keep moving in chain
            prev = head;
            head = head.next;
        }

        // If key was not found
        if (head == null)
            return null;

        // Reduce size
        size--;

        // Remove key
        if (prev != null)
            prev.next = head.next;
        else
            symbolTable.set(symbolTableIndex, head.next);

        return head.value;
    }

    // Method used to retrieve the value for a given key
    public Integer get(K key)
    {
        // Find head of chain for given key
        int bucketIndex = getSymbolTableIndex(key);
        int hashCode = hashCode(key);

        HashNode<K, Integer> head = symbolTable.get(bucketIndex);

        // Search key in chain
        while (head != null) {
            if (elementIsEqualToNode(head, key, hashCode))
                return head.value;
            head = head.next;
        }

        // If key not found
        return null;
    }

    // Adds a key value pair to hash
    public Integer add(K key) {
        // Find head of chain for given key
        int bucketIndex = getSymbolTableIndex(key);
        int hashCode = hashCode(key);
        HashNode<K, Integer> head = symbolTable.get(bucketIndex);

        // Check if key is already present
        while (head != null) {
            if (elementIsEqualToNode(head, key, hashCode)) {
                return head.value;
            }
            head = head.next;
        }

        head = symbolTable.get(bucketIndex);
        HashNode<K, Integer> newNode = new HashNode<>(key, size, hashCode);
        newNode.next = head;
        symbolTable.set(bucketIndex, newNode);

        size++;
        // If load factor goes beyond threshold, then double the hash table size
        if ((double) size / capacity >= THRESHOLD) resize();
        return size-1;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("_____________________________\n");
        stringBuilder.append("|   Symbol    |   Position  |\n");
        stringBuilder.append("|-------------|-------------|\n");
        for (var node: symbolTable) {
            while (node != null) {
                stringBuilder.append("|%-12s | %-12d|\n".formatted(node.key, node.value));
                node = node.next;
            }
        }
        stringBuilder.append("|___________________________|\n");
        return stringBuilder.toString();
    }
}
