package ordo.exceptions;

// This exception is about the MapReduce class, if it doesn't exist !

public class MapReduceNotReferenced extends HidoopMissConfiguration {
  public MapReduceNotReferenced() {
    super("The MapReduce class doesn't exist, please check the name and restart !");
  }
}
