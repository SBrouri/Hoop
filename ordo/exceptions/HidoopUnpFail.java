package ordo.exceptions;

// This abstract class is use by Job main to filter custom unpredictable exceptions that can occure
// during the process and can not be solved 

public abstract class HidoopUnpFail extends RuntimeException {
  public HidoopUnpFail(String msg) {
      super(msg);
  }
}
