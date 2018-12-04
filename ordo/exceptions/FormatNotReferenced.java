package ordo.exceptions;

// This exception is about the format error 

public class FormatNotReferenced extends HidoopMissUsed{
  public FormatNotReferenced() {
      super("The Requested Format class does not exist !");
  }
}
