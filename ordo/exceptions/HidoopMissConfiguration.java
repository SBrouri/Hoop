package ordo.exceptions;

// This abstract class is use by Job main to filter custom exceptions due to a
// missconfiguration of hidoop by the user

public abstract class HidoopMissConfiguration extends Error {
  public HidoopMissConfiguration(String msg) {
    super(msg);
  }
}
