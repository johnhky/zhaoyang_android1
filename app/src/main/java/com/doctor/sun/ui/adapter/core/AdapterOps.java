package com.doctor.sun.ui.adapter.core;

import java.util.List;

/**
 * Created by rick on 13/8/2016.
 */
public interface AdapterOps<T> {
    void insert(int position, T item);

    void insert(T item);

    void update(T item);

    void update(int position, T item);

    void insertAll(List<T> items);

    void swapList(List<T> items);

    void removeItem(T item);

    void removeItemAt(int adapterPosition);

    void clear();

    int size();

    int indexOfImpl(T sortedItem);

    T get(String key);

    int inBetweenItemCount(int adapterPosition, String itemId);

    int inBetweenItemCount(String keyOne, String keyTwo);

    T get(int position);
}
