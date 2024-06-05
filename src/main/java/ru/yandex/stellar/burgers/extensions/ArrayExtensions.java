package ru.yandex.stellar.burgers.extensions;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

public class ArrayExtensions {
    public static <E> List<E> getRandomSublist(List<E> list, int sublistSize) {
        if (sublistSize > list.size()) throw new IllegalArgumentException("invalid sublist size");
        List<E> shuffledList = new ArrayList<>(list);
        shuffle(shuffledList);
        return shuffledList.subList(0, sublistSize);
    }
}
