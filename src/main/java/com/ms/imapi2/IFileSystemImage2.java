package com.ms.imapi2  ;

import com4j.*;

/**
 * File system image (rev.2)
 */
@IID("{D7644B2C-1537-4767-B62F-F1387B02DDFD}")
public interface IFileSystemImage2 extends IFileSystemImage {
  // Methods:
  /**
   * <p>
   * Get boot options array for supporting multi-boot
   * </p>
   * <p>
   * Getter method for the COM property "BootImageOptionsArray"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(60) //= 0x3c. The runtime will prefer the VTID if present
  @VTID(57)
  java.lang.Object[] bootImageOptionsArray();


  /**
   * <p>
   * Get boot options array for supporting multi-boot
   * </p>
   * <p>
   * Setter method for the COM property "BootImageOptionsArray"
   * </p>
   * @param pVal Mandatory java.lang.Object[] parameter.
   */

  @DISPID(60) //= 0x3c. The runtime will prefer the VTID if present
  @VTID(58)
  void bootImageOptionsArray(
    java.lang.Object[] pVal);


  // Properties:
}
