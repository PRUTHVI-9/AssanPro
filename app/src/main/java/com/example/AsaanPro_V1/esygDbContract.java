package com.example.AsaanPro_V1;

import android.provider.BaseColumns;

public final class esygDbContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public esygDbContract() {}

    private static final String BLOB_TYPE = " BLOB";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String AUTOINCREMENT = " AUTOINCREMENT";
    private static final String UNIQUE = " UNIQUE";
    private static final String COMMA_SEP = ",";

    //Inner class that defines the table contents
    public static abstract class EcgMaster implements BaseColumns {
        public static final String TABLE_NAME = "EcgMaster";

        public static final String COLUMN_NAME_EventID                  = "EventID";
        public static final String COLUMN_NAME_PatientID                = "PatientID";
        public static final String COLUMN_NAME_DeviceID                 = "DeviceID";
        public static final String COLUMN_NAME_EcgDate                  = "EcgDate";
        public static final String COLUMN_NAME_EcgTime                  = "EcgTime";
        public static final String COLUMN_NAME_EcgPrintCount            = "EcgPrintCount";
        public static final String COLUMN_NAME_Emergency                = "Emergency";
        public static final String COLUMN_NAME_AutoInterpretation       = "AutoInterpretation";
        public static final String COLUMN_NAME_CurrentInterpretation    = "CurrentInterpretation";
        public static final String COLUMN_NAME_Comments                 = "Comments";

        public static final String SQL_CREATE_ECG_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_EventID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PatientID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_DeviceID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EcgDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EcgTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EcgPrintCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Emergency + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_AutoInterpretation + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CurrentInterpretation + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Comments + TEXT_TYPE +
                        ")";
    }

     //Inner class that defines the table contents
     public static abstract class SearchMaster implements BaseColumns {
         public static final String TABLE_NAME = "SearchMaster";

         public static final String COLUMN_NAME_PatientID       = "PatientID";
         public static final String COLUMN_NAME_Patient_Fname   = "Patient_Fname";
         public static final String COLUMN_NAME_Patient_Mname   = "Patient_Mname";
         public static final String COLUMN_NAME_Patient_Lname   = "Patient_Lname";
         public static final String COLUMN_NAME_Patient_DOB     = "Patient_DOB";
         public static final String COLUMN_NAME_Patient_Mobile1 = "Patient_Mobile1";
         public static final String COLUMN_NAME_Patient_Mobile2 = "Patient_Mobile2";
         public static final String COLUMN_NAME_Emergency       = "Emergency";

         public static final String SQL_CREATE_SEARCH_MASTER =
                 "CREATE TABLE " + TABLE_NAME + " (" +
                         COLUMN_NAME_PatientID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + UNIQUE + COMMA_SEP +
                         COLUMN_NAME_Patient_Fname + TEXT_TYPE + COMMA_SEP +
                         COLUMN_NAME_Patient_Mname + TEXT_TYPE + COMMA_SEP +
                         COLUMN_NAME_Patient_Lname + TEXT_TYPE + COMMA_SEP +
                         COLUMN_NAME_Patient_DOB + TEXT_TYPE + COMMA_SEP +
                         COLUMN_NAME_Patient_Mobile1 + TEXT_TYPE + COMMA_SEP +
                         COLUMN_NAME_Patient_Mobile2 + TEXT_TYPE + COMMA_SEP +
                         COLUMN_NAME_Emergency + TEXT_TYPE +
                         ")";
     }

    public static abstract class PatientMaster implements BaseColumns {
        public static final String TABLE_NAME = "PatientMaster";

        public static final String COLUMN_NAME_PatientID = "PatientID";
        public static final String COLUMN_NAME_EventID = "EventID";

        public static final String COLUMN_NAME_PatientCode = "PatientCode";
        public static final String COLUMN_NAME_Patient_Fname = "Patient_Fname";
        public static final String COLUMN_NAME_Patient_Mname = "Patient_Mname";
        public static final String COLUMN_NAME_Patient_Lname = "Patient_Lname";
        public static final String COLUMN_NAME_Patient_DOB = "Patient_DOB";
        public static final String COLUMN_NAME_Patient_Gender = "Patient_Gender";
        public static final String COLUMN_NAME_Patient_Mobile1 = "Patient_Mobile1";
        public static final String COLUMN_NAME_Patient_Mobile2 = "Patient_Mobile2";
        public static final String COLUMN_NAME_Patient_Mailid = "Patient_Mailid";
        public static final String COLUMN_NAME_Patient_Addr1 = "Patient_Addr1";
        public static final String COLUMN_NAME_Patient_Addr2 = "Patient_Addr2";
        public static final String COLUMN_NAME_Patient_City = "Patient_City";
        public static final String COLUMN_NAME_Patient_PinCode = "Patient_PinCode";
        public static final String COLUMN_NAME_Patient_State = "Patient_State";
        public static final String COLUMN_NAME_Patient_CountryID = "Patient_CountryID";
        public static final String COLUMN_NAME_PatientOccupation = "PatientOccupation";
        public static final String COLUMN_NAME_CreatedBy = "CreatedBy";
        public static final String COLUMN_NAME_CreatedDate = "CreatedDate";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_IDNumber = "IDNumber";
        public static final String COLUMN_NAME_IDType = "IDType";
        public static final String COLUMN_NAME_IsInsured = "IsInsured";
        public static final String COLUMN_NAME_InsuranceProviderID = "InsuranceProviderID";

        public static final String COLUMN_NAME_OnlinePatientID = "OnlinePatientID";

        public static final String SQL_CREATE_PATIENT_MASTER =
            "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_PatientID + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_EventID + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_PatientCode + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_Fname + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_Mname + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_Lname + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_DOB + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_Gender + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_Mobile1 + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_Mobile2 + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_Mailid + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_Addr1 + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_Addr2 + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_City + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_PinCode + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_State + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_Patient_CountryID + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_PatientOccupation + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_CreatedBy + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_CreatedDate + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_IDNumber + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_IDType + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_IsInsured + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_InsuranceProviderID + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_OnlinePatientID + INTEGER_TYPE +
            ")";
    }

    public static abstract class EventMaster implements BaseColumns {
        public static final String TABLE_NAME = "EventMaster";

        public static final String COLUMN_NAME_EventID = "EventID";
        public static final String COLUMN_NAME_PatientID = "PatientID";
        public static final String COLUMN_NAME_EventDateTime = "EventDateTime";
        public static final String COLUMN_NAME_Eventtype = "Eventtype";

        public static final String SQL_CREATE_EVENT_MASTER =
            "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_EventID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + UNIQUE + COMMA_SEP +
                COLUMN_NAME_PatientID + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_EventDateTime + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Eventtype + TEXT_TYPE +
            ")";
    }

    public static abstract class HealthMaster implements BaseColumns {
        public static final String TABLE_NAME                   = "HealthMaster";

        public static final String COLUMN_NAME_EventID              = "EventID";
        public static final String COLUMN_NAME_PatientID            = "PatientID";
        public static final String COLUMN_NAME_BPSystolic           = "BPSystolic";
        public static final String COLUMN_NAME_BPDiastolic          = "BPDiastolic";
        public static final String COLUMN_NAME_Weight               = "Weight";
        public static final String COLUMN_NAME_Height               = "Height";
        public static final String COLUMN_NAME_HistoryText          = "HistoryText";
        public static final String COLUMN_NAME_HistoryCondIDs       = "HistoryCondIDs";
        public static final String COLUMN_NAME_HistoryConditions    = "HistoryConditions";
        public static final String COLUMN_NAME_BMI                  = "BMI";
        public static final String COLUMN_NAME_TropT                = "TropT";
        public static final String COLUMN_NAME_Hb                   = "Hb";
        public static final String COLUMN_NAME_HbA1c                = "HbA1c";
        public static final String COLUMN_NAME_RBS                  = "RBS";
        public static final String COLUMN_NAME_TC                   = "TC";
        public static final String COLUMN_NAME_LDL                  = "LDL";
        public static final String COLUMN_NAME_HDL                  = "HDL";
        public static final String COLUMN_NAME_TG                   = "TG";

        public static final String SQL_CREATE_PATIENT_HEALTH_MASTER =
            "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_EventID + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_PatientID + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_BPSystolic + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_BPDiastolic + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Weight + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Height + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_HistoryText + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_HistoryCondIDs + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_HistoryConditions + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_BMI + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_TropT + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_Hb + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_HbA1c + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_RBS + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_TC + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_LDL + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_HDL + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_TG + TEXT_TYPE +
            ")";
    }

    public static abstract class ConfigMaster implements BaseColumns {
        public static final String TABLE_NAME                   = "ConfigMaster";

        public static final String COLUMN_NAME_GainScale              = "GainScale";
        public static final String COLUMN_NAME_SpeedScale             = "SpeedScale";
        public static final String COLUMN_NAME_ReportingCenter        = "ReportingCenter";
        public static final String COLUMN_NAME_PreviewBeforeSend      = "PreviewBeforeSend";
        public static final String COLUMN_NAME_PrintBeforeSend        = "PrintBeforeSend";
        public static final String COLUMN_NAME_ColorPrint             = "ColorPrint";
        public static final String COLUMN_NAME_HRPrint                = "HRPrint";
        public static final String COLUMN_NAME_BordersPrint           = "BordersPrint";
        public static final String COLUMN_NAME_ReportGridType         = "ReportGridType";
        public static final String COLUMN_NAME_LongLead               = "LongLead";
        public static final String COLUMN_NAME_QRSLead                = "QRSLead";
        public static final String COLUMN_NAME_ServerIP               = "ServerIP";
        public static final String COLUMN_NAME_PrintGainScale         = "PrintGainScale";
        public static final String COLUMN_NAME_PrinterEmailID         = "PrinterEmailID";
        public static final String COLUMN_NAME_A4ReportType           = "A4ReportType";
        public static final String COLUMN_NAME_A4ReportSubtype        = "A4ReportSubtype";
        public static final String COLUMN_NAME_SleepTimeoutMinutes    = "SleepTimeoutMinutes";
        public static final String COLUMN_NAME_PendingEmergencyLimit  = "PendingEmergencyLimit";
        public static final String COLUMN_NAME_PrintReportFormat      = "PrintReportFormat";
        public static final String COLUMN_NAME_ThReportType           = "ThReportType";
        public static final String COLUMN_NAME_ThPrintDataLengthSec   = "ThPrintDataLengthSec";
        public static final String COLUMN_NAME_ShortDetails           = "ShortDetails";
        public static final String COLUMN_NAME_PhysicianEmailID       = "PhysicianEmailID";
        public static final String COLUMN_NAME_OriginatingCenterName  = "OriginatingCenterName";
        public static final String COLUMN_NAME_ReportDisclaimer       = "ReportDisclaimer";
        public static final String COLUMN_NAME_InstitutionLogoURL       = "InstitutionLogoURL";
        public static final String COLUMN_NAME_InstitutionLogo       = "InstitutionLogo";
        public static final String COLUMN_NAME_InstitutionAddress       = "InstitutionAddress";
        public static final String COLUMN_NAME_PhysicianAddress       = "PhysicianAddress";
        public static final String COLUMN_NAME_DeviceLatitude       = "DeviceLatitude";
        public static final String COLUMN_NAME_DeviceLongitude       = "DeviceLongitude";

//        public static final String COLUMN_NAME_DrSign       = "DrSign";

//        public static final String COLUMN_NAME_EmailSubject           = "EmailSubject";
//        public static final String COLUMN_NAME_DefaultEcgScreen       = "DefaultEcgScreen";
//        public static final String COLUMN_NAME_PhysicianName          = "PhysicianName";
//        public static final String COLUMN_NAME_PhysicianPassword      = "PhysicianPassword";
//        public static final String COLUMN_NAME_InstitutionGUID        = "InstitutionGUID";
//        public static final String COLUMN_NAME_FTPUser                = "FTPUser";
//        public static final String COLUMN_NAME_FTPPassword            = "FTPPassword";
//        public static final String COLUMN_NAME_FTPPath                = "FTPPath";
//        public static final String COLUMN_NAME_ReportingCenterName    = "ReportingCenterName";

        public static final String SQL_CREATE_CONFIG_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_GainScale + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_SpeedScale + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ReportingCenter + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PreviewBeforeSend + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrintBeforeSend + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ColorPrint + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_HRPrint + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_BordersPrint + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ReportGridType + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LongLead + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_QRSLead + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ServerIP + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrintGainScale + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrinterEmailID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_A4ReportType + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_A4ReportSubtype + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SleepTimeoutMinutes + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PendingEmergencyLimit + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrintReportFormat + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ThReportType + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ThPrintDataLengthSec + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ShortDetails + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PhysicianEmailID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OriginatingCenterName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ReportDisclaimer + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_InstitutionLogoURL + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_InstitutionLogo + BLOB_TYPE + COMMA_SEP +
                        COLUMN_NAME_InstitutionAddress + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PhysicianAddress + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DeviceLatitude + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DeviceLongitude + TEXT_TYPE +
//                        COLUMN_NAME_DrSign + BLOB_TYPE +
//
//                        COLUMN_NAME_EmailSubject + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_DefaultEcgScreen + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PhysicianName + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PhysicianPassword + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_InstitutionGUID + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_FTPUser + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_FTPPassword + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_FTPPath + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ReportingCenterName + TEXT_TYPE + COMMA_SEP +
                ")";
    }

    public static abstract class HWMaster implements BaseColumns {
        public static final String TABLE_NAME                   = "HWMaster";

        public static final String COLUMN_NAME_HWMainVersion          = "HWMainVersion";
        public static final String COLUMN_NAME_HWSubVersion           = "HWSubVersion";
        public static final String COLUMN_NAME_ECGUpdateIntervalMSec  = "ECGUpdateIntervalMSec";
        public static final String COLUMN_NAME_StrHWDeviceID          = "StrHWDeviceID";
        public static final String COLUMN_NAME_strI2I_PhysicianName          = "strI2I_PhysicianName";
        public static final String COLUMN_NAME_strI2I_PhysicianPassword          = "strI2I_PhysicianPassword";
        public static final String COLUMN_NAME_strI2I_InstitutionGUID          = "strI2I_InstitutionGUID";
        public static final String COLUMN_NAME_strI2I_FTPUser          = "strI2I_FTPUser";
        public static final String COLUMN_NAME_strI2I_FTPPassword          = "strI2I_FTPPassword";
        public static final String COLUMN_NAME_strI2I_FTPPath          = "strI2I_FTPPath";
//bmp 19-May-25
        public static final String COLUMN_NAME_OfflinePatient1ID     = "OfflinePatient1ID";
        public static final String COLUMN_NAME_OfflinePatient1Used   = "OfflinePatient1Used";
        public static final String COLUMN_NAME_OfflinePatient2ID     = "OfflinePatient2ID";
        public static final String COLUMN_NAME_OfflinePatient2Used   = "OfflinePatient2Used";
        public static final String COLUMN_NAME_OfflinePatient3ID     = "OfflinePatient3ID";
        public static final String COLUMN_NAME_OfflinePatient3Used   = "OfflinePatient3Used";
        public static final String COLUMN_NAME_OfflinePatient4ID     = "OfflinePatient4ID";
        public static final String COLUMN_NAME_OfflinePatient4Used   = "OfflinePatient4Used";
        public static final String COLUMN_NAME_OfflinePatient5ID     = "OfflinePatient5ID";
        public static final String COLUMN_NAME_OfflinePatient5Used   = "OfflinePatient5Used";
//bmp 19-May-25

        public static final String SQL_CREATE_HW_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_HWMainVersion + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_HWSubVersion + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ECGUpdateIntervalMSec + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_StrHWDeviceID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_strI2I_PhysicianName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_strI2I_PhysicianPassword + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_strI2I_InstitutionGUID + TEXT_TYPE + COMMA_SEP +
//bmp 19-May-25
                        COLUMN_NAME_OfflinePatient1ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OfflinePatient1Used + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OfflinePatient2ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OfflinePatient2Used + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OfflinePatient3ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OfflinePatient3Used + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OfflinePatient4ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OfflinePatient4Used + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OfflinePatient5ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OfflinePatient5Used + INTEGER_TYPE + COMMA_SEP +
//bmp 19-May-25
                        COLUMN_NAME_strI2I_FTPUser + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_strI2I_FTPPassword + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_strI2I_FTPPath + TEXT_TYPE +
                ")";
    }

    public static abstract class PatientSamplesDB implements BaseColumns {
        public static final String TABLE_NAME                   = "PatientSamplesDB";

        public static final String COLUMN_NAME_PatientSampleTimestamp   = "PatientSampleTimestamp";
        public static final String COLUMN_NAME_PatientList              = "PatientList";
        public static final String COLUMN_NAME_PatientSampleList        = "PatientSampleList";
        public static final String COLUMN_NAME_PatientSampleStatus      = "PatientSampleStatus";
        public static final String COLUMN_NAME_PatientSampleReferred    = "PatientSampleReferred";
        public static final String COLUMN_NAME_PatientSampleData        = "PatientSampleData";
        public static final String COLUMN_NAME_PatientSampleComments    = "PatientSampleComments";
        public static final String COLUMN_NAME_PatientMeasurementData   = "PatientMeasurementData";
//        public static final String COLUMN_NAME_PatientSamplePDFReport   = "PatientSamplePDFReport";
        public static final String COLUMN_NAME_PatientSampleDrSignURL   = "PatientSampleDrSignURL";
        public static final String COLUMN_NAME_PatientSampleDrSignSizeBytes   = "PatientSampleDrSignSizeBytes";
        public static final String COLUMN_NAME_PatientSampleDrSign      = "PatientSampleDrSign";

        public static final String SQL_CREATE_PATIENT_SAMPLES_DB =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_PatientSampleTimestamp + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PatientList + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PatientSampleList + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PatientSampleStatus + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PatientSampleReferred + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PatientSampleComments + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PatientSampleData + BLOB_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PatientSamplePDFReport + BLOB_TYPE + COMMA_SEP +
                        COLUMN_NAME_PatientMeasurementData + BLOB_TYPE + COMMA_SEP +
                        COLUMN_NAME_PatientSampleDrSignURL + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PatientSampleDrSignSizeBytes + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PatientSampleDrSign + BLOB_TYPE +
                        ")";
    }
    public static abstract class EmergencySamplesDB implements BaseColumns {
        public static final String TABLE_NAME                   = "EmergencySamplesDB";

        public static final String COLUMN_NAME_EmergencySampleTimestamp = "EmergencySampleTimestamp";
        public static final String COLUMN_NAME_EmergencySampleFname     = "EmergencySampleFname";
        public static final String COLUMN_NAME_EmergencyDate            = "EmergencyDate";
        public static final String COLUMN_NAME_EmergencyTime            = "EmergencyTime";
        public static final String COLUMN_NAME_EmergencySampleList      = "EmergencySampleList";
        public static final String COLUMN_NAME_EmergencySampleID        = "EmergencySampleID";
        public static final String COLUMN_NAME_EmergencySampleReferred  = "EmergencySampleReferred";
        public static final String COLUMN_NAME_EmergencySamplePrinted   = "EmergencySamplePrinted";
        public static final String COLUMN_NAME_EmergencySampleComments  = "EmergencySampleComments";
        public static final String COLUMN_NAME_EmergencySampleData      = "EmergencySampleData";
        public static final String COLUMN_NAME_EmergencyMeasurementData = "EmergencyMeasurementData";
//        public static final String COLUMN_NAME_EmergencySamplePDFReport = "EmergencySamplePDFReport";

        public static final String SQL_CREATE_EMERGENCY_SAMPLES_DB =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_EmergencySampleTimestamp + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_EmergencySampleFname + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EmergencyDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EmergencyTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EmergencySampleList + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EmergencySampleID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_EmergencySampleReferred + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EmergencySamplePrinted + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EmergencySampleComments + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EmergencySampleData + BLOB_TYPE + COMMA_SEP +
//                        COLUMN_NAME_EmergencySamplePDFReport + BLOB_TYPE + COMMA_SEP +
                        COLUMN_NAME_EmergencyMeasurementData + BLOB_TYPE +
                        ")";
    }

    public static abstract class CountryMaster implements BaseColumns {
        public static final String TABLE_NAME                   = "CountryMaster";

        public static final String COLUMN_NAME_CountryID          = "CountryID";
        public static final String COLUMN_NAME_Country           = "Country";

        public static final String SQL_CREATE_COUNTRY_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_CountryID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Country + TEXT_TYPE +
                        ")";
    }
    public static abstract class StateMaster implements BaseColumns {
        public static final String TABLE_NAME                   = "StateMaster";

        public static final String COLUMN_NAME_StateID          = "StateID";
        public static final String COLUMN_NAME_State           = "State";
        public static final String COLUMN_NAME_StateInCountryID           = "StateInCountryID";

        public static final String SQL_CREATE_STATE_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_StateID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_State + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_StateInCountryID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class InsuranceProviderMaster implements BaseColumns {
        public static final String TABLE_NAME                   = "InsuranceProviderMaster";

        public static final String COLUMN_NAME_InsuranceProviderID          = "InsuranceProviderID";
        public static final String COLUMN_NAME_InsuranceProvider           = "InsuranceProvider";

        public static final String SQL_CREATE_INSURANCEPROVIDER_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_InsuranceProviderID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_InsuranceProvider + TEXT_TYPE +
                        ")";
    }
    public static abstract class HistoryConditionsMaster implements BaseColumns {
        public static final String TABLE_NAME                   = "HistoryConditionsMaster";

        public static final String COLUMN_NAME_HistoryConditionsID          = "HistoryConditionsID";
        public static final String COLUMN_NAME_HistoryConditions           = "HistoryConditions";
        public static final String COLUMN_NAME_HistoryConditionsCritical           = "HistoryConditionsCritical";

        public static final String SQL_CREATE_HISTORYCONDITIONS_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_HistoryConditionsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_HistoryConditions + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_HistoryConditionsCritical + TEXT_TYPE +
                        ")";
    }
}

