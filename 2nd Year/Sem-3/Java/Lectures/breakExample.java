public class breakExample {
    public static void main(String[] args) {
        int  targetRollNo = 104;

        for (int rollNo = 101; rollNo <= 110; rollNo++) {
            System.out.println("Checking roll number: " + rollNo);
            if (rollNo == targetRollNo) {
                System.out.println("Roll number " + targetRollNo + " found.");
                break;
            }
        }
    }
}