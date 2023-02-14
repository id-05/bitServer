package com.ms.imapi2;

import com4j.*;

/**
 * CD Disc-At-Once RAW Writer Events
 */
@IID("{27354142-7F64-5B0F-8F00-5D77AFBE261E}")
public interface DDiscFormat2RawCDEvents extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Update to current progress
   * </p>
   * @param object Mandatory com4j.Com4jObject parameter.
   * @param progress Mandatory com4j.Com4jObject parameter.
   */

  @VTID(7)
  void update(
    @MarshalAs(NativeType.Dispatch) Com4jObject object,
    @MarshalAs(NativeType.Dispatch) Com4jObject progress);


  // Properties:
}
