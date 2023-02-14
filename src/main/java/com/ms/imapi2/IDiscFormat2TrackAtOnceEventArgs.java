package com.ms.imapi2;

import com4j.*;

/**
 * CD Track-at-once Audio Writer Event Arguments
 */
@IID("{27354140-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IDiscFormat2TrackAtOnceEventArgs extends IWriteEngine2EventArgs {
  // Methods:
  /**
   * <p>
   * The total elapsed time for the current write operation.
   * </p>
   * <p>
   * Getter method for the COM property "CurrentTrackNumber"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(768) //= 0x300. The runtime will prefer the VTID if present
  @VTID(14)
  int currentTrackNumber();


  /**
   * <p>
   * The current write action.
   * </p>
   * <p>
   * Getter method for the COM property "CurrentAction"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IMAPI_FORMAT2_TAO_WRITE_ACTION
   */

  @DISPID(769) //= 0x301. The runtime will prefer the VTID if present
  @VTID(15)
  IMAPI_FORMAT2_TAO_WRITE_ACTION currentAction();


  /**
   * <p>
   * The elapsed time for the current track write or media finishing operation.
   * </p>
   * <p>
   * Getter method for the COM property "ElapsedTime"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(770) //= 0x302. The runtime will prefer the VTID if present
  @VTID(16)
  int elapsedTime();


  /**
   * <p>
   * The estimated time remaining for the current track write or media finishing operation.
   * </p>
   * <p>
   * Getter method for the COM property "RemainingTime"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(771) //= 0x303. The runtime will prefer the VTID if present
  @VTID(17)
  int remainingTime();


  // Properties:
}
