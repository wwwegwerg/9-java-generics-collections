package com.example.task02;

import java.io.*;
import java.nio.file.Files;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SavedList<E extends Serializable> extends AbstractList<E> {
    private final File file;
    private final List<E> list = new ArrayList<>();

    public SavedList(File file) {
        this.file = file;
        loadListFromFile();
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        E oldElement = list.set(index, element);
        saveListToFile();
        return oldElement;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void add(int index, E element) {
        list.add(index, element);
        saveListToFile();
    }

    @Override
    public E remove(int index) {
        E removedElement = list.remove(index);
        saveListToFile();
        return removedElement;
    }

    @SuppressWarnings("unchecked")
    private void loadListFromFile() {
        if (file == null || !file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file.toPath()))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                list.clear();
                list.addAll((List<E>) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            list.clear();
        }
    }

    private void saveListToFile() {
        if (file == null) {
            return;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
            oos.writeObject(new ArrayList<>(list));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save list to file: " + file, e);
        }
    }
}
