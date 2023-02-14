package com.ms.imapi2;

import com4j.*;

/**
 * Pseudo-random based IStream data (implementation dependent)
 */
@IID("{27354145-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IStreamPseudoRandomBased extends IStream {
  // Methods:
  /**
   * <p>
   * Seeds the random number generator and seeks to start of stream
   * </p>
   * @param value Mandatory int parameter.
   */

  @VTID(14)
  void put_Seed(
    int value);


  /**
   * <p>
   * Seeds the random number generator and seeks to start of stream
   * </p>
   * @return  Returns a value of type int
   */

  @VTID(15)
  int get_Seed();


  /**
   * <p>
   * Extended seed method for the random number generator (seeks to start of stream)
   * </p>
   * @param values Mandatory Holder<Integer> parameter.
   * @param eCount Mandatory int parameter.
   */

  @VTID(16)
  void put_ExtendedSeed(
    Holder<Integer> values,
    int eCount);


    // Properties:
  }
