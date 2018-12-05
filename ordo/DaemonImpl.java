package ordo;

// Format
import formats.Format;
import map.Mapper;

// Communication tools 
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

//
public class DaemonImpl extends UnicastRemoteObject implements Daemon {

    public DaemonImpl() throws RemoteException {}

    class TaskExec implements Runnable {
	Format reader;
        Format writer;        
	Mapper m;      
        CallBack cb;

	//
        public TaskExec(Mapper m, Format reader, Format writer, CallBack cb) {
	this.reader = reader;
        this.writer = writer;            
	this.m = m;          
        this.cb = cb;
        }

	// 
        @Override
        public void run() {
	    //
            this.reader.open(Format.OpenMode.R);
            this.writer.open(Format.OpenMode.W);
            // 
            this.m.map(this.reader, this.writer);
            try {
		//
                this.cb.setTaskDone();
                System.out.println("task end");
            } catch (RemoteException e) {
                // 
                e.printStackTrace();
            } finally {
                this.reader.close();
                this.writer.close();
            }
        }
    }

    @Override
    public void runMap(Mapper m, Format reader, Format writer, CallBack cb) throws RemoteException {
        Thread td = new Thread(new TaskExec(m, reader, writer, cb));
        td.start();
    }

    public static void main(String argv[]) throws RemoteException, AlreadyBoundException, MalformedURLException {
	// 
	int p = 4242;
        int did = 0;
        
	//
        if (argv.length > 0) {
            did = Integer.parseInt(argv[0]);
        }
        if (argv.length > 1) {
           p = Integer.parseInt(argv[1]);
        }
        //
        System.out.println("Start of the " + did + " on " +p);
        LocateRegistry.createRegistry(p);
        Naming.rebind("//localhost:"+ p + "/hidoop_daemon/" + did, new DaemonImpl());
    }
}
