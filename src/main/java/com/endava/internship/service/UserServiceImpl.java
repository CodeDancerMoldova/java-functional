package com.endava.internship.service;

import com.endava.internship.domain.Privilege;
import com.endava.internship.domain.User;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class UserServiceImpl implements UserService {

    @Override
    public List<String> getFirstNamesReverseSorted(List<User> users) {
        return users.stream().
                map(User::getFirstName).
                sorted(reverseOrder()).
                collect(toList());
    }

    @Override
    public List<User> sortByAgeDescAndNameAsc(final List<User> users) {
        return users.
                stream().
                sorted(comparing(User::getAge)
                        .reversed()
                        .thenComparing(User::getFirstName))
                .collect(toList());
    }

    @Override
    public List<Privilege> getAllDistinctPrivileges(final List<User> users) {
        return users.stream()
                .map(User::getPrivileges)
                .flatMap(List::stream)
                .distinct().collect(toList());
    }

    @Override
    public Optional<User> getUpdateUserWithAgeHigherThan(final List<User> users, final int age) {
        return users.stream()
                .filter(user -> user.getAge() > age)
                .findAny();
    }

    @Override
    public Map<Integer, List<User>> groupByCountOfPrivileges(final List<User> users) {
        return users.stream()
                .collect(groupingBy(user -> user.getPrivileges()
                        .size()));
    }

    @Override
    public double getAverageAgeForUsers(final List<User> users) {
        return users.stream()
                .mapToDouble(User::getAge)
                .average()
                .orElse(-1);
    }

    @Override
    public Optional<String> getMostFrequentLastName(final List<User> users) {
        return users.stream()
                .collect(groupingBy(User::getLastName, counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= 2)
                .reduce((x, y) -> x.getValue() > y.getValue() ? x : y)
                .map(Map.Entry::getKey);
    }

    @Override
    public List<User> filterBy(final List<User> users, final Predicate<User>... predicates) {
        Predicate<User> allPredicates = Arrays.stream(predicates)
                .reduce(Predicate::and)
                .orElse(x -> true);
        return users.stream().filter(allPredicates).collect(toList());

    }

    @Override
    public String convertTo(final List<User> users, final String delimiter, final Function<User, String> mapFun) {
        return users.stream()
                .map(mapFun)
                .collect(joining(delimiter));
    }

    @Override
    public Map<Privilege, List<User>> groupByPrivileges(List<User> users) {
        return users.stream()
                .flatMap(user -> user.getPrivileges().stream()
                        .map(privilege -> new AbstractMap.SimpleEntry<>(privilege, user)))
                .collect(groupingBy(Map.Entry::getKey,
                        mapping(Map.Entry::getValue, toList())));
    }

    @Override
    public Map<String, Long> getNumberOfLastNames(final List<User> users) {
        return users
                .stream()
                .collect(groupingBy(User::getLastName, counting()));
    }


}
