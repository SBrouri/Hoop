package ordo.exceptions;

// This exception is about the configuration file  

public class CanNotReadConfiguration extends HidoopFail{
  public CanNotReadConfiguration() {
    super("Problem reading the configuration file !");
  }
}
