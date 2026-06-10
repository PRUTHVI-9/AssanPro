package com.example.AsaanPro_V1;

import java.util.ArrayList;

/**
 * Created by Bhalchandra on 18-09-2017.
 */

public class clsPatient {
    public String strPatientID;
    public String strPatient_Fname, strPatient_Mname, strPatient_Lname;
    public String strPatient_DOB, PatientAge, strPatient_Gender;
    public String strPatient_Mobile, strPatient_Mobile2, strPatient_Mailid;
    public String strPatient_Addr1, strPatient_Addr2, strPatient_City, strPatient_PinCode;
    public String strPatient_State, strPatient_CountryID;
    public String strPatientOccupation, strIDNumber, strIDType, strInsuranceProviderID;
    public String strModifiedBy, strCreatedBy;
    public boolean bIsInsured;

    public String Systolic, Diastolic, Weight, Height;
    public String BMI, HbA1c, RBS, TC, LDL, HDL, TG, Hb, TropT;
    public String ReferringDr;
    public String HistoryText, strHistoryConditions, strConditionIDs;

    public ArrayList<String> HistoryConditions = new ArrayList<String>();
    public ArrayList<Integer> HistoryConditionsID = new ArrayList<Integer>();

    public ArrayList<String> EventDate = new ArrayList<String>();
    public ArrayList<String> EventTime = new ArrayList<String>();
    public ArrayList<String> EventID = new ArrayList<String>();
    public ArrayList<String> EventType = new ArrayList<String>();
}
