public class continueExample {
    public static void main(String[] args) {
        for(int time= 1; time <= 6; time++) {
            if(time == 4 ) {
                System.out.println("Skipping time: " + time);
                continue;
            }
            System.out.println("Time: " + time);
        }
    }
}
