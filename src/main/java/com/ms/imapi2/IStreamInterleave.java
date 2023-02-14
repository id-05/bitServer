package com.ms.imapi2;

import com4j.*;

/**
 * Stream interleave: each stream gets interleaveSize bytes, then cycles to the next stream
 */
@IID("{27354147-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IStreamInterleave extends IStream {
  // Methods:
  /**
   * <p>
   * Initialize the stream with an array of IStreams and interleave sizes
   * </p>
   * @param streams Mandatory Holder<com.ms.imapi2.IStream> parameter.
   * @param interleaveSizes Mandatory Holder<Integer> parameter.
   * @param streamCount Mandatory int parameter.
   */

  @VTID(14)
  void initialize(
    Holder<IStream> streams,
    Holder<Integer> interleaveSizes,
    int streamCount);


  // Properties:
}
