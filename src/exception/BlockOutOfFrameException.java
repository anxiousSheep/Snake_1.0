package exception;

public class BlockOutOfFrameException extends Exception {
    public BlockOutOfFrameException(int i, String xOrY) {
        super("The position of " + xOrY + ": " + i + " is out of the walls");
    }
}
