package com.ms.imapi2;

import com4j.*;

/**
 * IDiscMaster2 is used to get an enumerator for the set of CD/DVD (optical) devices on the system
 */
@IID("{27354130-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IDiscMaster2 extends Com4jObject,Iterable<Com4jObject> {
  // Methods:
  /**
   * <p>
   * Enumerates the list of CD/DVD devices on the system (VT_BSTR)
   * </p>
   * <p>
   * Getter method for the COM property "_NewEnum"
   * </p>
   */

  @DISPID(-4) //= 0xfffffffc. The runtime will prefer the VTID if present
  @VTID(7)
  java.util.Iterator<Com4jObject> iterator();

  /**
   * <p>
   * Gets a single recorder's ID (ZERO BASED INDEX)
   * </p>
   * <p>
   * Getter method for the COM property "Item"
   * </p>
   * @param index Mandatory int parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(0) //= 0x0. The runtime will prefer the VTID if present
  @VTID(8)
  @DefaultMethod
  String item(
    int index);


  /**
   * <p>
   * The current number of recorders in the system.
   * </p>
   * <p>
   * Getter method for the COM property "Count"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
  @VTID(9)
  int count();


  /**
   * <p>
   * Whether IMAPI is running in an environment with optical devices and permission to access them.
   * </p>
   * <p>
   * Getter method for the COM property "IsSupportedEnvironment"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(10)
  boolean isSupportedEnvironment();


  // Properties:
}
