package util;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class QueryUtil {
    // Generics + Lambdas: predicate/ comparator передаются как функции
    public static <T> List<T> filterAndSort(List<T> src, Predicate<T> filter, Comparator<T> sorter) {
        return src.stream()
                .filter(filter != null ? filter : x -> true)
                .sorted(sorter != null ? sorter : (a, b) -> 0)
                .collect(Collectors.toList());
    }
}