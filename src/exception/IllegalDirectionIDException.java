package exception;

public class IllegalDirectionIDException extends Exception {
    public IllegalDirectionIDException(int i) {
        super("The id " + i + " can't be a direction");
    }
}
