package ordo;

// Exceptions 
import ordo.exceptions.*;
// Format 
import formats.Format;
import formats.Format.Type;
import formats.KVFormat;
// import formats.LineFormat;
import hdfs.HdfsClient;
import hdfs.ChunkIdentifier;
import map.MapReduce;
import hdfs.NameNodeDaemon;
// INPUT AND OUTPUT 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
// Communication tools
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.util.Properties;
// Map and Reduce parocess 
import application.MyMapReduce;

// This class is the implementation of the job interface 
public class JobImpl implements JobInterface {
      protected Type inputFormat, outputFormat;
      protected SortComparator sortComparator;
      protected String inputFname, outputFname;

// File name get/set	
   @Override
   public void setInputFname(String fileName) {
      inputFname = fileName;
   }

   @Override
   public void setOutputFname(String fName) {
      outputFname = fName;
   }

   @Override
   public String getInputFname() {
      return inputFname;
   }

   @Override
   public String getOutputFname() {
      return outputFname;
   }

// Format get/set
   @Override
   public void setInputFormat(Type t) {
      inputFormat = t;
   }

   @Override
   public void setOutputFormat(Type t) {
      outputFormat = t;
   }

   @Override
   public Type getInputFormat() {
      return inputFormat;
   }

   @Override
   public Type getOutputFormat() {
      return outputFormat;
   }

// Sort Comparator get/set
   @Override
   public SortComparator getSortComparator() {
      return sortComparator;
   }

   @Override
   public void setSortComparator(SortComparator ssc) {
      sortComparator = ssc;
   }

//  
   @Override
   public void startJob(MapReduce mr) {
      try {
      if (this.getInputFname() == null) {
         throw new JobNotSetup("inputFname");
      }
      if (this.getInputFormat() == null) {
         throw new JobNotSetup("inputFormat");
      }
      if (this.getOutputFname() == null) { 
         int index = this.getInputFname().lastIndexOf(".");
         if (index == -1) {
            this.setOutputFname(this.getInputFname() + "-res");
         } else {
            this.setOutputFname(this.getInputFname().substring(0, index) + "-res" +
            this.getInputFname().substring(index));
         }
      }
      if (this.getOutputFormat() == null) {
          throw new JobNotSetup("outputFormat");
      }
      // 
      HdfsClient.HdfsWrite(inputFormat, inputFname, 1);
      HdfsClient.HdfsAssociateResult(outputFormat, inputFname, outputFname + "-tmp");

      int cn;
      NameNodeDaemon nnd;
      try {
            // 
            nnd = (NameNodeDaemon) Naming.lookup("//localhost:4141/NameNodeDaemon");
            cn = nnd.getNumberOfChunk(inputFname);
          } catch (RemoteException e) {
              throw new NameNodeUnreachable("the number of fragments");
          }
          // 
          TaskFinish[] taskFinish = new TaskFinish[cn];
          for (int k = 0; k < cn; k++) {
            ChunkIdentifier c;
            try {
                c = nnd.locateChunk(inputFname, k);        
                if (c.getResultLocalFname() == null){
                   throw new NoResultChunkAssociated(c);
                }
                } catch (RemoteException e) {
                    throw new NameNodeUnreachable("the location of the fragment");
                }
		//
             Format reader = selectFormat(inputFormat);
             reader.setFname(c.getChunkLocalFname());
             Format writer = selectFormat(outputFormat);
             writer.setFname(c.getResultLocalFname());
	     // 
             try {
               CallbackTaskImpl callbackTask = new CallbackTaskImpl();
               taskFinish[k] = callbackTask;
               Daemon d = (Daemon) Naming.lookup(c.getNodeIdentifer());
               d.runMap(mr, reader, writer, callbackTask);
             } catch (RemoteException e) {
                 e.printStackTrace();
                 throw new DaemonUnreachable(c.getNodeIdentifer());
             }
            }

            // 
            System.out.println("Waiting the end of the tasks ...");
	    // 
            for (int k = 0; k < taskFinish.length; k++) {
                try {
                    taskFinish[k].waitFinish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(2);
                }
            }
            // 
            HdfsClient.HdfsRead(outputFname + "-tmp", outputFname + "-tmpred");
            // 
            Format reader = selectFormat(outputFormat);
            reader.setFname(outputFname + "-tmpred");
            reader.open(Format.OpenMode.R);
	    //
            Format writer = selectFormat(outputFormat);
            writer.setFname(outputFname);
            writer.open(Format.OpenMode.W);
	    // 
            System.out.println("Execution of the Reduce part ... ");
            mr.reduce(reader, writer);
	    //
            reader.close();
            writer.close();

        } catch (NotBoundException e) {
            e.printStackTrace();            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
// Aux method to select the format 
   private static Format selectFormat(Format.Type typ){
        switch (typ) {
            case KV:
                return new KVFormat();
            case LINE:
                return new LineFormat();
            default:
                throw new FormatNotReferenced();
        }
    }
// The main of the class JOB
   public static void main(String[] argv) {
	 try {
         String configFname = "config/Job.properties";
         if (argv.length > 0) {
             configFname = argv[0];
         }
         Properties prop = new Properties();
         try {
             prop.load(new FileInputStream(configFname));
         } catch (FileNotFoundException e) {
             throw new ConfigNotFound(configFname);
         } catch (IOException e) {
             throw new CanNotReadConfig();
         }
         JobImpl jb = new JobImpl();
         jb.setInputFname(prop.getProperty("inputFname"));
         jb.setOutputFname(prop.getProperty("outputFname"));
         jb.setInputFormat(Format.Type.valueOf(prop.getProperty("inputFormat")));
         jb.setOutputFormat(Format.Type.valueOf(prop.getProperty("outputFormat")));
         MapReduce mr;
	 // 
         switch (prop.getProperty("mapReduce")) {
             case "MyMapReduce":
                mr = new MyMapReduce();
                break;
             default:
                // Error
                throw new MapReduceNotReferenced();
         }
         // Exec. of the Hidoop
         jb.startJob(mr);
         System.out.println("Done");
         System.exit(0);
     } catch (HidoopMissUsed e) {
       // Error
          System.out.println("Error : " + e.getMessage());
	  System.exit(-1);
     } catch (HidoopFail e){
       // Error
       System.out.println("RuntimeException : " + e.getMessage());
       System.exit(1);
     }
   }
}
