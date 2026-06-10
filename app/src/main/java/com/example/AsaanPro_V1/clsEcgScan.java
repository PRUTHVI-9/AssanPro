package com.example.AsaanPro_V1;

/**
 * Created by Bhalchandra on 10-01-2018.
 */

public class clsEcgScan {
    public static final short RECORD_DURATION_SEC               = 10;
    public static final short SAMPLES_PER_SEC                   = 250;
    public static final short SIZEOF_RECORD_BUFFER              = SAMPLES_PER_SEC * RECORD_DURATION_SEC;

    boolean Emergency;
    String strScanID;
    String strScanFileName;
    String strScanDate;
    String strScanTime;
    String strReferingPhysician;
    String strCommentsByCardiologist;
//    String strCommentsBy_Fname, strCommentsBy_Lname;
    String strReceivedComments;
    //Added by BMP on 25-Jan-2018
    boolean DemoMode;
    String[][] prn_nInterval = new String[12][4];
    String[] prn_nAxis = new String[3];
    String prn_current_HR;
    short HRCopy;
    short[] ECGLeadData0 = new short[SIZEOF_RECORD_BUFFER];
    short[] ECGLeadData1 = new short[SIZEOF_RECORD_BUFFER];
    short[] ECGLeadData2 = new short[SIZEOF_RECORD_BUFFER];
    short[] ECGLeadData3 = new short[SIZEOF_RECORD_BUFFER];
    short[] ECGLeadData4 = new short[SIZEOF_RECORD_BUFFER];
    short[] ECGLeadData5 = new short[SIZEOF_RECORD_BUFFER];
    short[] ECGLeadData6 = new short[SIZEOF_RECORD_BUFFER];
    short[] ECGLeadData7 = new short[SIZEOF_RECORD_BUFFER];
    short[] ECGLeadData8 = new short[SIZEOF_RECORD_BUFFER];
    short[] ECGLeadData9 = new short[SIZEOF_RECORD_BUFFER];
    short[] ECGLeadData10 = new short[SIZEOF_RECORD_BUFFER];
    short[] ECGLeadData11 = new short[SIZEOF_RECORD_BUFFER];
    short[] nInterpretation_Status = new short[18];
    String strInterpretation_Status;
}
