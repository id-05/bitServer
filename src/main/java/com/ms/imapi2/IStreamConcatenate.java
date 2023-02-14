package com.ms.imapi2;

import com4j.*;

/**
 * Stream concatenation
 */
@IID("{27354146-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IStreamConcatenate extends IStream {
  // Methods:
  /**
   * <p>
   * Initialize the stream with two IStreams
   * </p>
   * @param stream1 Mandatory com.ms.imapi2.IStream parameter.
   * @param stream2 Mandatory com.ms.imapi2.IStream parameter.
   */

  @VTID(14)
  void initialize(
    IStream stream1,
    IStream stream2);


  /**
   * <p>
   * Initialize the stream with an array of IStreams
   * </p>
   * @param streams Mandatory Holder<com.ms.imapi2.IStream> parameter.
   * @param streamCount Mandatory int parameter.
   */

  @VTID(15)
  void initialize2(
    Holder<IStream> streams,
    int streamCount);


  /**
   * <p>
   * Append another stream (grow)
   * </p>
   * @param stream Mandatory com.ms.imapi2.IStream parameter.
   */

  @VTID(16)
  void append(
    IStream stream);


  /**
   * <p>
   * Append an array of streams (grow)
   * </p>
   * @param streams Mandatory Holder<com.ms.imapi2.IStream> parameter.
   * @param streamCount Mandatory int parameter.
   */

  @VTID(17)
  void append2(
    Holder<IStream> streams,
    int streamCount);


  // Properties:
}
