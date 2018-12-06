package ordo.exceptions;

// This exception is about the location of the config file

public class ConfigurationNotFound extends HidoopMissUsed {
  public ConfigurationNotFound(String expectedPath) {
    super("The configuration file was not found in the location : " + expectedPath);
  }
}
