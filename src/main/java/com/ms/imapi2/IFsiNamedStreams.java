package com.ms.imapi2  ;

import com4j.*;

/**
 * Named stream collection
 */
@IID("{ED79BA56-5294-4250-8D46-F9AECEE23459}")
public interface IFsiNamedStreams extends Com4jObject,Iterable<Com4jObject> {
  // Methods:
  /**
   * <p>
   * Get an enumerator for the named stream collection
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
   * Get a named stream from the collection
   * </p>
   * <p>
   * Getter method for the COM property "Item"
   * </p>
   * @param index Mandatory int parameter.
   * @return  Returns a value of type IFsiFileItem2
   */

  @DISPID(0) //= 0x0. The runtime will prefer the VTID if present
  @VTID(8)
  @DefaultMethod
  IFsiFileItem2 item(
    int index);


  /**
   * <p>
   * Number of named streams in the collection
   * </p>
   * <p>
   * Getter method for the COM property "Count"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(81) //= 0x51. The runtime will prefer the VTID if present
  @VTID(9)
  int count();


  /**
   * <p>
   * Get a non-variant enumerator for the named stream collection
   * </p>
   * <p>
   * Getter method for the COM property "EnumNamedStreams"
   * </p>
   * @return  Returns a value of type IEnumFsiItems
   */

  @DISPID(82) //= 0x52. The runtime will prefer the VTID if present
  @VTID(10)
  IEnumFsiItems enumNamedStreams();


  // Properties:
}
