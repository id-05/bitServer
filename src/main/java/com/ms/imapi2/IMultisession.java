package com.ms.imapi2;

import com4j.*;

/**
 * Generic multisession support interface.
 */
@IID("{27354150-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IMultisession extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Is it possible to write this multi-session type on the current media in its present state.
   * </p>
   * <p>
   * Getter method for the COM property "IsSupportedOnCurrentMediaState"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(256) //= 0x100. The runtime will prefer the VTID if present
  @VTID(7)
  boolean isSupportedOnCurrentMediaState();


  /**
   * <p>
   * Is this multi-session type the one to use on current media
   * </p>
   * <p>
   * Setter method for the COM property "InUse"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(257) //= 0x101. The runtime will prefer the VTID if present
  @VTID(8)
  void inUse(
    boolean value);


  /**
   * <p>
   * Is this multi-session type the one to use on current media
   * </p>
   * <p>
   * Getter method for the COM property "InUse"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(257) //= 0x101. The runtime will prefer the VTID if present
  @VTID(9)
  boolean inUse();


  /**
   * <p>
   * The disc recorder to use to import previous session(s)
   * </p>
   * <p>
   * Getter method for the COM property "ImportRecorder"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IDiscRecorder2
   */

  @DISPID(258) //= 0x102. The runtime will prefer the VTID if present
  @VTID(10)
  IDiscRecorder2 importRecorder();


  // Properties:
}
