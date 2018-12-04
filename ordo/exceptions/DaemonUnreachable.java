package ordo.exceptions;

// This exception is about connexion with the daemon (daemon_Id)

public class DaemonUnreachable extends HidoopFail{
  public DaemonUnreachable(String daemonId) {
    super("Can't contact the damon" + daemonId);
  }
}
