public class FoodDeliveryRatingAnalyzer {

    // Calculate total rating for one partner
    public static int calculateTotal(int[] ratings) {
        int total = 0;

        for (int rating : ratings) {
            total += rating;
        }

        return total;
    }

    // Overloaded method: calculate totals for all partners
    public static int[] calculateTotal(int[][] ratings) {
        int[] totals = new int[ratings.length];

        for (int i = 0; i < ratings.length; i++) {
            totals[i] = calculateTotal(ratings[i]);
        }

        return totals;
    }

    // Calculate average rating for one partner
    public static double calculateAverage(int[] ratings) {
        return (double) calculateTotal(ratings) / ratings.length;
    }

    // Overloaded method: calculate averages for all partners
    public static double[] calculateAverage(int[][] ratings) {
        double[] averages = new double[ratings.length];

        for (int i = 0; i < ratings.length; i++) {
            averages[i] = calculateAverage(ratings[i]);
        }

        return averages;
    }

    // Linear Search
    public static int linearSearch(int[][] ratings, int searchValue) {

        for (int i = 0; i < ratings.length; i++) {
            for (int j = 0; j < ratings[i].length; j++) {

                if (ratings[i][j] == searchValue) {
                    return i;
                }
            }
        }

        return -1;
    }

    // Bubble Sort: Highest total to lowest total
    public static void bubbleSort(int[] totals, int[] partnerIndexes) {

        for (int i = 0; i < totals.length - 1; i++) {

            for (int j = 0; j < totals.length - 1 - i; j++) {

                if (totals[j] < totals[j + 1]) {

                    // Swap totals
                    int tempTotal = totals[j];
                    totals[j] = totals[j + 1];
                    totals[j + 1] = tempTotal;

                    // Swap partner indexes
                    int tempIndex = partnerIndexes[j];
                    partnerIndexes[j] = partnerIndexes[j + 1];
                    partnerIndexes[j + 1] = tempIndex;
                }
            }
        }
    }

    public static void main(String[] args) {

        // ===== Food Delivery Partner Ratings =====

        String[] partners = {
            "Partner 1",
            "Partner 2",
            "Partner 3",
            "Partner 4",
            "Partner 5"
        };

        /*
         * Columns:
         * 0 = Delivery Speed
         * 1 = Food Quality
         * 2 = Packaging
         * 3 = Service
         */
        int[][] ratings = {
            {4, 5, 4, 5},
            {3, 4, 5, 4},
            {5, 5, 5, 5},
            {4, 3, 4, 3},
            {5, 4, 5, 4}
        };

        // Calculate totals and averages using overloaded methods
        int[] totals = calculateTotal(ratings);
        double[] averages = calculateAverage(ratings);

        // ===== Display Rating Analysis =====

        System.out.println("===== Food Delivery Rating Analysis =====");
        System.out.println();

        for (int i = 0; i < partners.length; i++) {

            System.out.println(
                partners[i] + " Total : " + totals[i]
            );

            System.out.printf(
                partners[i] + " Average : %.2f%n",
                averages[i]
            );

            System.out.println();
        }

        // ===== Linear Search =====

        int searchValue = 5;

        int searchResult = linearSearch(ratings, searchValue);

        System.out.println("===== Linear Search =====");
        System.out.println();
        System.out.println("Searching for rating value : " + searchValue);

        if (searchResult != -1) {
            System.out.println(
                "Rating " + searchValue + " found in " + partners[searchResult]
            );
        } else {
            System.out.println(
                "Rating " + searchValue + " was not found."
            );
        }

        System.out.println();

        // ===== Prepare Data for Ranking =====

        int[] rankingTotals = totals.clone();
        int[] partnerIndexes = new int[partners.length];

        for (int i = 0; i < partnerIndexes.length; i++) {
            partnerIndexes[i] = i;
        }

        // Apply Bubble Sort
        bubbleSort(rankingTotals, partnerIndexes);

        // ===== Display Ranking =====

        System.out.println("===== Ranking =====");
        System.out.println();

        for (int i = 0; i < partnerIndexes.length; i++) {

            int partnerIndex = partnerIndexes[i];

            System.out.println(
                (i + 1) + ". " + partners[partnerIndex]
            );
        }

        System.out.println();

        // Highest-rated partner
        int highestPartnerIndex = partnerIndexes[0];

        System.out.println(
            "Highest Rated Partner: " + partners[highestPartnerIndex]
        );

        System.out.println(
            "Highest Total Rating: " + rankingTotals[0]
        );
    }
}