package com.ms.imapi2;

import com4j.*;

/**
 * Provides notification of media erase progress.
 */
@IID("{2735413A-7F64-5B0F-8F00-5D77AFBE261E}")
public interface DDiscFormat2EraseEvents extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Erase progress
   * </p>
   * @param object Mandatory com4j.Com4jObject parameter.
   * @param elapsedSeconds Mandatory int parameter.
   * @param estimatedTotalSeconds Mandatory int parameter.
   */

  @VTID(7)
  void update(
    @MarshalAs(NativeType.Dispatch) Com4jObject object,
    int elapsedSeconds,
    int estimatedTotalSeconds);


  // Properties:
}
