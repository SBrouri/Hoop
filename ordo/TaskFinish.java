package ordo;

// This interface is ilmemented in the CallbackTask class 

public interface TaskFinish {
    //
    void waitFinish() throws InterruptedException;
    //
    boolean isProcessing();
    //
    boolean isFailed();
}
