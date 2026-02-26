package com.example.task03;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Task03Main {
    private static final Pattern RUSSIAN_WORD = Pattern.compile("^[а-яё]+$");

    public static void main(String[] args) throws IOException {
        List<Set<String>> anagrams = findAnagrams(new FileInputStream("task03/resources/singular.txt"), Charset.forName("windows-1251"));
        for (Set<String> anagram : anagrams) {
            System.out.println(anagram);
        }

    }

    public static List<Set<String>> findAnagrams(InputStream inputStream, Charset charset) {
        Map<String, TreeSet<String>> result = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            List<String> filteredWords = br.lines()
                    .map(String::toLowerCase)
                    .filter(x -> RUSSIAN_WORD.matcher(x).matches() && x.length() >= 3)
                    .collect(Collectors.toList());
            for (String word : filteredWords) {
                char[] symbols = word.toCharArray();
                Arrays.sort(symbols);
                String key = new String(symbols);
                result.computeIfAbsent(key, a -> new TreeSet<>()).add(word);
            }
        } catch (IOException ignored) { }
        return result.values()
                .stream()
                .filter(x -> x.size() >= 2)
                .collect(Collectors.toList());
    }
}
