import java.util.Scanner;
public class StudentCouncilElection {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of candidates: ");
        int n = sc.nextInt();
        sc.nextLine();
        String[] candidates = new String[n];
        int[] votes = new int[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Enter name of candidate " + (i + 1) + ": ");
            candidates[i] = sc.nextLine();
        }
        System.out.print("Enter number of voters: ");
        int voters = sc.nextInt();
        for (int i = 0; i < voters; i++) {
            System.out.println("\nVoter " + (i + 1));
            System.out.println("Choose a candidate:");
            for (int j = 0; j < n; j++) {
                System.out.println((j + 1) + ". " + candidates[j]);
            }
            System.out.print("Enter candidate number: ");
            int choice = sc.nextInt();
            if (choice >= 1 && choice <= n) {
                votes[choice - 1]++;
                System.out.println("Vote recorded successfully.");
            } else {
                System.out.println("Invalid vote.");
                System.out.println("Vote not counted.");
            }
        }
        System.out.println("\n================================");
        System.out.println("   STUDENT COUNCIL ELECTION");
        System.out.println("        FINAL RESULTS");
        System.out.println("================================");
        for (int i = 0; i < n; i++) {
            System.out.println(candidates[i] + " : " + votes[i] + " votes");
        }
        int winnerIndex = 0;
        for (int i = 1; i < n; i++) {
            if (votes[i] > votes[winnerIndex]) {
                winnerIndex = i;
            }
        }
        System.out.println("\nWinner: " + candidates[winnerIndex]);
        System.out.println("Votes received: " + votes[winnerIndex]);
        sc.close();
    }
}