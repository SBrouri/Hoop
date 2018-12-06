package ordo.exceptions;

// This exception is about connexion with the daemon (daemon_Id)

public class DaemonInaccessible extends HidoopFail{
  public DaemonInaccessible(String daemonId) {
    super("Can't contact the damon" + daemonId);
  }
}
