package util;
import java.util.Iterator;

/**
 * A generic list class that can store any type of object.
 * @param <E>
 * @author Pranav Sudheer and Pranav Komarla
 */
public class List<E> implements Iterable<E> {
    private E[] objects;
    private int size;
    private static final int DEFAULT_CAPACITY = 4;

    /**
     * Constructs a list with a default capacity.
     */
    @SuppressWarnings("unchecked")
    public List() {
        this.objects = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Find the index of the object or return -1 if not found
     * @param e the object to find
     * @return the index of the object or -1 if not found
     */
    private int find(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Grow the array when it reaches capacity
     */
    @SuppressWarnings("unchecked")
    private void grow(){
        E[] arr = (E[]) new Object[this.objects.length + DEFAULT_CAPACITY];
        for(int i = 0; i < objects.length; i++) {
            arr[i] = objects[i];
        }
        objects = arr;
    }

    /**
     * Check if the list contains the element
     * @param e the element to check
     * @return true if the list contains the element, false otherwise
     */
    public boolean contains(E e) {
        return find(e) != -1;
    }

    /**
     * Add an element to the list
     * @param e the element to add
     */
    public void add(E e) {
        if (size == objects.length-1) {
            grow();
        }
        objects[size] = e;
        size++;
    }

    /**
     * Remove an element from the list
     * @param e the element to remove
     */
    public void remove(E e) {
        int index = find(e);
        if (index != -1) {
            for (int i = index; i < size-1; i++) {
                objects[i] = objects[i+1];
            }
            size--;
        }
    }

    /**
     * Check if the list is empty
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Return the number of elements in the list
     * @return the number of elements in the list
     */
    public int size() {
        return size;
    }

    /**
     * Return the object at a given index
     * @param index the index of the object
     * @return the object at the given index
     */
    public E get(int index) {
        return objects[index];
    }

    /**
     * Set an element at a given index
     * @param index the index of the element
     * @param e the element to set
     */
    public void set(int index, E e) {
        objects[index] = e;
    }

    /**
     * Return the index of an object, or -1 if not found
     * @param e the object to find
     * @return the index of the object, or -1 if not found
     */
    public int indexOf(E e) {
        return find(e);
    }

    /**
     * Return an iterator for the list
     * @return an iterator for the list
     */
    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    /**
     * Inner class for iterator
     */
    private class ListIterator implements Iterator<E> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {

            return currentIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                currentIndex = 0;
            }
            return objects[currentIndex++];
        }
    }
}
