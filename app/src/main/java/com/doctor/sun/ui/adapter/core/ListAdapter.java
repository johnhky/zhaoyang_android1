package com.doctor.sun.ui.adapter.core;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by rick on 10/20/15.
 */
public abstract class ListAdapter<T, VH extends ViewDataBinding> extends LoadMoreAdapter<T, VH> {
    private List<T> data = new ArrayList<>();

    public ListAdapter(Context context) {
        super(context);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void add(int location, T object) {
        data.add(location, object);
    }

    public boolean addAll(int location, Collection<? extends T> collection) {

        return data.addAll(location, collection);
    }

    public T remove(int location) {
        return data.remove(location);
    }

    public boolean removeAll(Collection<?> collection) {
        return data.removeAll(collection);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public boolean retainAll(Collection<?> collection) {
        return data.retainAll(collection);
    }

    public List<T> subList(int start, int end) {
        return data.subList(start, end);
    }

    public int size() {
        return data.size();
    }

    public Iterator<T> iterator() {
        return data.iterator();
    }

    public void clear() {
        data.clear();
    }

    public boolean containsAll(Collection<?> collection) {
        return data.containsAll(collection);
    }

    public ListIterator<T> listIterator() {
        return data.listIterator();
    }

    public Object[] toArray() {
        return data.toArray();
    }

    public int indexOf(Object object) {
        return data.indexOf(object);
    }

    public T set(int location, T object) {
        return data.set(location, object);
    }

    public <T1> T1[] toArray(T1[] array) {
        return data.toArray(array);
    }

    public ListIterator<T> listIterator(int location) {
        return data.listIterator(location);
    }

    public boolean addAll(Collection<? extends T> collection) {
        return collection != null && data.addAll(collection);
    }

    public boolean add(T object) {
        return data.add(object);
    }

    public T get(int location) {
        return data.get(location);
    }

    public boolean contains(Object object) {
        return data.contains(object);
    }

    public int lastIndexOf(Object object) {
        return data.lastIndexOf(object);
    }

    public boolean remove(Object object) {
        return data.remove(object);
    }
}
