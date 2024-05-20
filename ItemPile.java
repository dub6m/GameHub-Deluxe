/**
 * This interface indicates that the implementing type is capable of representing a "pile" of
 * non-negative integer "items" that can be modified and manipulated.
 */
public interface ItemPile {
    /**
     * Indicates whether the pile is empty.
     * 
     * @return Whether the pile is empty.
     */
    public boolean isEmpty();

    /**
     * Returns the current size of the pile.
     * 
     * @return the size of the pile.
     */
    public int getSize();

    /**
     * Returns the top item of the pile (without removing it), or -1 if the pile is empty.
     * 
     * @return the top item of the pile, or -1 if empty.
     */
    public int seeTop();

    /**
     * Removes the top item from the pile and returns it, or -1 if the pile is empty.
     * 
     * @return the top item of the pile, or -1 if empty.
     */
    public int takeFromPile();

    /**
     * Add the item to the top of the pile.
     * 
     * Returns -1 if the new item is negative, or the item cannot be inserted.
     * 
     * @param item the item to add to the pile.
     * @return the added item
     */
    public int addToPile(int item);

    /**
     * Does an ordered insertion from the top of the pile down.
     * 
     * @param item the item to push down the pile
     * @return the added item
     */
    public int pushDown(int item);

    /**
     * Indiates if the pile is in increasing order from the bottom to the top.
     * @return whether the pile is in increasing order
     */
    public boolean isInIncreasingOrder();
    
    /**
     * Returns a string representation of the pile.
     * @return a string representation of the pile.
     */
    public String toString();
}
