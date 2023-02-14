package com.ms.imapi2;

import com4j.*;

/**
 * Represents a single CD/DVD type device, enabling additional commands requiring advanced marshalling code.
 */
@IID("{27354132-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IDiscRecorder2Ex extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Send a command to the device that does not transfer any data
   * </p>
   * @param cdb Mandatory Holder<Byte> parameter.
   * @param cdbSize Mandatory int parameter.
   * @param timeout Mandatory int parameter.
   * @return  Returns a value of type byte
   */

  @VTID(3)
  @ReturnValue(index=2)
  byte sendCommandNoData(
    Holder<Byte> cdb,
    int cdbSize,
    int timeout);


  /**
   * <p>
   * Send a command to the device that requires data sent to the device
   * </p>
   * @param cdb Mandatory Holder<Byte> parameter.
   * @param cdbSize Mandatory int parameter.
   * @param timeout Mandatory int parameter.
   * @param buffer Mandatory Holder<Byte> parameter.
   * @param bufferSize Mandatory int parameter.
   * @return  Returns a value of type byte
   */

  @VTID(4)
  @ReturnValue(index=2)
  byte sendCommandSendDataToDevice(
    Holder<Byte> cdb,
    int cdbSize,
    int timeout,
    Holder<Byte> buffer,
    int bufferSize);


  /**
   * <p>
   * Send a command to the device that requests data from the device
   * </p>
   * @param cdb Mandatory Holder<Byte> parameter.
   * @param cdbSize Mandatory int parameter.
   * @param senseBuffer Mandatory Holder<Byte> parameter.
   * @param timeout Mandatory int parameter.
   * @param buffer Mandatory Holder<Byte> parameter.
   * @param bufferSize Mandatory int parameter.
   * @param bufferFetched Mandatory Holder<Integer> parameter.
   */

  @VTID(5)
  void sendCommandGetDataFromDevice(
    Holder<Byte> cdb,
    int cdbSize,
    Holder<Byte> senseBuffer,
    int timeout,
    Holder<Byte> buffer,
    int bufferSize,
    Holder<Integer> bufferFetched);


    /**
     * <p>
     * Send a DVD Structure to the media
     * </p>
     * @param format Mandatory int parameter.
     * @param data Mandatory Holder<Byte> parameter.
     * @param count Mandatory int parameter.
     */

    @VTID(7)
    void sendDvdStructure(
      int format,
      Holder<Byte> data,
      int count);


                /**
                 * <p>
                 * Sets mode page data using MODE_SELECT10 command
                 * </p>
                 * @param requestType Mandatory com.ms.imapi2.IMAPI_MODE_PAGE_REQUEST_TYPE parameter.
                 * @param data Mandatory Holder<Byte> parameter.
                 * @param byteSize Mandatory int parameter.
                 */

                @VTID(14)
                void setModePage(
                  IMAPI_MODE_PAGE_REQUEST_TYPE requestType,
                  Holder<Byte> data,
                  int byteSize);


                      /**
                       * <p>
                       * The byte alignment requirement mask for this device.
                       * </p>
                       * @return  Returns a value of type int
                       */

                      @VTID(18)
                      int getByteAlignmentMask();


                      /**
                       * <p>
                       * The maximum non-page-aligned transfer size for this device.
                       * </p>
                       * @return  Returns a value of type int
                       */

                      @VTID(19)
                      int getMaximumNonPageAlignedTransferSize();


                      /**
                       * <p>
                       * The maximum non-page-aligned transfer size for this device.
                       * </p>
                       * @return  Returns a value of type int
                       */

                      @VTID(20)
                      int getMaximumPageAlignedTransferSize();


                      // Properties:
                    }
