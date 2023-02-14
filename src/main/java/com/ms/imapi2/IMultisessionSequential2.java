package com.ms.imapi2;

import com4j.*;

/**
 * Sequential multisession support interface (rev.2)
 */
@IID("{B507CA22-2204-11DD-966A-001AA01BBC58}")
public interface IMultisessionSequential2 extends IMultisessionSequential {
  // Methods:
  /**
   * <p>
   * Write unit size (writes must be performed in these units).
   * </p>
   * <p>
   * Getter method for the COM property "WriteUnitSize"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(517) //= 0x205. The runtime will prefer the VTID if present
  @VTID(16)
  int writeUnitSize();


  // Properties:
}
