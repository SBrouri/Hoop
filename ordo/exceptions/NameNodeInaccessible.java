package ordo.exceptions;

// This exception is about connexion with the namenode 

public class NameNodeInaccessible extends HidoopUnpFail {
  public NameNodeInaccessible(String r){
    super("Could not contact NameNode, can not get " + r);
  }
}
