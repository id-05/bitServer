package com.ms.imapi2;

import com4j.*;

/**
 * Track-at-once Data Writer
 */
@IID("{2735413D-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IDiscFormat2DataEventArgs extends IWriteEngine2EventArgs {
  // Methods:
  /**
   * <p>
   * The total elapsed time for the current write operation.
   * </p>
   * <p>
   * Getter method for the COM property "ElapsedTime"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(768) //= 0x300. The runtime will prefer the VTID if present
  @VTID(14)
  int elapsedTime();


  /**
   * <p>
   * The estimated time remaining for the write operation.
   * </p>
   * <p>
   * Getter method for the COM property "RemainingTime"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(769) //= 0x301. The runtime will prefer the VTID if present
  @VTID(15)
  int remainingTime();


  /**
   * <p>
   * The estimated total time for the write operation.
   * </p>
   * <p>
   * Getter method for the COM property "TotalTime"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(770) //= 0x302. The runtime will prefer the VTID if present
  @VTID(16)
  int totalTime();


  /**
   * <p>
   * The current write action.
   * </p>
   * <p>
   * Getter method for the COM property "CurrentAction"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IMAPI_FORMAT2_DATA_WRITE_ACTION
   */

  @DISPID(771) //= 0x303. The runtime will prefer the VTID if present
  @VTID(17)
  IMAPI_FORMAT2_DATA_WRITE_ACTION currentAction();


  // Properties:
}
