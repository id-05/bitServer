package com.ms.imapi2;

import com4j.*;

/**
 * Sequential multisession support interface
 */
@IID("{27354151-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IMultisessionSequential extends IMultisession {
  // Methods:
  /**
   * <p>
   * Is this the first data session on the media?
   * </p>
   * <p>
   * Getter method for the COM property "IsFirstDataSession"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(512) //= 0x200. The runtime will prefer the VTID if present
  @VTID(11)
  boolean isFirstDataSession();


  /**
   * <p>
   * The first sector in the previous session on the media.
   * </p>
   * <p>
   * Getter method for the COM property "StartAddressOfPreviousSession"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(513) //= 0x201. The runtime will prefer the VTID if present
  @VTID(12)
  int startAddressOfPreviousSession();


  /**
   * <p>
   * The last sector in the previous session on the media.
   * </p>
   * <p>
   * Getter method for the COM property "LastWrittenAddressOfPreviousSession"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(514) //= 0x202. The runtime will prefer the VTID if present
  @VTID(13)
  int lastWrittenAddressOfPreviousSession();


  /**
   * <p>
   * Next writable address on the media (also used sectors).
   * </p>
   * <p>
   * Getter method for the COM property "NextWritableAddress"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(515) //= 0x203. The runtime will prefer the VTID if present
  @VTID(14)
  int nextWritableAddress();


  /**
   * <p>
   * Free sectors available on the media.
   * </p>
   * <p>
   * Getter method for the COM property "FreeSectorsOnMedia"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(516) //= 0x204. The runtime will prefer the VTID if present
  @VTID(15)
  int freeSectorsOnMedia();


  // Properties:
}
