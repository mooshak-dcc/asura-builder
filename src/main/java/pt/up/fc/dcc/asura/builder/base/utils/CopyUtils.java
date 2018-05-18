package pt.up.fc.dcc.asura.builder.base.utils;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Utilities for copying/cloning objects
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class CopyUtils {

    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(final T obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Map<?, ?>) {
            return (T) deepCopyMap((Map<?, ?>) obj);
        } else if (obj instanceof Collection<?>) {
            return (T) deepCopyCollection((Collection<?>) obj);
        } else if (obj instanceof Object[]) {
            return (T) deepCopyObjectArray((Object[]) obj);
        } else if (obj.getClass().isArray()) {
            return (T) copyPrimitiveArray(obj);
        } else if (obj instanceof Serializable) {
            return (T) deepCopySerializable((Serializable) obj);
        }

        return obj;
    }

    private static Object copyPrimitiveArray(final Object obj) {
        final int length = Array.getLength(obj);
        final Object copy = Array.newInstance(obj.getClass().getComponentType(), length);
        // deep clone not necessary, primitives are immutable
        System.arraycopy(obj, 0, copy, 0, length);
        return copy;
    }

    private static <E> E[] deepCopyObjectArray(final E[] obj) {
        final E[] clone = (E[]) Array.newInstance(obj.getClass().getComponentType(), obj.length);
        for (int i = 0; i < obj.length; i++) {
            clone[i] = deepCopy(obj[i]);
        }

        return clone;
    }

    private static <E> Collection<E> deepCopyCollection(final Collection<E> obj) {
        Collection<E> clone;
        // this is of course far from comprehensive. extend this as needed
        if (obj instanceof LinkedList<?>) {
            clone = new LinkedList<>();
        } else if (obj instanceof SortedSet<?>) {
            clone = new TreeSet<>();
        } else if (obj instanceof Set) {
            clone = new HashSet<>();
        } else {
            clone = new ArrayList<>();
        }

        for (E item : obj) {
            clone.add(deepCopy(item));
        }

        return clone;
    }

    private static <K, V> Map<K, V> deepCopyMap(final Map<K, V> map) {
        Map<K, V> clone;
        // this is of course far from comprehensive. extend this as needed
        if (map instanceof LinkedHashMap<?, ?>) {
            clone = new LinkedHashMap<>();
        } else if (map instanceof TreeMap<?, ?>) {
            clone = new TreeMap<>();
        } else {
            clone = new HashMap<>();
        }

        for (Map.Entry<K, V> entry : map.entrySet()) {
            clone.put(deepCopy(entry.getKey()), deepCopy(entry.getValue()));
        }

        return clone;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Serializable> T deepCopySerializable(T obj) {
        T copy;
        byte[] byteData;

        try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream)) {

            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            byteData = byteOutputStream.toByteArray();
        } catch (IOException e) {
            return obj;
        }

        try (ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteData);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream)) {

            copy = (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return obj;
        }

        return copy;
    }
}
