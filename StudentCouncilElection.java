
import java.util.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;

class Candidate {
    private final int id;
    private String name;
    private int votes;

    public Candidate(int id, String name) {
        this.id = id;
        setName(name);
        this.votes = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVotes() {
        return votes;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Candidate name cannot be empty."
            );
        }
        this.name = name.trim();
    }

    public void addVote() {
        votes++;
    }

    public String toString() {
        return id + " - " + name + " : " + votes + " votes";
    }
}

interface Reportable {
    void displayReport();
}

class Election implements Reportable {
    private final List<Candidate> candidates = new ArrayList<>();
    private final Set<String> candidateNames = new HashSet<>();
    private final Map<Integer, Candidate> candidateMap = new HashMap<>();

    public void addCandidate(int id, String name) {
        String key = name.trim().toLowerCase(Locale.ROOT);

        if (candidateNames.contains(key)) {
            throw new IllegalArgumentException(
                "Duplicate candidate name."
            );
        }

        Candidate candidate = new Candidate(id, name);
        candidates.add(candidate);
        candidateNames.add(key);
        candidateMap.put(id, candidate);
    }

    public void castVote(int candidateId) {
        Candidate candidate = candidateMap.get(candidateId);

        if (candidate == null) {
            throw new IllegalArgumentException(
                "Invalid candidate ID."
            );
        }

        candidate.addVote();
    }

    public Candidate searchCandidate(String name) {
        return candidates.stream()
            .filter(c -> c.getName().equalsIgnoreCase(name.trim()))
            .findFirst()
            .orElse(null);
    }

    public void displayReport() {
        System.out.println("\n===== ELECTION RESULTS =====");

        candidates.stream()
            .sorted(Comparator.comparingInt(
                Candidate::getVotes
            ).reversed())
            .forEach(System.out::println);

        if (candidates.isEmpty()) {
            System.out.println("No candidates available.");
            return;
        }

        int highestVotes = candidates.stream()
            .mapToInt(Candidate::getVotes)
            .max()
            .orElse(0);

        List<Candidate> winners = candidates.stream()
            .filter(c -> c.getVotes() == highestVotes)
            .collect(Collectors.toList());

        if (winners.size() == 1) {
            System.out.println("Winner: " + winners.get(0).getName());
        } else {
            System.out.println("Tie between:");
            winners.forEach(c -> System.out.println(c.getName()));
        }

        System.out.println("Total votes: " +
            candidates.stream().mapToInt(
                Candidate::getVotes
            ).sum());

        System.out.println("Candidates: " + candidates.size());
    }

    public void searchByName(String name) {
        Candidate result = searchCandidate(name);

        if (result == null) {
            System.out.println("Candidate not found.");
        } else {
            System.out.println("Found: " + result);
        }
    }

    public void showCandidates() {
        candidates.forEach(System.out::println);
    }

    public void saveResults() throws IOException {
        Path path = Paths.get("election_results.txt");

        List<String> lines = new ArrayList<>();
        lines.add("STUDENT COUNCIL ELECTION RESULTS");
        lines.add("Candidate ID | Name | Votes");

        for (Candidate c : candidates) {
            lines.add(c.getId() + "|" +
                c.getName() + "|" + c.getVotes());
        }

        Files.write(path, lines);
        System.out.println("Results saved to election_results.txt");
    }

    public void readResults() throws IOException {
        Path path = Paths.get("election_results.txt");

        if (!Files.exists(path)) {
            System.out.println("No saved results found.");
            return;
        }

        System.out.println("\n===== SAVED RESULTS =====");

        try (var reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    public void showStreamSummary() {
        System.out.println("\n===== STREAM SUMMARY =====");

        long candidatesWithVotes = candidates.stream()
            .filter(c -> c.getVotes() > 0)
            .count();

        String names = candidates.stream()
            .map(Candidate::getName)
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .collect(Collectors.joining(", "));

        System.out.println(
            "Candidates with votes: " + candidatesWithVotes
        );
        System.out.println("Names alphabetically: " + names);
    }
}

public class StudentCouncilElection {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Election election = new Election();

        try {
            System.out.print("Enter number of candidates: ");
            int n = Integer.parseInt(sc.nextLine().trim());

            if (n <= 0) {
                throw new IllegalArgumentException(
                    "Number of candidates must be positive."
                );
            }

            for (int i = 1; i <= n; i++) {
                System.out.print(
                    "Enter candidate " + i + " name: "
                );
                String name = sc.nextLine();

                election.addCandidate(i, name);
            }

            boolean running = true;

            while (running) {
                System.out.println("\n===== ELECTION MENU =====");
                System.out.println("1. Display candidates");
                System.out.println("2. Cast vote");
                System.out.println("3. Display results");
                System.out.println("4. Search candidate");
                System.out.println("5. Save results");
                System.out.println("6. Read saved results");
                System.out.println("7. Stream summary");
                System.out.println("8. Exit");
                System.out.print("Enter choice: ");

                int choice = Integer.parseInt(
                    sc.nextLine().trim()
                );

                switch (choice) {
                    case 1:
                        election.showCandidates();
                        break;

                    case 2:
                        System.out.print("Enter candidate ID: ");
                        int id = Integer.parseInt(
                            sc.nextLine().trim()
                        );

                        election.castVote(id);
                        System.out.println("Vote recorded successfully.");
                        break;

                    case 3:
                        election.displayReport();
                        break;

                    case 4:
                        System.out.print("Enter candidate name: ");
                        election.searchByName(sc.nextLine());
                        break;

                    case 5:
                        election.saveResults();
                        break;

                    case 6:
                        election.readResults();
                        break;

                    case 7:
                        election.showStreamSummary();
                        break;

                    case 8:
                        running = false;
                        System.out.println("Election system closed.");
                        break;

                    default:
                        System.out.println("Invalid menu choice.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
