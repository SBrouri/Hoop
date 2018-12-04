package ordo.exceptions;

// This exception is about connexion with the namenode 

public class NameNodeUnreachable extends HidoopFail {
  public NameNodeUnreachable(String r){
    super("Could not contact NameNode, can not get " + r);
  }
}
