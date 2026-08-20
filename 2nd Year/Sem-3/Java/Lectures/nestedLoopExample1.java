public class nestedLoopExample1 {

    public static void main(String[] args) {
        for (int row = 1; row<= 3; row++) {
            for (int number= 1; number<= row; number++) {
                System.out.println(number + " ");
            }
            System.out.println();
        }
    }
}