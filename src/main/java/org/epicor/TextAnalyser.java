package org.epicor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TextAnalyser {
    // These are the stop words
    static final Set<String> prepositions = Set.of("aboard", "about", "above", "across", "after", "against", "along",
            "amid", "among", "anti", "around", "as", "at", "before", "behind", "below", "beneath", "beside", "besides",
            "between", "beyond", "but", "by", "concerning", "considering", "despite", "down", "during", "except",
            "excepting", "excluding", "following", "for", "from", "in", "inside", "into", "like", "minus", "near",
            "of", "off", "on", "onto", "opposite", "outside", "over", "past", "per", "plus", "regarding", "round",
            "save", "since", "than", "through", "to", "toward", "towards", "under", "underneath", "unlike", "until",
            "up", "upon", "versus", "via", "with", "within", "without");
    static final Set<String> pronouns = Set.of("i", "you", "he", "she", "me", "it", "we", "they", "my", "your", "his", "her",
            "him", "its", "our", "their", "this", "that", "these", "those", "who", "whom", "which", "what", "whose", "all",
            "any", "each", "every", "no one", "none", "some", "anybody", "anyone", "anything", "each other",
            "one another", "myself", "yourself", "himself", "herself", "itself", "ourselves", "themselves");
    static final Set<String> conjunctions = Set.of("and", "but", "for", "or", "nor", "so", "yet", "after", "before",
            "since", "than", "that", "though", "unless", "until", "when", "where", "while", "both", "either", "neither");
    static final Set<String> modalAxillaryVerbs = Set.of("be", "am", "is", "are", "was", "were", "being", "been",
            "has", "have", "had", "having", "do", "does", "did", "will", "would", "shall", "should", "may", "might",
            "can", "could", "must", "ought");
    static final Set<String> articles = Set.of("a", "an", "the");

    public static void main(String[] args) {
        // Get the start time
        long startTime = System.currentTimeMillis();

        try {
            // Read text
            String text = Files.readString(Paths.get("src/main/resources/moby.txt")).toLowerCase();

            // Read words from text
            List<String> words = Arrays.stream(text.split(" "))
                    .filter(w -> !w.isBlank() && !prepositions.contains(w) && !pronouns.contains(w) && !conjunctions.contains(w)
                            && !articles.contains(w) && !modalAxillaryVerbs.contains(w))
                    .map(w -> w.replaceAll("'s$", "")) // Remove plural 's
                    .toList();

            // Count word occurrences
            Map<String, Long> wordVsFrequency = words.stream()
                    .collect(Collectors.groupingBy(w -> w, Collectors.counting()));


            // Sort by frequency
            List<Map.Entry<String, Long>> sortedWords = wordVsFrequency.entrySet().stream()
                    .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                    .toList();

            // Total word count
            System.out.println("Total Number of Words:: " + words.size());

            // Top 5 Words
            System.out.println("Top 5 Words in the given text:: ");
            sortedWords.stream().limit(5).forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

            // Unique Words Sorted
            List<String> uniqueWords = new ArrayList<>(new TreeSet<>(words));
            System.out.println("Top 50 Unique Words:: " + uniqueWords.subList(0, Math.min(50, uniqueWords.size())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        double processingTime = (endTime - startTime) / 1000d;

        System.out.println("Processing Time:: " + processingTime + "s");
    }
}
