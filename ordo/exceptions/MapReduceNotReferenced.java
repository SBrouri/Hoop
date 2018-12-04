package ordo.exceptions;

// This exception is about the MapReduce class, it doesn't exist !

public class MapReduceNotReferenced extends HidoopMissUsed{
  public MapReduceNotReferenced() {
    super("The MapReduce class doesn't exist, please check the name and restart !");
  }
}
