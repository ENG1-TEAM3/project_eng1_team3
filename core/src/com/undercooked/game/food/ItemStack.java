package com.undercooked.game.food;

import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class ItemStack implements Iterable<Item> {
    Array<Item> items;

    public ItemStack() {
        this.items = new Array<>();
    }

    public int size() {
        return items.size;
    }

    public Item peek() {
        if (items.size == 0) {
            return null;
        }
        return items.peek();
    }

    public Item pop() {
        return items.pop();
    }

    public void clear() {
        items.clear();
    }

    public void add(Item item) {
        items.add(item);
    }

    public Item get(int index) {
        return items.get(index);
    }

    public Iterator<Item> iterator() {
        return items.iterator();
    }

    /**
     * Returns whether the {@link ItemStack} has an {@link Item}
     * with the provided {@link String} ID or not.
     * @param itemID {@link String} : The ID of the {@link Item}.
     * @return {@code boolean} : {@code true} if it has the ID,
     *                           {@code false} if it does not.
     */
    public boolean hasID(String itemID) {
        for (Item item : items) {
            if (item.getID() == itemID) {
                return true;
            }
        }
        return false;
    }

    /**
     * A static version of the {@link #hasID(String)} function.
     * @param items {@link Array<Item>} : An {@link Array} of the {@link Item}s.
     * @param itemID {@link String} : The ID of the {@link Item}.
     * @return {@code boolean} : {@code true} if it has the ID,
     *                           {@code false} if it does not.
     */
    public static boolean hasID(Array<Item> items, String itemID) {
        for (Item item : items) {
            if (item.getID() == itemID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an {@link Array} of the {@link #items} {@link Array}
     * that the ItemStack uses, either as a copy or not.
     * @param copy {@code boolean} : Whether it should return as a copy or
     *                               not.
     * @return {@link Array<Item>} : An {@link Array} of the {@link Item}s.
     */
    public Array<Item> asArray(boolean copy) {
        // If it's not a copy, just return the array
        if (!copy) {
            return items;
        }

        // If it is a copy, then create a new array and copy over
        // all the values within
        Array<Item> returnArray = new Array<>();
        for (Item item : items) {
            returnArray.add(item);
        }
        // Return the copy.
        return returnArray;
    }

    /**
     * Returns a copy of the {@link #items} {@link Array}
     * that the ItemStack uses.
     * @return {@link Array<Item>} : An {@link Array} of the {@link Item}s.
     */
    public Array<Item> asArray() {
        return asArray(true);
    }
}
