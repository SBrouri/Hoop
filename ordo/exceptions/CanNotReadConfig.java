package ordo.exceptions;

// This exception is about the configuration file  

public class CanNotReadConfig extends HidoopFail{
  public CanNotReadConfig() {
    super("Problem reading the configuration file !");
  }
}
