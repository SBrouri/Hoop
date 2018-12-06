package ordo.exceptions;

// This exception is about the format error 

public class FormatNotReferenced extends HidoopMissConfiguration {
  public FormatNotReferenced() {
      super("The Requested Format class does not exist !");
  }
}
