package ordo;

//
import java.util.concurrent.Semaphore;
// 
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

//
public class CallbackTaskImpl extends UnicastRemoteObject implements CallBack, TaskFinish {
    protected TaskStatus status;
    protected Semaphore waitingSemaphore;  

    // the tast status 
    enum TaskStatus {
    WORKING,
    DONE,
    FAILED;
    }
// 
    public CallbackTaskImpl() throws RemoteException {
        this.status = TaskStatus.WORKING;
        this.waitingSemaphore = new Semaphore(0);
    }


// 
    public void setTaskDone() throws RemoteException {
        this.status = TaskStatus.DONE;
        this.waitingSemaphore.release();
    }

//
    public void waitFinish() throws InterruptedException {
            this.waitingSemaphore.acquire();
    }

// 
   public boolean isProcessing() {
        return this.status == TaskStatus.WORKING;
    }

//
    @Override
    public boolean isFailed() {
        return this.status == TaskStatus.FAILED;
    }
}
