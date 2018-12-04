package ordo.exceptions;

// This exception is about the start of JOB, while the parameter is not initialised, the job can not start 

public class JobNotSetup extends HidoopMissUsed {
  public JobNotSetup(String paramName){
    super("The following parameter : " + paramName + ", the job parameter is not initialised, the job wile not start !");
  }
}
