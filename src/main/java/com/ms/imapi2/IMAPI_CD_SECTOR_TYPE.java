package com.ms.imapi2;

/**
 * <p>
 * The CD sector type for provided data
 * </p>
 */
public enum IMAPI_CD_SECTOR_TYPE {
  /**
   * <p>
   * 2352 bytes per sector of audio data
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_CD_SECTOR_AUDIO, // 0
  /**
   * <p>
   * 2336 bytes per sector of zeros, rare
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_CD_SECTOR_MODE_ZERO, // 1
  /**
   * <p>
   * 2048 bytes per sector of user data
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_CD_SECTOR_MODE1, // 2
  /**
   * <p>
   * 2336 bytes per sector, rare XA form
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  IMAPI_CD_SECTOR_MODE2FORM0, // 3
  /**
   * <p>
   * 2048 bytes per sector, data XA form
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  IMAPI_CD_SECTOR_MODE2FORM1, // 4
  /**
   * <p>
   * 2336 bytes per sector, VideoCD form
   * </p>
   * <p>
   * The value of this constant is 5
   * </p>
   */
  IMAPI_CD_SECTOR_MODE2FORM2, // 5
  /**
   * <p>
   * 2352 bytes per sector, Mode1 data      (with EDC/ECC/scrambling)
   * </p>
   * <p>
   * The value of this constant is 6
   * </p>
   */
  IMAPI_CD_SECTOR_MODE1RAW, // 6
  /**
   * <p>
   * 2352 bytes per sector, Mode2Form0 data (with EDC/ECC/scrambling)
   * </p>
   * <p>
   * The value of this constant is 7
   * </p>
   */
  IMAPI_CD_SECTOR_MODE2FORM0RAW, // 7
  /**
   * <p>
   * 2352 bytes per sector, Mode2Form1 data (with EDC/ECC/scrambling)
   * </p>
   * <p>
   * The value of this constant is 8
   * </p>
   */
  IMAPI_CD_SECTOR_MODE2FORM1RAW, // 8
  /**
   * <p>
   * 2352 bytes per sector, Mode2Form2 data (with EDC/ECC/scrambling)
   * </p>
   * <p>
   * The value of this constant is 9
   * </p>
   */
  IMAPI_CD_SECTOR_MODE2FORM2RAW, // 9
}
