package com.example.AsaanPro_V1;

import android.content.Context;

public class Measurement {

	public static final short   FDAMPL =  50 ;        //29
	public static final short   SDAMPL =  17  ;       //10
	public static final short MAX_POINTS_IN_MEDIAN = 430;
	public static final short Fix_Change = 3;
	public static final short MAX_HR_LIMIT = 250;
	public static final short SAMPLING_FREQUENCY = 250;
	public static final short DELTA = 35;
	public static final short SLOPE_FACTOR = 7;
	public static final short TOTAL_NO_OF_FIDUCIAL_POINTS = 13;
	public static final short TOTAL_AMPLITUDE_POINTS = 9;

	public static float ADC_VOLTAGE_RANGE = (float) 3.3;
	public static short AMPLIFIER_GAIN = 250;

	public static final short P_PLUS_START		=	0;
	public static final short P_PLUS_END		=	1;
	public static final short P_NEG_END			=	2;
	public static final short Q_START			=	3;
	public static final short R_START			=	4;
	public static final short R_END				=	5;
	public static final short S_END				=	6;
	public static final short R_DASH_END		=	7;
	public static final short S_DASH_END		=	8;
	public static final short T_START			=	9;
	public static final short T_END				=	10;
	public static final short U_START			=	11;
	public static final short U_END				=	12;

	public static final short P_PLUS_AMP		=	0;
	public static final short P_NEG_AMP			=	1;
	public static final short Q_AMP				=	2;
	public static final short R_AMP				=	3;
	public static final short S_AMP				=	4;
	public static final short R_DASH_AMP		=	5;
	public static final short S_DASH_AMP		=	6;
	public static final short T_AMP				=	7;
	public static final short U_AMP				=	8;
	public static final short QRS_AMP			=	9;

	public static final short P_AXIS	=	0;
	public static final short QRS_AXIS =	1;
	public static final short T_AXIS	=	2;
	
	public static final short MAX_BITCOUNT = 4095;
	public static final short TOTAL_NO_OF_AMPLITUDES = 10;
	

	public static short Last,Last_ButOne =0;
	public static short Current_FD = 0,Last_FD = 0,Last_ButOne_FD = 0;
	public static short Current_SD = 0,Last_SD = 0,Last_ButOne_SD = 0;
	public static short Current_SOD = 0,Last_SOD = 0,Last_ButOne_SOD = 0;

	public static short SFD = 0,Last_SFD = 0,Last_ButOne_SFD = 0,SSD = 0,SOD = 0,SSOD = 0;

	public static short qrs_time1 = 0,qrs_time2 = 0,qrs_time3 = 0,qrs_time4 = 0,sum_of_qrs_time = 0,RR_Interval;

	public static boolean qrs_time1_flag = true;
	public static boolean qrs_time2_flag = false,qrs_time3_flag = false,qrs_time4_flag = false;
	public static boolean calculate_HR = false, clear_HR = false,QRSDetected = false,QRS_FLAG;
	public static short QRS_Count=0;   
	public static short current_HR = 0,current_HR_copy=0;
	public static short prev_HR=0;
	public static short no_of_points_in_median;
	
	public static volatile short[][] median_buffer = new short[12][MAX_POINTS_IN_MEDIAN];
	public static volatile short[] median_buffer1 = new short[MAX_POINTS_IN_MEDIAN];

	public static int iResult;
	public short temp_median_index;
	
	public static float []Dispaly_Amplitude_Buffer = new float[TOTAL_AMPLITUDE_POINTS];
	public static short []nDuration = new short[8]; 
	public static short []nInterval=new short[4];
	public static short []nInterpretation_Status=new short[18];
	public static String strInterpretation_Status;
	public static int []nAxis=new int[3];
	public static short NoOfabnormalities=0;
	public static boolean bOtherwiseNormal=false;
	public static boolean bTWave_Inversion_flag=false;

	
	private boolean First_Median = false;
	private int strtptr;
	private short n_Age, nQRSDeadzone, nTargetHR,  nRRIndex;
	private int nRRInterval, nSumOfRRInterval;
	private boolean bCollectQRSSample, bSPointDetected, bSlopeChanged, bJPointDetected;
	private boolean bInitialSlopeCalculated, bFirstJPoint;
	private short nSPoint, nMedianIndex, nEPoint, nJPoint, nLastJPoint, nPointVariation=4;
	private short nPrevSlope, nSlope, nPostSCounter, nInitialSlope;
	private short nRPoint, nQPoint, nPStart, nPEnd, nPPoint, nRStart, nREnd;
	private short nSEnd, nTPoint, nTEnd, nTStart;
	public static short[][]nWavePositions = new short[12][TOTAL_NO_OF_FIDUCIAL_POINTS];
	public static short[]nJPointPosition = new short[12];
	private short[][]siAmplitude = new short[12][TOTAL_NO_OF_FIDUCIAL_POINTS];
	short nTriggerPoint = 49;
	private float[]siTAmplitude_inv = new float[12];
	private static short [][]siAmp = new short [12][TOTAL_NO_OF_AMPLITUDES];
	private short []TIndex = new short[12];  //vnp 31 oct 12
	private short []QStart = new short[12];	//vnp 31 oct 12
	private float[]siTAmplitude = new float[12];
	
	private short nQStart; 
	
	private short []siPPlusAmplitude = new short[12];
	private short []siPNegAmplitude = new short[12];
	private short []siQRSAmplitude = new short [12];
	private boolean MAX_BUFFER_SIZE_EXCEED_FLAG = false; //vnp 20 sep 14


	
	public void Calculate(Context context, clsEcgScan ecgScan) {
		int i,j=0;
		boolean return_value;
		short Sample=0; //vnp 19 april 17  To caculate HR on QRS lead, information of QRS lead is passed and HR is calculated accordingly. For this switch cases added
		
		iResult = 0;
//		for(i=0; i<MainActivity.SIZEOF_SAMPLE_BUFFER; i++) {
//			iResult = iResult + MainActivity.SampleData0[i];
//		}
		
	/*	for(i=0; i<MainActivity.SIZEOF_SAMPLE_BUFFER; i++) {
		//vnp 8 jan 14	iResult = iResult + MainActivity.ECGLeadData1[i];
			return_value=QRSDetect_for_HR_calculation(context,MainActivity.ECGLeadData6[i]);	
		}*/  //vnp 19 april 17

		nRRInterval=0;//vnp 26 april 17
		nSumOfRRInterval=0;//vnp 26 april 17
		nRRIndex=0; //vnp 26 april 17



		for(i=0; i<MainActivity.SIZEOF_SAMPLE_BUFFER; i++) {
			switch(MainActivity.lead_no)
			{
				case 0: Sample = ecgScan.ECGLeadData0[i];
					break;
				case 1: Sample = ecgScan.ECGLeadData1[i];
					break;
				case 2: Sample = ecgScan.ECGLeadData2[i];
					break;
				case 3: Sample = ecgScan.ECGLeadData3[i];
					break;
				case 4: Sample = ecgScan.ECGLeadData4[i];
					break;
				case 5: Sample = ecgScan.ECGLeadData5[i];
					break;
				case 6: Sample = ecgScan.ECGLeadData6[i];
					break;
				case 7: Sample = ecgScan.ECGLeadData7[i];
					break;
				case 8: Sample = ecgScan.ECGLeadData8[i];
					break;
				case 9: Sample = ecgScan.ECGLeadData9[i];
					break;
				case 10: Sample = ecgScan.ECGLeadData10[i];
					break;
				case 11: Sample = ecgScan.ECGLeadData11[i];
					break;


			}
			return_value=QRSDetect_for_HR_calculation(context,Sample);

		}

//		RR_Interval=(short) (nSumOfRRInterval/(nRRIndex-1));
		if(nRRIndex > 1) {
			RR_Interval=(short) (nSumOfRRInterval/(nRRIndex-1));
		}
		nRRInterval=0;//vnp 26 april 17
		nSumOfRRInterval=0;//vnp 26 april 17
		nRRIndex=0; //vnp 26 april 17

		if (current_HR==0)
			no_of_points_in_median=200;
		else
			no_of_points_in_median=(short) (250*60/current_HR);
		
		current_HR_copy=current_HR;
		if(no_of_points_in_median>MAX_POINTS_IN_MEDIAN)
			no_of_points_in_median=MAX_POINTS_IN_MEDIAN;
	j=no_of_points_in_median;
	
			nTargetHR = (short) (MAX_HR_LIMIT - n_Age);
			nQRSDeadzone = (short)((60*SAMPLING_FREQUENCY)/nTargetHR);

//		    Toast.makeText(context, "Test HR Result = " + Integer.toString(current_HR),
//					Toast.LENGTH_LONG).show();

			
//	    Toast.makeText(context, "Test HR Result = " + Integer.toString(no_of_points_in_median),
//				Toast.LENGTH_LONG).show();
//	    Toast.makeText(context, "QRS_Dead_Zone = " + Integer.toString(nQRSDeadzone),
//				Toast.LENGTH_LONG).show();
	}
	
	 public boolean QRSDetect_for_HR_calculation(Context context, short Current){
			
		boolean Mark_Sample= false;
		
		// below routine calculates ABSOLUTE current first derivative  
		
		Last_ButOne_FD = Last_FD;
		Last_FD = Current_FD;
		
		Current_FD = (short) Math.abs(Current - Last_ButOne);
		
		Last_ButOne = Last;
		Last = Current;
		
		Last_ButOne_SFD = Last_SFD;
		Last_SFD = SFD;
		
		SFD =(short) (((Current_FD/4)+(Last_FD/2)+(Last_ButOne_FD/4)) * FDAMPL) ;
		if(SFD > 0x0fff)
			SFD = 0x0fff;
		
		
		
		
		//---------  first derivative calculation over ---------
		
		//--------  second derivative calculation starts -------
		
		Last_ButOne_SD = Last_SD;
		Last_SD = Current_SD;
		
		Current_SD = (short) Math.abs(Current_FD - Last_ButOne_FD);
		
		SSD = (short) (((Current_SD/4)+(Last_SD/2)+(Last_ButOne_SD/4)) * SDAMPL);
		if( SSD > 0x0fff)
			SSD = 0x0fff;			//IF SSD > 4095 THEN SSD=4095
		
		Last_ButOne_SOD = Last_SOD;
		Last_SOD = SOD ;
		
		SOD = (short) (SFD  + SSD);
		
		SSOD = (short) ((SOD/4)+(Last_SOD/2)+(Last_ButOne_SOD/4));
		if( SSOD > 0x0fff)
			SSOD = 0x0fff;			//IF SSD > 4095 THEN SSD=4095
		 nRRInterval++;   //vnp 20 april 17
		
		if(QRSDetected == true)
		{
			if(SSOD <= 0x04c7)	// check for 30 % of full scale to clear QRS bit,for 8 bit 0x004c, for 12 bit 0x04d0
			{
				QRSDetected = false;
				QRS_FLAG = true;
				//QRS_FLAG1 = 0x01;
				//Beep_Time=0;
				wave_time();// check beep for each QRS detected & RR interval to avoid false QRS dtection
				//QRS_Count++;
			}
		}
		else if(QRSDetected == false)
		{
			if ((SSOD >= 0x0d9a))	// check for 85 % of full scale to set QRS bit,// for 8 bit 0x00d8, for 12 bit 0x0da0 
			{

				if(nRRIndex>=1) {
					nSumOfRRInterval = nSumOfRRInterval + nRRInterval;
				}
				nRRIndex++;
				nRRInterval =0;

				QRSDetected = true;
				QRS_Count++;
				Mark_Sample = true;
			}
		}
		
		//-------- new logic--		
		if(qrs_time1_flag==true)
		{
			qrs_time1++;
			if(qrs_time1 > 375)     //0x170) 
			{
				qrs_time1 = 1;
				clear_HR = true;
				calculate_HR = false;
			}			
		}
		if(qrs_time2_flag == true)
		{
			qrs_time2++;
			if(qrs_time2 > 375)    //0x170)
			{
				qrs_time2 = 1;
				clear_HR = true;
				calculate_HR = false;
			}		
		}
		if(qrs_time3_flag == true)
		{
			qrs_time3++;
			if(qrs_time3 > 375)    //0x170)
			{
				qrs_time3 = 1;
				clear_HR = true;
				calculate_HR = false;
			}
		}
		if(qrs_time4_flag == true)
		{
			qrs_time4++;
			if(qrs_time4 > 375)    //0x170)
			{
				qrs_time4 = 1;
				clear_HR = true;
				calculate_HR = false;
			}
		}				
		if(clear_HR == true)
		{
			clear_HR = false;
			current_HR = 0;	 
		}
		if(calculate_HR == true)
		{
			calculate_HR = false;
			
			sum_of_qrs_time = qrs_time2;
			sum_of_qrs_time = (short) (sum_of_qrs_time + qrs_time3);
			sum_of_qrs_time = (short) (sum_of_qrs_time + qrs_time4);
			qrs_time1 = 1;
			qrs_time2 = 0;
			qrs_time3 = 0; 
			qrs_time4 = 0;
			
			sum_of_qrs_time = (short) (sum_of_qrs_time /3) ;
			RR_Interval= sum_of_qrs_time;		
			
			sum_of_qrs_time = (short) (0x3a98 / sum_of_qrs_time);     //0x2274 / sum_of_qrs_time;
			
			prev_HR = current_HR;
			current_HR = sum_of_qrs_time;
			
			sum_of_qrs_time = 0;
		
		}
		return Mark_Sample;

	}

	 /*------------------------------------------------------------------------------------------
	 Fuction Name :	wave_time
	 Input		 : 	-
	 Ouput		 : 	-
	 Description	 :	Checks if false QRS is detected.  
	 -------------------------------------------------------------------------------------------*/
	 

	public void wave_time()
	 {
	 	if(qrs_time4_flag == true)
	 	{
	 		if(qrs_time4>68)
	 		{
	 			qrs_time4_flag = false;
	 			qrs_time1_flag = true;
	 			calculate_HR = true;
	 		}
	 		else
	 			QRS_FLAG=false;
	 	}
	 	else if(qrs_time3_flag == true)
	 	{
	 		if(qrs_time3>68)
	 		{
	 			qrs_time3_flag = false;
	 			qrs_time4_flag = true;
	 		}
	 		else
	 			QRS_FLAG=false;
	 	}
	 	else if(qrs_time2_flag == true)
	 	{
	 		if(qrs_time2>68)
	 		{
	 			qrs_time2_flag=false;
	 			qrs_time3_flag=true;
	 		}
	 		else
	 			QRS_FLAG=false;
	 	}
	 	else if(qrs_time1_flag == true)
	 	{
	 		if(qrs_time1>68)
	 		{
	 			qrs_time1_flag= false;
	 			qrs_time2_flag=true;
	 		}
	 		else
	 			QRS_FLAG=false;
	 	}
	 }
	
	void ClearQRSVariables()
	{
		 qrs_time1 = 0;
		 qrs_time2 = 0;
		 qrs_time3 = 0; 
		 qrs_time4 = 0;
		 qrs_time1_flag = true;
		 qrs_time2_flag = false;
		 qrs_time3_flag = false;
		 qrs_time4_flag = false;
		// dummy_flag = 0;
		 current_HR= 0;
		 prev_HR = 0;
		 calculate_HR =false;
		 QRS_Count= 0;
		 nMedianIndex =0;
		 QRSDetected = false;
	}
	
	public void Calculate_Median(Context context, clsEcgScan ecgScan) {
		
		int i;
		short Sample = 0, sCurrentIndex, temp_Index;
		boolean Mark_Sample,QRS_Found;
		First_Median =true;
		QRS_Found = false;
		MAX_BUFFER_SIZE_EXCEED_FLAG = false; //vnp 20 sep 14
		ClearQRSVariables();
		QRS_Count=0;
//		if(MainActivity.DemoMode) {
//			sCurrentIndex = 0;	//This will avoid discontinuity in demo waveform
//		} else {
			sCurrentIndex = MainActivity.mWriteLeadDataIndex;
//		}
		
		temp_Index = sCurrentIndex;
		nRRInterval=0;//vnp 26 april 17
		nSumOfRRInterval=0;//vnp 26 april 17
		nRRIndex=0; //vnp 26 april 17

		
		for(i=0; i<MainActivity.SIZEOF_SAMPLE_BUFFER; i++) {
	//		for(i=0; i<(15*no_of_points_in_median); i++) {
			if( temp_Index > MainActivity.MAX_RECORD_BUFFER_INDEX )
			{	
				temp_Index = 0;
				MAX_BUFFER_SIZE_EXCEED_FLAG = true;
			}
		
		//	Sample= MainActivity.ECGLeadData0[i];
			switch(MainActivity.lead_no)
			{
				case 0 : Sample = ecgScan.ECGLeadData0[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData8[temp_Index];
				break;
				case 1 : Sample = ecgScan.ECGLeadData1[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData0[temp_Index];
				break;
				case 2 : Sample = ecgScan.ECGLeadData2[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData1[temp_Index];
				break;
				case 3 : Sample = ecgScan.ECGLeadData3[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData9[temp_Index];
				break;
				case 4 : Sample = ecgScan.ECGLeadData4[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData10[temp_Index];
				break;
				case 5 : Sample = ecgScan.ECGLeadData5[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData11[temp_Index];
				break;
				case 6 : Sample = ecgScan.ECGLeadData6[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData2[temp_Index];
				break;
				case 7 : Sample = ecgScan.ECGLeadData7[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData3[temp_Index];
				break;
				case 8 : Sample = ecgScan.ECGLeadData8[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData4[temp_Index];
				break;
				case 9 : Sample = ecgScan.ECGLeadData9[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData5[temp_Index];
				break;
				case 10 : Sample = ecgScan.ECGLeadData10[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData6[temp_Index];
				break;
				case 11 : Sample = ecgScan.ECGLeadData11[temp_Index]; //vnp 4 april 15 Sample = MainActivity.ECGLeadData7[temp_Index];
				break;
						
				
			}
			temp_Index++;
			Mark_Sample= QRSDetect_for_HR_calculation(context, Sample);
			
			if(Mark_Sample==true)
			{
				if(i>=50)//vnp 25 march 14 60)
				{
					if(temp_Index>=50) //vnp 20 sep 14
					{
						//vnp 20 sep 14 strtptr= (i-50);
						strtptr=temp_Index-50;  //vnp 20 sep 14
					}
					else  //vnp 20 sep 14
					{
						strtptr= MainActivity.MAX_RECORD_BUFFER_INDEX -(50-temp_Index);  //vnp 20 sep 14
						MAX_BUFFER_SIZE_EXCEED_FLAG = true; //vnp 20 sep 14
					}	
					iResult = strtptr;
					FormMedianBuf(MainActivity.lead_no, ecgScan);
					QRS_Found = true;
				}
			}
		}

//		RR_Interval=(short) (nSumOfRRInterval/(nRRIndex-1));
		if(nRRIndex > 1) {
			RR_Interval=(short) (nSumOfRRInterval/(nRRIndex-1));
		}
		nRRInterval=0;//vnp 26 april 17
		nSumOfRRInterval=0;//vnp 26 april 17
		nRRIndex=0; //vnp 26 april 17



		if(QRS_Found==false)
		{
			for(i=0;i<no_of_points_in_median;i++)
			{
				median_buffer[MainActivity.lead_no][i] = 0x0800;
			}
		}
		
	}
	
	void FormMedianBuf(short lead_no, clsEcgScan ecgScan)
	{
		
		if(First_Median)
		{
			 CopyInMedianBuf(lead_no, ecgScan);
			 First_Median = false; 

		}
		else
		{
			UpdateMedianBuf(lead_no, ecgScan);
		}
	}
	
//	void CopyInMedianBuf_old(short lead_no)
//	{
//		int j;
//
//		for(j=0;j<no_of_points_in_median;j++)
//		{
//		//	median_buffer[1][j]=MainActivity.ECGLeadData0[strtptr];
//		//	median_buffer1[j]=MainActivity.ECGLeadData0[strtptr];
//
//			switch(lead_no)
//			{
//				case 0: median_buffer[0][j]=MainActivity.ECGLeadData0[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData8[strtptr];
//						break;
//				case 1: median_buffer[1][j]=MainActivity.ECGLeadData1[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData0[strtptr];
//						break;
//				case 2: median_buffer[2][j]=MainActivity.ECGLeadData2[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData1[strtptr];
//						break;
//				case 3: median_buffer[3][j]=MainActivity.ECGLeadData3[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData9[strtptr];
//						break;
//				case 4: median_buffer[4][j]=MainActivity.ECGLeadData4[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData10[strtptr];
//						break;
//				case 5: median_buffer[5][j]=MainActivity.ECGLeadData5[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData11[strtptr];
//						break;
//				case 6: median_buffer[6][j]=MainActivity.ECGLeadData6[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData2[strtptr];
//						break;
//				case 7: median_buffer[7][j]=MainActivity.ECGLeadData7[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData3[strtptr];
//						break;
//				case 8: median_buffer[8][j]=MainActivity.ECGLeadData8[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData4[strtptr];
//						break;
//				case 9: median_buffer[9][j]=MainActivity.ECGLeadData9[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData5[strtptr];
//						break;
//				case 10: median_buffer[10][j]=MainActivity.ECGLeadData10[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData6[strtptr];
//						break;
//				case 11: median_buffer[11][j]=MainActivity.ECGLeadData11[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData7[strtptr];
//						break;
//
//			}
//			strtptr++;
//			if(strtptr >= 2500)
//			{
//				strtptr = 0;
//				MAX_BUFFER_SIZE_EXCEED_FLAG = true;
//			}
//		}
//
//	}
	void CopyInMedianBuf(short lead_no, clsEcgScan ecgScan)
	{
		int j;

		for(j=0;j<no_of_points_in_median;j++)
		{
			//	median_buffer[1][j]=MainActivity.ECGLeadData0[strtptr];
			//	median_buffer1[j]=MainActivity.ECGLeadData0[strtptr];

			//		switch(lead_no)
			//		{
			//	case 0:
			median_buffer[0][j]=ecgScan.ECGLeadData0[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData8[strtptr];
			//		break;
			//	case 1:
			median_buffer[1][j]=ecgScan.ECGLeadData1[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData0[strtptr];
			//		break;
			//case 2:
			median_buffer[2][j]=ecgScan.ECGLeadData2[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData1[strtptr];
			//		break;
			//case 3:
			median_buffer[3][j]=ecgScan.ECGLeadData3[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData9[strtptr];
			//		break;
			//case 4:
			median_buffer[4][j]=ecgScan.ECGLeadData4[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData10[strtptr];
			//		break;
			//case 5:
			median_buffer[5][j]=ecgScan.ECGLeadData5[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData11[strtptr];
			//		break;
			//case 6:
			median_buffer[6][j]=ecgScan.ECGLeadData6[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData2[strtptr];
			//		break;
			//case 7:
			median_buffer[7][j]=ecgScan.ECGLeadData7[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData3[strtptr];
			//		break;
			//case 8:
			median_buffer[8][j]=ecgScan.ECGLeadData8[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData4[strtptr];
			//		break;
			//case 9:
			median_buffer[9][j]=ecgScan.ECGLeadData9[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData5[strtptr];
			//		break;
			//case 10:
			median_buffer[10][j]=ecgScan.ECGLeadData10[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData6[strtptr];
			//		break;
			//case 11:
			median_buffer[11][j]=ecgScan.ECGLeadData11[strtptr];//vnp 4 april 15 MainActivity.ECGLeadData7[strtptr];
			//		break;

			//}
			strtptr++;
			if(strtptr >= 2500)
			{
				strtptr = 0;
				MAX_BUFFER_SIZE_EXCEED_FLAG = true;
			}
		}

	}

//	void UpdateMedianBuf_old(short lead_no)
//	{
//		short Difference=0,Sample=0, Sample_median;
//		int j=0;
//
//	//	for(j=0;j<no_of_points_in_median;j++)
//	//	{
//	//		Sample_median = median_buffer[lead_no][j];
//	//		Sample_median = median_buffer[1][j];
//	//		Sample = MainActivity.ECGLeadData1[strtptr];
//
//		while(j<no_of_points_in_median)
//		{
//
//			switch(MainActivity.lead_no)
//			{
//			case 0: Sample = MainActivity.ECGLeadData0[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData8[strtptr];
//					break;
//
//			case 1: Sample = MainActivity.ECGLeadData1[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData0[strtptr];
//			break;
//
//			case 2: Sample = MainActivity.ECGLeadData2[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData1[strtptr];
//			break;
//
//			case 3: Sample = MainActivity.ECGLeadData3[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData9[strtptr];
//			break;
//
//			case 4: Sample = MainActivity.ECGLeadData4[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData10[strtptr];
//			break;
//
//			case 5: Sample = MainActivity.ECGLeadData5[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData11[strtptr];
//			break;
//
//			case 6: Sample = MainActivity.ECGLeadData6[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData2[strtptr];
//			break;
//
//			case 7: Sample = MainActivity.ECGLeadData7[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData3[strtptr];
//			break;
//
//			case 8: Sample = MainActivity.ECGLeadData8[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData4[strtptr];
//			break;
//
//			case 9: Sample = MainActivity.ECGLeadData9[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData5[strtptr];
//			break;
//
//			case 10: Sample = MainActivity.ECGLeadData10[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData6[strtptr];
//			break;
//
//			case 11: Sample = MainActivity.ECGLeadData11[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData7[strtptr];
//			break;
//
//
//			}
//
//			Difference =(short) ((float) (median_buffer[MainActivity.lead_no][j] - Sample)/(float)16.0);
//
//			if(Math.abs(Difference) <Fix_Change )
//				median_buffer[MainActivity.lead_no][j] = (short) (median_buffer[MainActivity.lead_no][j] - Difference) ;
//			else
//			{
//				if(Difference>0)
//					median_buffer[MainActivity.lead_no][j] = (short) (median_buffer[MainActivity.lead_no][j] - Fix_Change);
//				else
//					median_buffer[MainActivity.lead_no][j] = (short) (median_buffer[MainActivity.lead_no][j] + Fix_Change);
//			}
//
///*			Difference =(short) ((float) (median_buffer1[j] - MainActivity.ECGLeadData0[strtptr])/(float)16.0);
//
//			if(Math.abs(Difference) <Fix_Change )
//				median_buffer1[j] = (short) (median_buffer1[j] - Difference) ;
//			else
//			{
//				if(Difference>0)
//					median_buffer1[j] = (short) (median_buffer1[j] - Fix_Change);
//				else
//					median_buffer1[j] = (short) (median_buffer1[j] + Fix_Change);
//			}
//
//	*/
//
//
//	//		if(MainActivity.ECGLeadData1[strtptr] > median_buffer[lead_no][j])
//	//		{
//		//	   Difference = (short) ((Sample - Sample_median)/(16))	;
//
///*			   if(Difference > Fix_Change)
//			   {
//				   median_buffer[lead_no][j] = (short) (median_buffer[lead_no][j] + Fix_Change);
//			   }*/
//		//	   if(Difference > Fix_Change)
//		//	   {
//		//		   median_buffer[1][j] = (short) (median_buffer[1][j] + Fix_Change);
//		//	   }
//
//    	//	}
//		//	else
//		//	{
//		//		Difference = (short) ( (median_buffer[lead_no][j] - Sample)/16);
//
///*				if(Difference >= Fix_Change)
//				{
//					   median_buffer[lead_no][j] = (short) (median_buffer[lead_no][j] - Fix_Change);
//
//				}*/
//
//	//			if(Difference >= Fix_Change)
//	//			{
//		/*	  		   median_buffer[1][j] = (short) (median_buffer[1][j] - Fix_Change);
//			*/
//		//		}*/
//
//
//		//	}
//				j++;
//			strtptr++;
//			if(strtptr >= 2500)//vnp 20 sep 14
//			{
//				strtptr = 0;    //vnp 20 sep 14
//				MAX_BUFFER_SIZE_EXCEED_FLAG = true;
//			}
//			if(MAX_BUFFER_SIZE_EXCEED_FLAG == true && strtptr == MainActivity.mWriteLeadDataIndex) //vnp 20 sep 14
//				break;
//	//vnp 20 sep 14		if(strtptr >= 2500)
//	//vnp 20 sep 14			j=no_of_points_in_median + 1;
//
//		}
//
//	}
	void UpdateMedianBuf(short lead_no, clsEcgScan ecgScan)
	{
		short Difference=0,Sample=0, Sample_median,i=0;
		int j=0;

		//	for(j=0;j<no_of_points_in_median;j++)
		//	{
		//		Sample_median = median_buffer[lead_no][j];
		//		Sample_median = median_buffer[1][j];
		//		Sample = MainActivity.ECGLeadData1[strtptr];

		while(j<no_of_points_in_median)
		{

			for(i=0;i<12;i++) {
				switch (i) {
					case 0:
						Sample = ecgScan.ECGLeadData0[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData8[strtptr];
						break;

					case 1:
						Sample = ecgScan.ECGLeadData1[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData0[strtptr];
						break;

					case 2:
						Sample = ecgScan.ECGLeadData2[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData1[strtptr];
						break;

					case 3:
						Sample = ecgScan.ECGLeadData3[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData9[strtptr];
						break;

					case 4:
						Sample = ecgScan.ECGLeadData4[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData10[strtptr];
						break;

					case 5:
						Sample = ecgScan.ECGLeadData5[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData11[strtptr];
						break;

					case 6:
						Sample = ecgScan.ECGLeadData6[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData2[strtptr];
						break;

					case 7:
						Sample = ecgScan.ECGLeadData7[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData3[strtptr];
						break;

					case 8:
						Sample = ecgScan.ECGLeadData8[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData4[strtptr];
						break;

					case 9:
						Sample = ecgScan.ECGLeadData9[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData5[strtptr];
						break;

					case 10:
						Sample = ecgScan.ECGLeadData10[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData6[strtptr];
						break;

					case 11:
						Sample = ecgScan.ECGLeadData11[strtptr];//vnp 4 april 15  Sample = MainActivity.ECGLeadData7[strtptr];
						break;


				}

				Difference = (short) ((float) (median_buffer[i][j] - Sample) / (float) 16.0);

				if (Math.abs(Difference) < Fix_Change)
					median_buffer[i][j] = (short) (median_buffer[i][j] - Difference);
				else {
					if (Difference > 0)
						median_buffer[i][j] = (short) (median_buffer[i][j] - Fix_Change);
					else
						median_buffer[i][j] = (short) (median_buffer[i][j] + Fix_Change);
				}
			}
/*			Difference =(short) ((float) (median_buffer1[j] - MainActivity.ECGLeadData0[strtptr])/(float)16.0);

			if(Math.abs(Difference) <Fix_Change )
				median_buffer1[j] = (short) (median_buffer1[j] - Difference) ;
			else
			{
				if(Difference>0)
					median_buffer1[j] = (short) (median_buffer1[j] - Fix_Change);
				else
					median_buffer1[j] = (short) (median_buffer1[j] + Fix_Change);
			}

	*/


			//		if(MainActivity.ECGLeadData1[strtptr] > median_buffer[lead_no][j])
			//		{
			//	   Difference = (short) ((Sample - Sample_median)/(16))	;

/*			   if(Difference > Fix_Change)
			   {
				   median_buffer[lead_no][j] = (short) (median_buffer[lead_no][j] + Fix_Change);
			   }*/
			//	   if(Difference > Fix_Change)
			//	   {
			//		   median_buffer[1][j] = (short) (median_buffer[1][j] + Fix_Change);
			//	   }

			//	}
			//	else
			//	{
			//		Difference = (short) ( (median_buffer[lead_no][j] - Sample)/16);

/*				if(Difference >= Fix_Change)
				{
					   median_buffer[lead_no][j] = (short) (median_buffer[lead_no][j] - Fix_Change);

				}*/

			//			if(Difference >= Fix_Change)
			//			{
		/*	  		   median_buffer[1][j] = (short) (median_buffer[1][j] - Fix_Change);
			*/
			//		}*/


			//	}
			j++;
			strtptr++;
			if(strtptr >= 2500)//vnp 20 sep 14
			{
				strtptr = 0;    //vnp 20 sep 14
				MAX_BUFFER_SIZE_EXCEED_FLAG = true;
			}
			if(MAX_BUFFER_SIZE_EXCEED_FLAG == true && strtptr == MainActivity.mWriteLeadDataIndex) //vnp 20 sep 14
				break;
			//vnp 20 sep 14		if(strtptr >= 2500)
			//vnp 20 sep 14			j=no_of_points_in_median + 1;

		}

	}

	public void QRSDetect (Context context, short Current, short nChannel_No, short i_Count)
	{
		
		// below routine calculates ABSOLUTE current first derivative  
		
		Last_ButOne_FD = Last_FD;
		Last_FD = Current_FD;
		
		Current_FD = (short) Math.abs(Current - Last_ButOne);
		
		Last_ButOne = Last;
		Last = Current;
		
		Last_ButOne_SFD = Last_SFD;
		Last_SFD = SFD;
		
		SFD =(short) (((Current_FD/4)+(Last_FD/2)+(Last_ButOne_FD/4)) * FDAMPL) ;
		if(SFD > 0x0fff)
			SFD = 0x0fff;
		
		
		
		
		//---------  first derivative calculation over ---------
		
		//--------  second derivative calculation starts -------
		
		Last_ButOne_SD = Last_SD;
		Last_SD = Current_SD;
		
		Current_SD = (short) Math.abs(Current_FD - Last_ButOne_FD);
		
		SSD = (short) (((Current_SD/4)+(Last_SD/2)+(Last_ButOne_SD/4)) * SDAMPL);
		if( SSD > 0x0fff)
			SSD = 0x0fff;			//IF SSD > 4095 THEN SSD=4095
		
		Last_ButOne_SOD = Last_SOD;
		Last_SOD = SOD ;
		
		SOD = (short) (SFD  + SSD);
		
		SSOD = (short) ((SOD/4)+(Last_SOD/2)+(Last_ButOne_SOD/4));
		if( SSOD > 0x0fff)
			SSOD = 0x0fff;			//IF SSD > 4095 THEN SSD=4095
	
		//vnp 20 april 17 nRRInterval++;
		
		   if(QRSDetected == true)
		   {
			   if(SSOD <= 0x04c7)	// check for 30 % of full scale to clear QRS bit,for 8 bit 0x004c, for 12 bit 0x04d0
			     {
				  QRSDetected = false;
				  QRS_FLAG = true;

			     }
		   }

		else if(QRSDetected == false)
		   {
		     if ((SSOD >= 0x0d9a))//vnp 28 march 14 && (nRRInterval>=nQRSDeadzone))	// check for 85 % of full scale to set QRS bit,// for 8 bit 0x00d8, for 12 bit 0x0da0 
		     {
			  	QRSDetected = true;
		      	QRS_Count++;
		  
				//Auto points detection---------------------------------------
				bCollectQRSSample			= true;

				nRRInterval=0;
				// E Point detection
				bSPointDetected				= false;
				nSPoint						= 0;
				bSlopeChanged				= false ;
				nMedianIndex				= 0;
		   	    bJPointDetected				= false;
		    //    bPostJDetected				= 0;
//				nPostJCounter				= 0;
		   	    
	//			Toast.makeText(context, "MedianIndex_in_QRS_Detect = " + Integer.toString(nMedianIndex),
	//					Toast.LENGTH_LONG).show();


				short nCurValue ,nLastValue ,nNextValue,QPoint ;
				boolean bEPointDetected = false ;
				boolean bQPointDetected = false ;
		//	    WriteIndex = 25000+((no_of_points_in_median*2)*ui_lead_no); //vnp 7 May 2012

				short nESearchIndex = 45;// WriteIndex+45+45;
//				while ( (nESearchIndex > (35+35+25000+((no_of_points_in_median*2)*ui_lead_no))) && (bEPointDetected==false))
				while ( (nESearchIndex > 35) && (bEPointDetected==false))
				{
					nCurValue = median_buffer[nChannel_No][nESearchIndex]; //ReadSample(nESearchIndex);
					nLastValue = median_buffer[nChannel_No][nESearchIndex-1]; //ReadSample(nESearchIndex-2);
					nNextValue = median_buffer[nChannel_No][nESearchIndex+1]; //ReadSample(nESearchIndex+2);

					if ((nCurValue <= nLastValue) && (nCurValue <= nNextValue))
					{
						bQPointDetected = true ;
						QPoint = nESearchIndex;
						nESearchIndex -= 2; 
					}
					else
					if ((bQPointDetected==true) && (Math.abs(nCurValue - nLastValue) < 10))
					{
						bEPointDetected = true ;
						
					/*	if(bAutoPointDetection)*/
							nEPoint = (short) (nESearchIndex - 2) ;
					 }
					 nESearchIndex = (short) (nESearchIndex-1);
				}
				//Added on 27-03-2002
				if(!bEPointDetected)
				{
					nEPoint = 38;//vnp 30 jan 14 25000+(2*38)+(ui_lead_no*no_of_points_in_median*2);//vnp 24 may 12 38;
				}
				/////////////////////
				nMedianIndex				= i_Count; //vnp 25 march 14 50;// 
//				Toast.makeText(context, "Eoint_Detected = " + Integer.toString(nEPoint), //vnp 22 march 14
//						Toast.LENGTH_LONG).show();
	//						Toast.makeText(context, "median_index = " + Integer.toString(nMedianIndex),  //vnp 22 march 14
	//						Toast.LENGTH_LONG).show();

	//						Toast.makeText(context, "i_count = " + Integer.toString(i_Count),  //vnp 22 march 14
	//						Toast.LENGTH_LONG).show();


			  }
			}

			//------S Point detection---------------------------------------------------------
			if(bCollectQRSSample==true)
			{

				short previous_but_one_sample, previous_sample, current_sample;

				//if ((nMedianIndex >= 53) && (!bSPointDetected) ) 
				if ((nMedianIndex > 61) && (bSPointDetected==false) ) 
				{
		//vnp 30 jan    14		  	WriteIndex = 25000+(nMedianIndex*2)+((no_of_points_in_median*2)*ui_lead_no); //vnp 7 May 2012
					previous_but_one_sample=median_buffer[nChannel_No][nMedianIndex-2]; //vnp 30 jan 14 ReadSample(WriteIndex-4);
					previous_sample=median_buffer[nChannel_No][nMedianIndex-1]; //vnp 30 jan 14ReadSample(WriteIndex-2);
					current_sample=median_buffer[nChannel_No][nMedianIndex]; //vnp 30 jan 14ReadSample(WriteIndex);
					nPrevSlope =(short) (previous_sample - previous_but_one_sample); // siMedianBuffer[nChannelNo][nMedianIndex-1] - siMedianBuffer[nChannelNo][nMedianIndex-2];
					nSlope = (short) (current_sample - previous_sample);// siMedianBuffer[nChannelNo][nMedianIndex] - siMedianBuffer[nChannelNo][nMedianIndex-1];

					if ( (( nPrevSlope < 0) && (nSlope > 0)) || ((nPrevSlope > 0) && (nSlope < 0 )) )  
						bSlopeChanged = true;
					else
						bSlopeChanged = false ;

					if ((bSlopeChanged) || (Math.abs(nSlope) <= DELTA))
					{
					//	if(bAutoPointDetection)
							nSPoint = nMedianIndex; //vnp 30 jan 14 25000+(nMedianIndex*2)+((no_of_points_in_median*2)*ui_lead_no); // + 1 ;
						bSPointDetected = true ;
						nPostSCounter = 0;
					}
				}
				//J point detection----------------------------------------------------------
				if(bSPointDetected)
				{

					if(!bJPointDetected)
					{
						if(bSPointDetected)
							nPostSCounter++;

				  		//vnp 30 jan 14 	WriteIndex = 25000+(nMedianIndex*2)+((no_of_points_in_median*2)*ui_lead_no); //vnp 7 May 2012
							previous_sample=median_buffer[nChannel_No][nMedianIndex-1]; //vnp 30 jan 14 ReadSample(WriteIndex-2);
							current_sample=median_buffer[nChannel_No][nMedianIndex]; //vnp 30 jan 14ReadSample(WriteIndex);

						//if(nPostSCounter >= 2) 
						if(nPostSCounter == 2) // Modified on 27-03-2002
						{

							nInitialSlope = (short) Math.abs(current_sample - previous_sample);//vnp 31 jan 14 abs(siMedianBuffer[nChannelNo][nMedianIndex] - siMedianBuffer[nChannelNo][nMedianIndex-1]);
							bInitialSlopeCalculated = true;
					//		Toast.makeText(context, "Postcounter_Initial_Slope = " + Integer.toString(nPostSCounter), //vnp 22 march 14
					//				Toast.LENGTH_LONG).show();

						}
						if(bInitialSlopeCalculated)
						{
							int nJSlope = Math.abs(current_sample - previous_sample);//vnp 31 jan 14 abs(siMedianBuffer[nChannelNo][nMedianIndex] - siMedianBuffer[nChannelNo][nMedianIndex-1]);
							if((float)nJSlope <= (((float)nInitialSlope * (float)SLOPE_FACTOR) / (float)10.0) )
							{
								//nLastJPoint = nJPoint ;
							//	if(bAutoPointDetection)
							//	{
									//Modified on 23-02-2002
									nJPoint = nMedianIndex;
									if(((nJPoint - nLastJPoint) > nPointVariation) && bFirstJPoint)
										nJPoint = nLastJPoint;
									else
									{
										nJPoint = nMedianIndex;
										nLastJPoint = nJPoint;
									}
									bFirstJPoint = true;

									nJPointPosition[nChannel_No] = nJPoint;
				//vnp 31 jan 14 This is for point marking logic in firmware					Wave_Marker[ui_lead_no][2]=25000+(ui_lead_no*( no_of_points_in_median*2))+(nMedianIndex*2);

									/*CDC *pDC = AfxGetApp()->m_pMainWnd->GetDC();
									CString str;
									str.Format("J=%d, JSlope=%d", nJPoint, nSlope);
									pDC->TextOut(420, 200, str);
									AfxGetApp()->m_pMainWnd->ReleaseDC(pDC);
									*/
									//////////////////////////
									//nJPoint = nMedianIndex;
							//	}
						//			Toast.makeText(context, "JPoint = " + Integer.toString(nJPoint), //vnp 22 march 14
						//					Toast.LENGTH_LONG).show();

								nPostSCounter			= 0 ;
								bJPointDetected         = true ;
							//vnp 9 oct 12	bPostJDetected          = 0 ;
								bInitialSlopeCalculated = false;
							}
						}
					}
				}
	//			Toast.makeText(context, "median_index = " + Integer.toString(nMedianIndex),  //vnp 22 march 14
	//			Toast.LENGTH_LONG).show();

	//			Toast.makeText(context, "i_count = " + Integer.toString(i_Count),  //vnp 22 march 14
	//			Toast.LENGTH_LONG).show();
				
		        nMedianIndex++;
		        temp_median_index=i_Count;
				if(nMedianIndex >= (no_of_points_in_median))   //vnp 5 nov 12
		   //vnp 22 march 14     if(bJPointDetected)
				{
		//vnp 31 jan 14				unsigned int Start_add=0;
					//vnp 31 jan 14					Start_add = 25000 + (no_of_points_in_median*ui_lead_no*2);

						DetectFiducialPoints(context, nChannel_No);//vnp 1 feb 13, median_buffer[nChannel_No], no_of_points_in_median);//vnp 31 jan 14 (ui_lead_no, Start_add, no_of_points_in_median);
				//vnp 3 oct 12	}
//						Toast.makeText(context, "channel_no_qrs_detect = " + Integer.toString(nChannel_No), //vnp 22 march 14
//							Toast.LENGTH_LONG).show();

					PutPositions(nChannel_No);//vnp 31 jan 14(ui_lead_no);// vnp 14 May 12(siMedianBuffer);
//					Toast.makeText(context, "channel_no_put_positions = " + Integer.toString(nChannel_No), //vnp 22 march 14
//							Toast.LENGTH_LONG).show();


					CalculateAmplitudes(nChannel_No);//vnp 31 jan 14 (ui_lead_no);
				 
//					Toast.makeText(context, "channel_no_calculate_amplitude = " + Integer.toString(nChannel_No), //vnp 22 march 14
//							Toast.LENGTH_LONG).show();
					nMedianIndex = 0;
					bCollectQRSSample = false;
				}
			}


		
	}
	
	void DetectFiducialPoints(Context context, short nChannel_No)//vnp 1 feb 14, short  median_buffer, short no_of_points_in_median)
	{
		SearchForRWave(context, nChannel_No);//{ r_index}
//		Toast.makeText(context, "channel_no_search_R_Wave = " + Integer.toString(nChannel_No), //vnp 22 march 14
//		Toast.LENGTH_LONG).show();
		SearchForQWave(context, nChannel_No); //{ e_point and q_index }
//		Toast.makeText(context, "channel_no_search_Q_wave = " + Integer.toString(nChannel_No), //vnp 22 march 14
//		Toast.LENGTH_LONG).show();
		SearchForPWave(context, nChannel_No); //{ p_index,start and end of p wave }
//		Toast.makeText(context, "channel_no_search_P_wave = " + Integer.toString(nChannel_No), //vnp 22 march 14
//		Toast.LENGTH_LONG).show();
		SearchForRWave2(context, nChannel_No); //{ start and end of r wave}
//		Toast.makeText(context, "channel_no_Search_R_Wave2 = " + Integer.toString(nChannel_No), //vnp 22 march 14
//		Toast.LENGTH_LONG).show();
		SearchForSWave(context, nChannel_No); //{ end of s wave }
//		Toast.makeText(context, "channel_no_search_S_wave = " + Integer.toString(nChannel_No), //vnp 22 march 14
//		Toast.LENGTH_LONG).show();
		SearchForTWave(context, nChannel_No);//(siECGBuffer, Settings->GetCurrentRRInterval());//{start and end of t wave }
//		Toast.makeText(context, "channel_no_search_T_wave = " + Integer.toString(nChannel_No), //vnp 22 march 14
//		Toast.LENGTH_LONG).show();
		
		
	}
	
	void SearchForRWave(Context context, short nChannel_No)
	{ //r_index
		short nStartIndex = 0;
		short siRValue = 0;
		short siMaxValue = 0;
		short nMaxIndex = 0;
		
		nStartIndex = (short) (nTriggerPoint - 10);
		siRValue = median_buffer[nChannel_No][nStartIndex];
		siMaxValue = siRValue;
		nMaxIndex = nStartIndex;
		
		while(nStartIndex < (nTriggerPoint + 10))
		{
			if( (siRValue >= median_buffer[nChannel_No][nStartIndex+1]) && (siRValue >= median_buffer[nChannel_No][nStartIndex-1]) && (siRValue > 0))
			{
				if(siRValue > siMaxValue)
				{
					siMaxValue = siRValue;
					//nMaxIndex = nStartIndex;
				}
				nMaxIndex = nStartIndex; //**modified on 03-05-2002
			}
			nStartIndex++;
			siRValue = median_buffer[nChannel_No][nStartIndex];
		}
		nRPoint = nMaxIndex;
		
		
	}
	
	void SearchForQWave(Context context,short nChannel_No)
	{ //e_point and q_index 
		short nStartIndex = 0;
		short siEValue = 0;
		short siQValue = 0;
		short siMaxValue = 0, current_value, next_value;
		short nMaxIndex = 0;
		boolean bPeakFound = false;
		boolean bSlopeZero = false;

		nStartIndex = (short)(nRPoint - 2);
		//nStartIndex = nRPoint - 10;
		bSlopeZero = false;

		while((!bSlopeZero) && (nStartIndex > (nTriggerPoint - 10)))
		{
			current_value= (short)(Math.abs(median_buffer[nChannel_No][nStartIndex]));//vnp 1 feb 14 ReadSample(nStartIndex)
			next_value = (short)(Math.abs(median_buffer[nChannel_No][nStartIndex + 1]));//vnp 1 feb 14 ReadSample(nStartIndex+2);
			//vnp 16 jan 16 if((current_value - next_value) <= 2 )
				if((short)(Math.abs(current_value - next_value)) <= 2 )
				bSlopeZero = true;
			nStartIndex = (short)(nStartIndex-1);
		}
//		Toast.makeText(context, "Channel no = " + Integer.toString(nChannel_No),
//				Toast.LENGTH_LONG).show();

		if(!bSlopeZero)
			nEPoint = (short)(nRPoint - 10);//vnp 1 feb 14 20;//10;
		else	
			nEPoint = nStartIndex;

//		Toast.makeText(context, "Eoint_Detected = " + Integer.toString(nEPoint), //vnp 6 april 15
//				Toast.LENGTH_LONG).show();

		nStartIndex = nEPoint;
		siEValue = median_buffer[nChannel_No][nEPoint];
		siQValue = (short) Math.abs(median_buffer[nChannel_No][nStartIndex] - siEValue);
		
		nQPoint = nEPoint;
		siMaxValue = siQValue;
		nMaxIndex = nEPoint;
	     
		while(nStartIndex < (nRPoint - 2))
		{
			if( (siQValue >= (short) Math.abs(median_buffer[nChannel_No][nStartIndex + 1] - siEValue)) &&
				(siQValue >= (short) Math.abs(median_buffer[nChannel_No][nStartIndex - 1] - siEValue)))
			{
				if(siQValue < siMaxValue)
				{
					siMaxValue = siQValue;
				}
				nMaxIndex = nStartIndex;
			}
			nStartIndex++;
			siQValue = (short) Math.abs(median_buffer[nChannel_No][nStartIndex + 1] - siEValue);
		}
		nQPoint = nMaxIndex;
		
	}
	void SearchForPWave(Context context,short nChannel_No)
	{// p_index,start and end of p wave
		short nStartIndex = 0;
		short siPValue = 0;
		short siEValue = 0;
		boolean bStartFound = false;
		short nPStartLimit=1;
		nPStart = 0;
		nPEnd = 0;

		nStartIndex = nEPoint;
		siEValue = median_buffer[nChannel_No][nEPoint] ;//vnp 3 feb 14 ReadSample(nEPoint);
		nPPoint = (short)(nEPoint - 5);//10;//5;
		siPValue = (short) Math.abs(median_buffer[nChannel_No][nStartIndex] - siEValue); //vnp 3 feb 14 abs(ReadSample(nStartIndex)-siEValue);
		bStartFound = false;
	//	Toast.makeText(context, "EPoint = " + Integer.toString(nEPoint),
	//			Toast.LENGTH_LONG).show();

		while(!bStartFound)
		{
			if((short) Math.abs(median_buffer[nChannel_No][nStartIndex] - siEValue)> siPValue)//vnp 3 feb 14 (abs(ReadSample(nStartIndex) - siEValue) > siPValue)
			{
				siPValue = (short) Math.abs(median_buffer[nChannel_No][nStartIndex] - siEValue); //vnp 3 feb 14 abs (ReadSample(nStartIndex)-siEValue);
				nPPoint = nStartIndex;
	//			Toast.makeText(context, "PPoint = " + Integer.toString(nPPoint),
	//					Toast.LENGTH_LONG).show();
			}
			nStartIndex--;//vnp 3 feb 14 nStartIndex=nStartIndex-2;
			if(nStartIndex==1)//vnp 3 feb 14 (nStartIndex == (25000 + ((no_of_points_in_median*2)*nChannelNo)+2))
			{
				bStartFound = true;//TRUE;
	//			Toast.makeText(context, "StartFound = " + Integer.toString(nStartIndex),
	//					Toast.LENGTH_LONG).show();

			}
		}

		//			Toast.makeText(context, "PPoint = " + Integer.toString(nPPoint),
		//					Toast.LENGTH_LONG).show();

		nStartIndex = nPPoint;
		bStartFound = false;
		
		if(nPPoint > 20)  //vnp 25 march 14
				nPStartLimit= (short) (nPPoint -20);   //vnp 25 march 14
		else
			nPStartLimit=1;	//vnp 25 march 14
		
		

		while((!bStartFound) && ( nStartIndex > nPStartLimit)) //((!bStartFound) && ( nStartIndex > (nPPoint - 20))) 
		{
			if(Math.abs(median_buffer[nChannel_No][nStartIndex] - siEValue) < (int)((float)siPValue * (float)0.35))//vnp 3 feb 14 (abs(ReadSample(nStartIndex) - siEValue) < (int)((float)siPValue * (float)0.35))
				bStartFound = true;
			nStartIndex--;//vnp 3 feb 14 nStartIndex = nStartIndex - 2;
		}

		nPStart = nStartIndex;
	//				Toast.makeText(context, "PStart = " + Integer.toString(nPStart),
	//						Toast.LENGTH_LONG).show();

		if(nPStart<=1)//vnp 3 feb 14 if(nPStart <= (2+(25000 + ((no_of_points_in_median*2)*nChannelNo))))
			nPStart=5;//vnp 3 feb 14vnp 3 feb 14nPStart = 10+(25000 + ((no_of_points_in_median*2)*nChannelNo));


		nStartIndex = nPPoint;
		bStartFound = false;//FALSE;
		while((!bStartFound) && ( nStartIndex < (nPPoint + 20) ) )
		{
			if(Math.abs(median_buffer[nChannel_No][nStartIndex] - siEValue) < (int)((float)siPValue * (float)0.35))//vnp 3 feb 14 (abs(ReadSample(nStartIndex) - siEValue) < (int)((float)siPValue * (float)0.35))
				bStartFound = true;//TRUE;
			nStartIndex++;//vnp 3 feb 14nStartIndex = nStartIndex + 2;
		}

		nPEnd = nStartIndex;
	//	Toast.makeText(context, "PEnd = " + Integer.toString(nPEnd),
	//			Toast.LENGTH_LONG).show();

		if(nPEnd<=1)//vnp 3 feb 14if(nPEnd <= (2+(25000 + ((no_of_points_in_median*2)*nChannelNo))))
			nPEnd=5;//vnp 3 feb 14nPEnd = 10+(25000 + ((no_of_points_in_median*2)*nChannelNo));


	//vnp 3 feb 14	Wave_Marker[nChannelNo][0]=nPStart;
		//vnp 3 feb 14	Wave_Marker[nChannelNo][1]=nPEnd;
		
	}
	void SearchForRWave2(Context context,short nChannel_No)
	{ //start and end of r wave
		short nStartIndex = 0;
		short siEValue = 0;
		boolean bStartFound = false;

		nStartIndex = nRPoint;
		siEValue = median_buffer[nChannel_No][nEPoint];//vnp 3 feb 14 siECGBuffer[nEPoint];

	    bStartFound = false;

		while((!bStartFound) && (nStartIndex > (nRPoint-10)))
		{
			if((median_buffer[nChannel_No][nStartIndex+1] > siEValue) && (median_buffer[nChannel_No][nStartIndex] < siEValue))//vnp 3 feb 14 ((siECGBuffer[nStartIndex+1] > siEValue) && (siECGBuffer[nStartIndex] < siEValue))
				bStartFound = true;
			nStartIndex--;
		}

		nRStart = (short)(nStartIndex + 1);

		//Added by Manjiri on 26-11-2007 for R-End point correction
		boolean bEndFound = false;
		nStartIndex = nRPoint;
		while((!bEndFound) && (nStartIndex < (nRPoint+15)))
		{
			if((median_buffer[nChannel_No][nStartIndex] >= siEValue) && (median_buffer[nChannel_No][nStartIndex+1]<= siEValue))//vnp 3 feb 14 ((siECGBuffer[nStartIndex] >= siEValue) && (siECGBuffer[nStartIndex+1] <= siEValue))
				bEndFound = true;
			nStartIndex++;
		}
		if(bEndFound)
			nREnd =(short) ( nStartIndex-1);
		else
		{
			//Added by Manjiri on 10-12-2007 for R-End point detection when S wave is absent
			bEndFound = false;
			nStartIndex = nRPoint;
			short siRValue = median_buffer[nChannel_No][nStartIndex];//vnp 3 feb 14 siECGBuffer[nStartIndex];
			nREnd = nStartIndex;
			while(nStartIndex < (nRPoint+15))
			{
				nStartIndex++;
				if(median_buffer[nChannel_No][nStartIndex]<  siRValue)//vnp 3 feb 14 (siECGBuffer[nStartIndex] <  siRValue)
					nREnd = nStartIndex;

				//Added on 11-12-2007
				//if(siECGBuffer[nStartIndex] <  siECGBuffer[nStartIndex+1])
				//	bEndFound = true;
			}
			//------------------------$$$--------------------------------
		}
		
	}
	void SearchForSWave(Context context,short nChannel_No)
	{ //end of s wave 
		short nStartIndex = 0;
		short siSValue = 0;
		short siEValue = 0;
		short siMaxValue = 0;
		short nMaxIndex = 0;
		boolean bStartFound = false;
		boolean bSlopeZero = false;

		nStartIndex = (short)(nREnd + 3);//3; //2; modified on 03-05-2002
		bSlopeZero = false;

		while((!bSlopeZero) && (nStartIndex < (nREnd + 15)))//15)))
		{
			if(Math.abs(median_buffer[nChannel_No][nStartIndex] - median_buffer[nChannel_No][nStartIndex + 1]) <= 5)//vnp 5 feb 14 (abs(ReadSample(nStartIndex) - ReadSample(nStartIndex+2)) <= 5)
				bSlopeZero = true;
			nStartIndex++; //vnp 5 feb 14 nStartIndex = nStartIndex + 2;
		}
		nSEnd = (short)(nStartIndex-1);//vnp 5 feb 14  2;//1;

		if(nSEnd != (nREnd+2))//vnp 4 feb 14  4))//2))
		{
			nStartIndex = nREnd;
			siEValue = median_buffer[nChannel_No][nEPoint];//vnp 5 feb 14  ReadSample(nEPoint);
			siSValue =(short) ( Math.abs(median_buffer[nChannel_No][nStartIndex] - siEValue));//vnp 4 feb 14  abs( ( ReadSample(nStartIndex)) - siEValue);
			siMaxValue = siSValue;
			nMaxIndex = nREnd;

			while( nStartIndex < nSEnd)
			{
				if((siSValue >= Math.abs(median_buffer[nChannel_No][nStartIndex + 1] - siEValue)) && (siSValue >= Math.abs(median_buffer[nChannel_No][nStartIndex - 1] - siEValue)))
				//vnp 4 feb 14 ( (siSValue >= abs(ReadSample(nStartIndex+2) - siEValue)) && (siSValue >= abs(ReadSample(nStartIndex-2) - siEValue)) ) 
				{
					if(siSValue < siMaxValue)
					{
						siMaxValue = siSValue;
					}
					nMaxIndex = nStartIndex;//???
				}
				nStartIndex++;//vnp 4 feb 14 nStartIndex = nStartIndex + 2;
				siSValue = (short)(Math.abs(median_buffer[nChannel_No][nStartIndex] - siEValue));//abs (ReadSample(nStartIndex) - siEValue);
			}
			nSPoint = nMaxIndex;
		}
		
	}
	void SearchForTWave(Context context,short nChannel_No)//(siECGBuffer, Settings->GetCurrentRRInterval());//
	{//start and end of t wave 
		short nStartIndex = 0;
		boolean bSlopeZero = false;
		boolean bEndFound = false;
		short siTValue = 0;
		short siEValue = 0;

		nStartIndex = (short)(nREnd + 20); //vnp 5 feb 14 nREnd + 40
		siEValue = median_buffer[nChannel_No][nStartIndex];//vnp 5 feb 14ReadSample(nStartIndex);
		siTValue = (short)(Math.abs(median_buffer[nChannel_No][nStartIndex] - siEValue)); //vnp 5 feb 14abs(ReadSample(nStartIndex)-siEValue);
		bEndFound = false;	
		nTPoint = (short)(nREnd+20); //vnp 5 feb 14nREnd + 40;    

		while(!bEndFound)
		{
			if(Math.abs(median_buffer[nChannel_No][nStartIndex] - siEValue) > siTValue)
			//vnp 5 feb 14 (abs(ReadSample(nStartIndex) - siEValue) > siTValue)
			{
				siTValue = (short)(Math.abs(median_buffer[nChannel_No][nStartIndex] - siEValue));//vnp 5 feb 14 abs(ReadSample(nStartIndex) - siEValue);
				nTPoint = nStartIndex;
			}
			nStartIndex++;//vnp 5 feb 14 nStartIndex = nStartIndex + 2;
			if(nStartIndex >= no_of_points_in_median)
			//vnp 5 feb 14 (nStartIndex == (25000 + ((no_of_points_in_median)*(nChannelNo+1)*2)))
				bEndFound = true;
		}

		if( nTPoint == nREnd)
			nTPoint = (short)(nTriggerPoint + 100) ;//vnp 5 feb 14 25000 + (nChannelNo*no_of_points_in_median*2)+(nTriggerPoint*2) + 200;

		nStartIndex =(short)(nTPoint+20);//vnp 5 feb 14 nTPoint + 40;
		if(nStartIndex>no_of_points_in_median)
		//vnp 4 feb 14 (nStartIndex> ((no_of_points_in_median)*(nChannelNo+1)*2))
			nStartIndex = no_of_points_in_median;//vnp 5 feb 14 (no_of_points_in_median)*(nChannelNo+1)*2;
		bSlopeZero = true;

		//***TEnd detection algorithm modified on 31-01-2003
		while((bSlopeZero) && (nStartIndex > nTPoint)) 
		{
			if((short)(median_buffer[nChannel_No][nStartIndex-1] - median_buffer[nChannel_No][nStartIndex]) >= 3)
			//vnp 5 feb 14 ((ReadSample(nStartIndex-2) - ReadSample(nStartIndex)) >= 3)
				bSlopeZero = false;
			nStartIndex--;//vnp 5 feb 14 nStartIndex = nStartIndex - 2;
		}

		if(!bSlopeZero)
			nTEnd = nStartIndex;
		else
			nTEnd = (short)(nTPoint+20);//vnp 5 feb 14 nTPoint+40;//vnp 7 feb 13  25000 + ((no_of_points_in_median* (nChannelNo+1)*2) - 30); //vnp 9 nov 12  //15
	
		nStartIndex =(short)(nTPoint+5);//vnp 5 feb 14 nTPoint + 10;//5;
		bSlopeZero = true; 

		
		while((bSlopeZero) && (nStartIndex < no_of_points_in_median))
			//vnp 7 feb 14( (bSlopeZero) && (nStartIndex < (no_of_points_in_median* 2*(nChannelNo+1))))   //vnp 9 nov 12
		{
			if((median_buffer[nChannel_No][nStartIndex] - median_buffer[nChannel_No][nStartIndex - 2]) >= 2)
			//vnp 7 feb 14((ReadSample(nStartIndex) - ReadSample(nStartIndex-2)) >= 2)
				bSlopeZero = false;
			nStartIndex++;//vnp 7 feb 14 nStartIndex = nStartIndex + 2;
		}
		if((nTEnd==(nTPoint+20)) && (nStartIndex == (nTPoint+5)))
		//vnp 7 feb 14((nTEnd==(nTPoint+40)) && (nStartIndex==(nTPoint+10)))
			nTEnd =(short) (nTPoint + 20);//vnp 7 feb 14 nTPoint+40;
		else
		{
			/*nTEnd = ((nTEnd - (25000+(2*no_of_points_in_median*nChannelNo)))+ (nStartIndex- (25000+(2*no_of_points_in_median*nChannelNo)))) / 2;
			nTEnd = nTEnd + (25000+(2*no_of_points_in_median*nChannelNo));*/
			nTEnd = (short)((nTEnd+ nStartIndex)/2);  //vnp 7 feb 14
		//nTEnd = nStartIndex;
		}	
		//***//***

		nStartIndex =(short)(nTPoint - 2);//vnp 7 feb 14  nTPoint - 4;//2;
		bSlopeZero = true;
		
		while((bSlopeZero) && (nStartIndex > nSEnd))
		{
			if(Math.abs(median_buffer[nChannel_No][nStartIndex] - median_buffer[nChannel_No][nStartIndex + 1]) <=1)
			//vnp 7 feb 14 (abs(ReadSample(nStartIndex) - ReadSample(nStartIndex+2)) <= 1)
				bSlopeZero = false;
			nStartIndex--; //vnp 7 feb 14 nStartIndex = nStartIndex -2;
		}
		nTStart = nStartIndex;

		nStartIndex = nSEnd;
		bSlopeZero = true;
		
		while(bSlopeZero)
		{
			if(Math.abs(median_buffer[nChannel_No][nStartIndex] - median_buffer[nChannel_No][nStartIndex + 1]) <= 1)
			//vnp 7 feb 14 (abs(ReadSample(nStartIndex) - ReadSample(nStartIndex+2)) <= 1)
				bSlopeZero = false;
			nStartIndex++; //vnp 7 feb 14 nStartIndex = nStartIndex + 2;
		}
		/*nTStart = ((nTStart- (25000+(2*no_of_points_in_median*nChannelNo))) + (nStartIndex- (25000+(2*no_of_points_in_median*nChannelNo)))) /2;
		nTStart= nTStart + (25000+(2*no_of_points_in_median*nChannelNo));
		if((nTStart%2)==1)
			nTStart=nTStart+1;
		if((nTEnd%2)==1)
			nTEnd=nTEnd+1; */ //vnp 7 feb 14
		
		nTStart = (short)((nTStart + nStartIndex)/2);

		
	//vnp 7 feb 14	Wave_Marker[nChannelNo][3]=nTStart;
	//vnp 7 feb 14	Wave_Marker[nChannelNo][4]=nTEnd;
		
	}
	void PutPositions_old(short nChannel_No) //vnp 27 may 16
	{
		nWavePositions[nChannel_No][P_PLUS_START] = nPStart;
		nWavePositions[nChannel_No][P_PLUS_END  ] = nPEnd;
		nWavePositions[nChannel_No][P_NEG_END ] = nPEnd;
		nWavePositions[nChannel_No][Q_START     ] = nEPoint;
		nWavePositions[nChannel_No][R_START	   ] = nRStart;
		nWavePositions[nChannel_No][R_END	   ] = nREnd;
		nWavePositions[nChannel_No][S_END	   ] = nSEnd;
		nWavePositions[nChannel_No][R_DASH_END  ] = nSEnd;
		nWavePositions[nChannel_No][S_DASH_END  ] = nSEnd;
		nWavePositions[nChannel_No][T_START	   ] = nTStart;
		nWavePositions[nChannel_No][T_END	   ] = nTEnd;
		nWavePositions[nChannel_No][U_START	   ] = nTEnd;
		nWavePositions[nChannel_No][U_END	   ] = nTEnd;
		
			if(median_buffer[nChannel_No][nPPoint] > median_buffer[nChannel_No][nEPoint])
			//vnp 8 feb 14(ReadSample(nPPoint) > ReadSample(nEPoint))
			{
				siAmplitude[nChannel_No][P_PLUS_AMP] =  (short) (median_buffer[nChannel_No][nPPoint] - median_buffer[nChannel_No][nEPoint]);//vnp 8 feb 14ReadSample(nPPoint) - ReadSample(nEPoint);
				siAmplitude[nChannel_No][P_NEG_AMP] = 0;

			}
			else
			{
				siAmplitude[nChannel_No][P_PLUS_AMP] = 0;
				siAmplitude[nChannel_No][P_NEG_AMP] = (short) (median_buffer[nChannel_No][nPPoint] - median_buffer[nChannel_No][nEPoint]);//vnp 8 feb 14ReadSample(nPPoint) - ReadSample(nEPoint);

			}

			siAmplitude[nChannel_No][Q_AMP] = (short) (median_buffer[nChannel_No][nQPoint] - median_buffer[nChannel_No][nEPoint]);//vnp 8 feb 14 ReadSample(nQPoint) - ReadSample(nEPoint);
			siAmplitude[nChannel_No][R_AMP] = (short) (median_buffer[nChannel_No][nPPoint] - median_buffer[nChannel_No][nRPoint]);//vnp 8 feb 14ReadSample(nRPoint) - ReadSample(nEPoint);
			siAmplitude[nChannel_No][S_AMP] = (short) (median_buffer[nChannel_No][nPPoint] - median_buffer[nChannel_No][nSPoint]);//vnp 8 feb 14ReadSample(nSPoint) - ReadSample(nEPoint);
			siAmplitude[nChannel_No][R_DASH_AMP] = 0;
			siAmplitude[nChannel_No][S_DASH_AMP] = 0;
			siAmplitude[nChannel_No][T_AMP] = (short) (median_buffer[nChannel_No][nPPoint] - median_buffer[nChannel_No][nEPoint]);//vnp 8 feb 14ReadSample(nTPoint) - ReadSample(nEPoint);
			siAmplitude[nChannel_No][U_AMP] = 0;
			siAmplitude[nChannel_No][QRS_AMP] =(short) (median_buffer[nChannel_No][nPPoint] - median_buffer[nChannel_No][nEPoint]);//vnp 8 feb 14 siAmplitude[ui_lead_no][R_AMP] + siAmplitude[ui_lead_no][S_AMP];
	
	}

	void PutPositions(short nChannel_No) {
		for (char l = 0; l < 12; l++) {
			nWavePositions[l][P_PLUS_START] = nPStart;
			nWavePositions[l][P_PLUS_END] = nPEnd;
			nWavePositions[l][P_NEG_END] = nPEnd;
			nWavePositions[l][Q_START] = nEPoint;
			nWavePositions[l][R_START] = nRStart;
			nWavePositions[l][R_END] = nREnd;
			nWavePositions[l][S_END] = nSEnd;
			nWavePositions[l][R_DASH_END] = nSEnd;
			nWavePositions[l][S_DASH_END] = nSEnd;
			nWavePositions[l][T_START] = nTStart;
			nWavePositions[l][T_END] = nTEnd;
			nWavePositions[l][U_START] = nTEnd;
			nWavePositions[l][U_END] = nTEnd;

//			Toast.makeText(context, "PStart = " + Integer.toString(nPStart),
//					Toast.LENGTH_LONG).show();

//			Toast.makeText(context, "PEnd = " + Integer.toString(nPEnd),
//						Toast.LENGTH_LONG).show();


			if (median_buffer[l][nPPoint] > median_buffer[l][nEPoint])
			//vnp 8 feb 14(ReadSample(nPPoint) > ReadSample(nEPoint))
			{
				siAmplitude[l][P_PLUS_AMP] = (short) (median_buffer[l][nPPoint] - median_buffer[l][nEPoint]);//vnp 8 feb 14ReadSample(nPPoint) - ReadSample(nEPoint);
				siAmplitude[l][P_NEG_AMP] = 0;

			} else {
				siAmplitude[l][P_PLUS_AMP] = 0;
				siAmplitude[l][P_NEG_AMP] = (short) (median_buffer[l][nPPoint] - median_buffer[l][nEPoint]);//vnp 8 feb 14ReadSample(nPPoint) - ReadSample(nEPoint);

			}

			siAmplitude[l][Q_AMP] = (short) (median_buffer[l][nQPoint] - median_buffer[l][nEPoint]);//vnp 8 feb 14 ReadSample(nQPoint) - ReadSample(nEPoint);
			siAmplitude[l][R_AMP] = (short) (median_buffer[l][nPPoint] - median_buffer[l][nRPoint]);//vnp 8 feb 14ReadSample(nRPoint) - ReadSample(nEPoint);
			siAmplitude[l][S_AMP] = (short) (median_buffer[l][nPPoint] - median_buffer[l][nSPoint]);//vnp 8 feb 14ReadSample(nSPoint) - ReadSample(nEPoint);
			siAmplitude[l][R_DASH_AMP] = 0;
			siAmplitude[l][S_DASH_AMP] = 0;
			siAmplitude[l][T_AMP] = (short) (median_buffer[l][nPPoint] - median_buffer[l][nEPoint]);//vnp 8 feb 14ReadSample(nTPoint) - ReadSample(nEPoint);
			siAmplitude[l][U_AMP] = 0;
			siAmplitude[l][QRS_AMP] = (short) (median_buffer[l][nPPoint] - median_buffer[l][nEPoint]);//vnp 8 feb 14 siAmplitude[ui_lead_no][R_AMP] + siAmplitude[ui_lead_no][S_AMP];

		}
	}


	short FindPeakIndex(short nStartPoint, short nEndPoint, short nPeakOption, short ui_lead_no)
	{
		short nPeakIndex = 0;
		
		if(nStartPoint >= nEndPoint)//(nStartPoint == nEndPoint)  // vnp 9 aug 12
		{
		//vnp 14 feb 14	nStartPoint = nStartPoint-(25000+(ui_lead_no*no_of_points_in_median*2));   //vnp 10 Aug 12
			//vnp 14 feb 14	nEndPoint = nEndPoint - (25000+(ui_lead_no*no_of_points_in_median*2));		//vnp 10 Aug 12
			nPeakIndex = (short) ((short)(nStartPoint + nEndPoint) /2);									//vnp 10 Aug 12
			//vnp 14 feb 14		if((nPeakIndex%2)==1)
			//vnp 14 feb 14			nPeakIndex=nPeakIndex+1;
			//vnp 14 feb 14	nPeakIndex = nPeakIndex + (25000+(ui_lead_no*no_of_points_in_median*2));	//vnp 10 Aug 12

			return nPeakIndex;
		}
		
		short siMaxValue;
		short siMinValue;
		short nStartIndex;

		siMaxValue = median_buffer[ui_lead_no][nStartPoint]; //vnp 14 feb 14ReadSample(nStartPoint);
		nPeakIndex = nStartPoint;

		nStartIndex = nStartPoint;// + 1;

		if(nPeakOption == 1)
		{
			while(nStartIndex <= nEndPoint)
			{

				if(median_buffer[ui_lead_no][nStartIndex] > siMaxValue)
				//vnp 14 feb 14(ReadSample(nStartIndex) > siMaxValue)
				{
					siMaxValue = median_buffer[ui_lead_no][nStartIndex];//vnp 14 feb 14ReadSample(nStartIndex);
					nPeakIndex = nStartIndex;
				}
				nStartIndex++;
			}		
		}
		else if(nPeakOption == 0)
		{
			while(nStartIndex <= nEndPoint)
			{
	  			if(median_buffer[ui_lead_no][nStartIndex] <siMaxValue )
	  		//vnp 14 feb 14(ReadSample(nStartIndex) < siMaxValue)
				{
					siMaxValue = median_buffer[ui_lead_no][nStartIndex]; //vnp 14 feb 14ReadSample(nStartIndex);
					nPeakIndex = nStartIndex;
				}

				nStartIndex++;
			}
		}
		else if(nPeakOption == 3)
		{
//bmp 03-May-25 Try To Avoid crash due to negative index
//			short nMaxPeakIndex = 0;
//			short nMinPeakIndex = 0;
			short nMaxPeakIndex = 1;
			short nMinPeakIndex = 1;
//bmp 03-May-25 Try To Avoid crash due to negative index
			short temp_value=0;

			siMaxValue = 0;
			siMinValue = MAX_BITCOUNT;
			while(nStartIndex <= nEndPoint)
			{
				temp_value = median_buffer[ui_lead_no][nStartIndex];//vnp 14 feb 14ReadSample(nStartIndex);
				if(temp_value > siMaxValue )
				{
					siMaxValue = temp_value; //ReadSample(nStartIndex);
					nMaxPeakIndex = nStartIndex;
				}

				if(temp_value < siMinValue)
				{
					siMinValue = temp_value;//ReadSample(nStartIndex);
					nMinPeakIndex = nStartIndex;
				}
				nStartIndex++; //vnp 14 feb 14nStartIndex = nStartIndex + 2;
			}


			short nPrevValue = median_buffer[ui_lead_no][nMaxPeakIndex-1]; //vnp 14 feb 14ReadSample(nMaxPeakIndex-2);
			short nCurrValue = median_buffer[ui_lead_no][nMaxPeakIndex]; //vnp 14 feb 14 ReadSample(nMaxPeakIndex);
			short nNextValue = median_buffer[ui_lead_no][nMaxPeakIndex+1];//vnp 14 feb 14 ReadSample(nMaxPeakIndex+2 );
			boolean bPosPeakFound = false;
			boolean bNegPeakFound = false;


			if( (nCurrValue >= nPrevValue) && (nCurrValue >= nNextValue) )
				bPosPeakFound = false;

			nPrevValue = median_buffer[ui_lead_no][nMinPeakIndex-1];//vnp 14 feb 14ReadSample(nMinPeakIndex-2);
			nCurrValue = median_buffer[ui_lead_no][nMinPeakIndex];//vnp 14 feb 14ReadSample(nMinPeakIndex);
			nNextValue = median_buffer[ui_lead_no][nMinPeakIndex+1];//vnp 14 feb 14ReadSample(nMinPeakIndex+2);

			
			if( (nCurrValue <= nPrevValue) && (nCurrValue <= nNextValue) )
				bNegPeakFound = true;
			
			if(bPosPeakFound && bNegPeakFound)
			{
				if(Math.abs(siMaxValue - median_buffer[ui_lead_no][nQStart]) > Math.abs(siMinValue - median_buffer[ui_lead_no][nQStart]))
				//vnp 14 feb 14 (abs(siMaxValue - ReadSample(nQStart)) > abs(siMinValue - ReadSample(nQStart)))
					nPeakIndex = nMaxPeakIndex;
				else
					nPeakIndex = nMinPeakIndex;
			}
			else if(!bPosPeakFound && !bNegPeakFound)
			{
				//vnp 14 feb 14	nStartPoint = nStartPoint-(25000+(ui_lead_no*no_of_points_in_median*2));
				//vnp 14 feb 14	nEndPoint = nEndPoint - (25000+(ui_lead_no*no_of_points_in_median*2));
				nPeakIndex = (short)((short)(nStartPoint + nEndPoint) /2);
				//vnp 14 feb 14	if((nPeakIndex%2)==1)
				//vnp 14 feb 14		nPeakIndex=nPeakIndex+1;

				//vnp 14 feb 14	nPeakIndex = nPeakIndex + (25000+(ui_lead_no*no_of_points_in_median*2));
			}
			else
			{
				if(bPosPeakFound)
					nPeakIndex = nMaxPeakIndex;
				else
					nPeakIndex = nMinPeakIndex;
			}
		}
		//vnp 14 feb 14		if((nPeakIndex<25000)|| (nPeakIndex > 35320))
		//vnp 14 feb 14		{
		//vnp 14 feb 14		asm("nop");
		//vnp 14 feb 14		asm("nop");
		//vnp 14 feb 14		asm("nop");

		//vnp 14 feb 14	}

		return nPeakIndex;

	}
	void Find_Total_Positive_Negative_Points(short nStartPoint, short nEndPoint, short ui_lead_no)   //vnp 23 april 13
	{
		short ui_Positive_cnt=0, ui_Negative_cnt=0, ui_Total_cnt=0, ui_BufferIndex=0, ui_Point_cnt=0, nReferencePoint;

		if(nEndPoint > nStartPoint)
		{
			ui_Total_cnt =  (short) ((short)(nEndPoint - nStartPoint)/2);
				//vnp 17 feb 14	((nEndPoint-(25000+(ui_lead_no*no_of_points_in_median*2))) - (nStartPoint-(25000+(ui_lead_no*no_of_points_in_median*2))))/2;
			ui_BufferIndex =(short) (nStartPoint + 1);//vnp 17 feb 14 nStartPoint + 2;
		}
		else if(nStartPoint > nEndPoint)
		{
			ui_Total_cnt = (short) ((short)(nStartPoint - nEndPoint)/2  );
					//vnp 17 feb 14((nStartPoint-(25000+(ui_lead_no*no_of_points_in_median*2))) - (nEndPoint-(25000+(ui_lead_no*no_of_points_in_median*2))))/2;
			ui_BufferIndex = (short)(nEndPoint+1); //vnp 17 feb 14 nEndPoint + 2;
		}
		else
		{
			siTAmplitude_inv[ui_lead_no] = (float) 0.0;
		}
		
		if(nEndPoint != nStartPoint)
		{	
			if(median_buffer[ui_lead_no][nEPoint] > median_buffer[ui_lead_no][nStartPoint])
			//vnp 17 feb 14(ReadSample(nEPoint) > ReadSample(nStartPoint))
				nReferencePoint = nStartPoint;
			else
				nReferencePoint = nEPoint;

			while(ui_Point_cnt < ui_Total_cnt )
			{
				if(median_buffer[ui_lead_no][nReferencePoint] < median_buffer[ui_lead_no][ui_BufferIndex])
				//vnp 17 feb 14(ReadSample(nReferencePoint)<ReadSample(ui_BufferIndex))
					ui_Positive_cnt++;
				else if(median_buffer[ui_lead_no][nReferencePoint] > median_buffer[ui_lead_no][ui_BufferIndex])
				//vnp 17 feb 14(ReadSample(nReferencePoint)>ReadSample(ui_BufferIndex))
					ui_Negative_cnt++;
				ui_BufferIndex++; //vnp 17 feb 14ui_BufferIndex = ui_BufferIndex + 2;
				ui_Point_cnt++;
			}
		
			if(ui_Positive_cnt>= ((6 * ui_Total_cnt)/10))
				siTAmplitude_inv[ui_lead_no] = (float)1.0;
			else if(ui_Negative_cnt>= ((6 * ui_Total_cnt)/10))
				siTAmplitude_inv[ui_lead_no] = (float)-1.0;
			else
				siTAmplitude_inv[ui_lead_no] = (float)0.0;
		}		
		

	}

	void CalculateAmplitudes(short nChannel_No)
	{
		short nPPosIndex = FindPeakIndex(nWavePositions[nChannel_No][P_PLUS_START], nWavePositions[nChannel_No][P_PLUS_END],(short) 3, nChannel_No);
		short nPNegIndex = FindPeakIndex(nWavePositions[nChannel_No][P_PLUS_START], nWavePositions[nChannel_No][P_NEG_END],(short) 3, nChannel_No);
		short nQIndex = FindPeakIndex(nWavePositions[nChannel_No][Q_START], nWavePositions[nChannel_No][R_START],(short) 3, nChannel_No);
		short nRIndex = FindPeakIndex(nWavePositions[nChannel_No][R_START], nWavePositions[nChannel_No][R_END],(short) 3, nChannel_No);
		short nSIndex = FindPeakIndex(nWavePositions[nChannel_No][R_END], nWavePositions[nChannel_No][S_END],(short) 3, nChannel_No);
		short nRDashIndex = FindPeakIndex(nWavePositions[nChannel_No][S_END], nWavePositions[nChannel_No][R_DASH_END],(short) 3, nChannel_No);
		short nSDashIndex = FindPeakIndex(nWavePositions[nChannel_No][R_DASH_END], nWavePositions[nChannel_No][S_DASH_END],(short) 3, nChannel_No);
		short nTIndex = FindPeakIndex(nWavePositions[nChannel_No][T_START],nWavePositions[nChannel_No][T_END],(short) 3, nChannel_No);
		short nUIndex = FindPeakIndex(nWavePositions[nChannel_No][U_START], nWavePositions[nChannel_No][U_END],(short) 3, nChannel_No);


		nQStart = nWavePositions[nChannel_No][Q_START];  // vnp 29 may 12

		//Added on 08-04-2003 ver 1.7
		if(nPPosIndex <= nPNegIndex) 
		{
			siAmp[nChannel_No][P_PLUS_AMP] = (short)(median_buffer[nChannel_No][nPPosIndex] - median_buffer[nChannel_No][nQStart]);//vnp 17 feb 14 ReadSample(nPPosIndex) - ReadSample(nQStart);
			siAmp[nChannel_No][P_NEG_AMP] = 0;
		}
		else
		{
			siAmp[nChannel_No][P_PLUS_AMP] = 0;
			siAmp[nChannel_No][P_NEG_AMP] = (short)(median_buffer[nChannel_No][nPNegIndex] - median_buffer[nChannel_No][nQStart]);//vnp 17 feb 14 ReadSample(nPNegIndex) - ReadSample(nQStart);
		}

		siPPlusAmplitude[nChannel_No]= siAmp[nChannel_No][P_PLUS_AMP] ;
		siPNegAmplitude[nChannel_No] = siAmp[nChannel_No][P_NEG_AMP];


		siAmp[nChannel_No][Q_AMP] = (short)(median_buffer[nChannel_No][nQIndex] - median_buffer[nChannel_No][nQStart]);//vnp 17 feb 14 ReadSample(nQIndex) - ReadSample(nQStart);
		siAmp[nChannel_No][R_AMP] = (short)(median_buffer[nChannel_No][nRIndex] - median_buffer[nChannel_No][nQStart]);//vnp 17 feb 14 ReadSample(nRIndex) - ReadSample(nQStart);
		siAmp[nChannel_No][S_AMP] = (short)(median_buffer[nChannel_No][nSIndex] - median_buffer[nChannel_No][nQStart]);//vnp 17 feb 14 ReadSample(nSIndex) - ReadSample(nQStart);

		if(nWavePositions[nChannel_No][S_END] != nWavePositions[nChannel_No][R_DASH_END])
			siAmp[nChannel_No][R_DASH_AMP] =(short)(median_buffer[nChannel_No][nRDashIndex] - median_buffer[nChannel_No][nQStart]);//vnp 17 feb 14  ReadSample(nRDashIndex) - ReadSample(nQStart);
		else
			siAmp[nChannel_No][R_DASH_AMP] = 0;

		if(nWavePositions[R_DASH_END] != nWavePositions[S_DASH_END])
			siAmp[nChannel_No][S_DASH_AMP] = (short)(median_buffer[nChannel_No][nSDashIndex] - median_buffer[nChannel_No][nQStart]);//vnp 17 feb 14 ReadSample(nSDashIndex) - ReadSample(nQStart);
		else
			siAmp[nChannel_No][S_DASH_AMP] = 0;

		TIndex[nChannel_No]= median_buffer[nChannel_No][nTIndex];//vnp 17 feb 14 ReadSample(nTIndex);
		QStart[nChannel_No]= median_buffer[nChannel_No][nQStart];//vnp 17 feb 14ReadSample(nQStart);
		siAmp[nChannel_No][T_AMP] =(short)(median_buffer[nChannel_No][nTIndex] - median_buffer[nChannel_No][nQStart]);//vnp 17 feb 14 ReadSample(nTIndex) - ReadSample(nQStart);
		siTAmplitude[nChannel_No]=(float)siAmp[nChannel_No][T_AMP]* (float)0.00244;//((ADC_VOLTAGE_RANGE*1000) / (MAX_BITCOUNT*AMPLIFIER_GAIN));//0.00244;
		Find_Total_Positive_Negative_Points(nWavePositions[nChannel_No][T_START],nWavePositions[nChannel_No][T_END], nChannel_No);

		if(nWavePositions[nChannel_No][U_START] != nWavePositions[nChannel_No][U_END])
			siAmp[nChannel_No][U_AMP] =(short) (median_buffer[nChannel_No][nUIndex] - median_buffer[nChannel_No][nQStart]);//vnpReadSample(nUIndex) - ReadSample(nQStart);
		else
			siAmp[nChannel_No][U_AMP] = 0;

		short nHalfMaxBitcount = MAX_BITCOUNT/2;
		if(siAmp[nChannel_No][R_AMP] < 0)
		{
			if((short)(median_buffer[nChannel_No][nQIndex] - nHalfMaxBitcount) > (short)(median_buffer[nChannel_No][nSIndex] - nHalfMaxBitcount))
			//vnp 17 feb 14 ( (ReadSample(nQIndex) - nHalfMaxBitcount) > (ReadSample(nSIndex) - nHalfMaxBitcount))
				siAmp[nChannel_No][QRS_AMP] =(short) ((short)(median_buffer[nChannel_No][nRIndex] - nHalfMaxBitcount) - (short)(median_buffer[nChannel_No][nQIndex] - nHalfMaxBitcount));
						//vnp 17 feb 14(ReadSample(nRIndex) - nHalfMaxBitcount) - (ReadSample(nQIndex) - nHalfMaxBitcount);
			else
				siAmp[nChannel_No][QRS_AMP] = (short) ((short)(median_buffer[nChannel_No][nRIndex] - nHalfMaxBitcount) - (short)(median_buffer[nChannel_No][nSIndex] - nHalfMaxBitcount));
						//vnp 17 feb 14(ReadSample(nRIndex) - nHalfMaxBitcount) - (ReadSample(nSIndex) - nHalfMaxBitcount);

		}
		else
		{
			if((short)(median_buffer[nChannel_No][nQIndex] - nHalfMaxBitcount) > (short)(median_buffer[nChannel_No][nSIndex] - nHalfMaxBitcount))
			//vnp 17 feb 14( (signed)(ReadSample(nQIndex) - nHalfMaxBitcount) > (signed)(ReadSample(nSIndex) - nHalfMaxBitcount))
				siAmp[nChannel_No][QRS_AMP] = (short) ((short)(median_buffer[nChannel_No][nRIndex] - nHalfMaxBitcount) - (short)(median_buffer[nChannel_No][nSIndex] - nHalfMaxBitcount));
				//vnp 17 feb 14 siAmp[QRS_AMP] = (ReadSample(nRIndex) - nHalfMaxBitcount) - (ReadSample(nSIndex) - nHalfMaxBitcount);
			else
				siAmp[nChannel_No][QRS_AMP] = (short) ((short)(median_buffer[nChannel_No][nRIndex] - nHalfMaxBitcount) - (short)(median_buffer[nChannel_No][nQIndex] - nHalfMaxBitcount));
				//vnp 17 feb 14 siAmp[QRS_AMP] = (ReadSample(nRIndex) - nHalfMaxBitcount) - (ReadSample(nQIndex) - nHalfMaxBitcount);

		}
		siQRSAmplitude[nChannel_No]=siAmp[nChannel_No][QRS_AMP];
	
	}
	
	void GetMeasurements(Context context)
	{
		short Sample = 0;
		short i; 
		boolean Mark_Sample= false;
	//vnp 13 march 14	Databufptr  = 25000+((no_of_points_in_median*2)*ui_lead_no);//vnp 9 oct 12 ui_lead_no*2; // vnp 11 april 12 25000;        // Lead buf start address
		First_Median = true;
		bFirstJPoint = false;  //vnp 9 oct 12
	//	if(MainActivity.lead_no==0)
	//	{
	//		Toast.makeText(context, "lead no = " + Integer.toString(MainActivity.lead_no),
	//				Toast.LENGTH_LONG).show();

	//	}
		
		//	 bFirstEightBeatsOver=1;
		ClearQRSVariables();

		
		for(i=0;i<no_of_points_in_median;i++) //vnp 9 oct 12 (i=0;i<2500;i++)          //BufferLength
		{
			Sample = median_buffer[MainActivity.lead_no][i];//vnp 13 march 14 ReadSample(Databufptr);
			QRSDetect(context, Sample,MainActivity.lead_no,i);//vnp 13 march 14QRSDetect(Sample,ui_lead_no,Databufptr);
	
	/*//vnp 27 march 14		Mark_Sample= QRSDetect_for_HR_calculation(context, Sample);
			if(Mark_Sample==true)
			{
				Toast.makeText(context, "i_count = " + Integer.toString(i),  //vnp 22 march 14
				Toast.LENGTH_LONG).show();
				Toast.makeText(context, "lead_no = " + Integer.toString(MainActivity.lead_no),  //vnp 22 march 14
				Toast.LENGTH_LONG).show();
				
			}*/
			//vnp 9 oct 12 Databufptr=Databufptr+24; // vnp 11 april 12 ++;
			//vnp 13 march 14 Databufptr++;
			//vnp 13 march 14 Databufptr++;
		}
	
//		Toast.makeText(context, "MedianIndex_in_GetMeasurements = " + Integer.toString(nMedianIndex),
//				Toast.LENGTH_LONG).show();
		
//		Toast.makeText(context, "i_count_in_QRS_Detect = " + Integer.toString(temp_median_index),
//				Toast.LENGTH_LONG).show();
//		Toast.makeText(context, "i_count_in_GetMeasurements = " + Integer.toString(i),
//				Toast.LENGTH_LONG).show();

	}
	
	static void Display_Amplitudes_in_millivolts(short nChannel_No)
	{
		Dispaly_Amplitude_Buffer[0] = (float)(siAmp[nChannel_No][P_PLUS_AMP] - siAmp[nChannel_No][P_NEG_AMP]) * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		Dispaly_Amplitude_Buffer[1] = (float)siAmp[nChannel_No][Q_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		Dispaly_Amplitude_Buffer[2] = (float)siAmp[nChannel_No][R_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		Dispaly_Amplitude_Buffer[3] = (float)siAmp[nChannel_No][S_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		Dispaly_Amplitude_Buffer[4] = (float)siAmp[nChannel_No][R_DASH_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		Dispaly_Amplitude_Buffer[5] = (float)siAmp[nChannel_No][S_DASH_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		Dispaly_Amplitude_Buffer[6] = (float)siAmp[nChannel_No][T_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		Dispaly_Amplitude_Buffer[7] = (float)siAmp[nChannel_No][U_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		Dispaly_Amplitude_Buffer[8] = (float)siAmp[nChannel_No][QRS_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);

		//Added on 28-05-2008
		//Amplitude in mV = fAmplitude/AMPLIFIER_GAIN
		
		for(int nAmp = 0; nAmp < TOTAL_AMPLITUDE_POINTS; nAmp++)
		{
			Dispaly_Amplitude_Buffer[nAmp] = (float)((Dispaly_Amplitude_Buffer[nAmp] / (float)AMPLIFIER_GAIN) * 1000);
		}

		
	}

	static void Display_Duration_in_millisecs(short nChannel_No)
	{
		nDuration[0] = (short) ((nWavePositions[nChannel_No][P_NEG_END] - nWavePositions[nChannel_No][P_PLUS_START]) * 4);
		nDuration[1] = (short) ((nWavePositions[nChannel_No][R_START] - nWavePositions[nChannel_No][Q_START]) * 4);
		nDuration[2] = (short) ((nWavePositions[nChannel_No][R_END] - nWavePositions[nChannel_No][R_START]) * 4);
		nDuration[3] = (short) ((nWavePositions[nChannel_No][S_END] - nWavePositions[nChannel_No][R_END]) * 4);
		nDuration[4] = (short) ((nWavePositions[nChannel_No][R_DASH_END] - nWavePositions[nChannel_No][S_END]) * 4);
		nDuration[5] = (short) ((nWavePositions[nChannel_No][S_DASH_END] - nWavePositions[nChannel_No][R_DASH_END]) * 4);
		nDuration[6] = (short) ((nWavePositions[nChannel_No][T_END] - nWavePositions[nChannel_No][T_START]) * 4);
		nDuration[7] = (short) ((nWavePositions[nChannel_No][U_END] - nWavePositions[nChannel_No][U_START]) * 4);
		
		
	}
	
	static void Display_Interval_in_millisecs(short nChannel_No)
	{
		
		nInterval[0] = (short) ((nWavePositions[nChannel_No][Q_START] - nWavePositions[nChannel_No][P_NEG_END]) * 4);
		nInterval[1] = (short) ((nWavePositions[nChannel_No][Q_START] - nWavePositions[nChannel_No][P_PLUS_START]) * 4);
		nInterval[2] = (short) ((nWavePositions[nChannel_No][S_DASH_END] - nWavePositions[nChannel_No][Q_START]) * 4);
		
		int nDuration = (nWavePositions[nChannel_No][T_END] - nWavePositions[nChannel_No][Q_START]) * 4;
		float fQt  =((float)nDuration / (float)1000);
		double drr,dSampling;
		drr = (double)RR_Interval;
		dSampling = (double)SAMPLING_FREQUENCY;
		double dT = Math.sqrt(drr / dSampling);//sqrt((double)drr/ (double)dSampling);

		if(dT != 0)
			nInterval[3] = (short)(((float)1000 * (float)fQt)/(float)dT);
		else
			nInterval[3] = 0;
		
	}
	
	void Measure_axis()
	{
		double pi = 3.1415926535;
		double ldTanInv = 0;

		int nPAxis = 0;

		float fP_I_amp = (float)((float)(siPPlusAmplitude[0] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT)) + (float)(siPNegAmplitude[0]) * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT));
		float fP_aVF_amp = (float)((float)(siPPlusAmplitude[5] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT)) + (float)(siPNegAmplitude[5]) * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT));

		//Amplitude in mV = fAmplitude/AMPLIFIER_GAIN
		fP_I_amp = (float)((fP_I_amp / (float)AMPLIFIER_GAIN) * 1000);
		fP_aVF_amp = (float)((fP_aVF_amp / (float)AMPLIFIER_GAIN) * 1000);

		if(fP_I_amp != 0)
		{
			ldTanInv = Math.atan((double) fP_aVF_amp / (double) fP_I_amp);
			nPAxis = (int)((double)ldTanInv * 180 / (double) pi);
		}

		//Added for testing-Dr. Chandorkar
		if(fP_I_amp == 0)
		{
			if(fP_aVF_amp > 0)
				nPAxis = 90;
			else
				nPAxis = -90;
		}
		if(fP_I_amp < 0)
			nPAxis = 180 + nPAxis;

		nAxis[P_AXIS] = nPAxis;

		// QRS Axis calculations
		int nQRSAxis = 0;
		ldTanInv = 0;
//		siQRSAmplitude[0]=344;
//		siQRSAmplitude[5]=248;

		// Added in version 3.0 on 12-05-2008 for bodylevel amplifier

		//float fQRSAmpOfLeadI = (float)(siQRSAmplitude[LEAD_I][nTrendIndex] * 0.00244);
		//float fQRSAmpOfLeadAVF = (float)(siQRSAmplitude[LEAD_aVF][nTrendIndex] * 0.00244);
		float fQRSAmpOfLeadI = (float)(siQRSAmplitude[0] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT));
		float fQRSAmpOfLeadAVF = (float)(siQRSAmplitude[5] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT));
		//Added on 28-05-2008
		//Amplitude in mV = fAmplitude/AMPLIFIER_GAIN
		fQRSAmpOfLeadI = (float)((fQRSAmpOfLeadI / (float)AMPLIFIER_GAIN) * 1000);
		fQRSAmpOfLeadAVF = (float)((fQRSAmpOfLeadAVF / (float)AMPLIFIER_GAIN) * 1000);

//		fQRSAmpOfLeadI = -0.80;
//		fQRSAmpOfLeadAVF = 2.83;
		//----------------$$$----------------------------------------
		if(fQRSAmpOfLeadI != 0)
		{
//			ldTanInv = tanl((long double)(fQRSAmpOfLeadAVF)/(long double)fQRSAmpOfLeadI);
			ldTanInv = Math.atan((double) (fQRSAmpOfLeadAVF) / (double) fQRSAmpOfLeadI);
			nQRSAxis = (int)((double)ldTanInv * 180 / (double) pi);
		}
		//*****************************************************************
		//Added for testing-Dr. Chandorkar
		if(fQRSAmpOfLeadI == 0)
		{
			if(fQRSAmpOfLeadAVF > 0)
				nQRSAxis = 90;
			else
				nQRSAxis = -90;
		}
		if(fQRSAmpOfLeadI < 0)
			nQRSAxis = 180 + nQRSAxis;
		//******************************************************************

		nAxis[QRS_AXIS] = nQRSAxis;

		// T Axis calculations
		int nTAxis = 0;
		ldTanInv = 0; 
//		siTAmplitude[5]=-186;
//		siTAmplitude[0]=83;
		// Added in version 3.0 on 12-05-2008 for bodylevel amplifier
		//float fTAmpOfLeadAVF  = (float)(siTAmplitude[LEAD_aVF][nTrendIndex] * 0.00244);
		//float fTAmpOfLeadI = (float)(siTAmplitude[LEAD_I][nTrendIndex] * 0.00244);

		float fTAmpOfLeadAVF  = (float)(siTAmplitude[5] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT));
		float fTAmpOfLeadI = (float)(siTAmplitude[0] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT));

		//Added on 28-05-2008
		//Amplitude in mV = fAmplitude/AMPLIFIER_GAIN
		fTAmpOfLeadAVF = (float)((fTAmpOfLeadAVF / (float)AMPLIFIER_GAIN) * 1000);
		fTAmpOfLeadI = (float)((fTAmpOfLeadI / (float)AMPLIFIER_GAIN) * 1000);

//		fTAmpOfLeadI = 0.05;
//		fTAmpOfLeadAVF = -0.07;
		//----------------$$$----------------------------------------
		if(fTAmpOfLeadI != 0)
		{
			ldTanInv = Math.atan((double) (fTAmpOfLeadAVF) / (double) (fTAmpOfLeadI));
			nTAxis = (int)((double)ldTanInv * 180 / (double) pi);
		}
		//Added for testing-Dr. Chandorkar
		if(fTAmpOfLeadI == 0)
		{
			if(fTAmpOfLeadAVF > 0)
				nTAxis = 90;
			else
				nTAxis = -90;
		}
		if(fTAmpOfLeadI < 0)
			nTAxis = 180 + nTAxis;

		nAxis[T_AXIS] = nTAxis;





	}
	void Get_Amplitudes_in_millivolts(float fAmplitude[] ,short nChannel_No)
	{
		fAmplitude[0] = (float)(siAmp[nChannel_No][P_PLUS_AMP] - siAmp[nChannel_No][P_NEG_AMP]) * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		fAmplitude[1] = (float)siAmp[nChannel_No][Q_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		fAmplitude[2] = (float)siAmp[nChannel_No][R_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		fAmplitude[3] = (float)siAmp[nChannel_No][S_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		fAmplitude[4] = (float)siAmp[nChannel_No][R_DASH_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		fAmplitude[5] = (float)siAmp[nChannel_No][S_DASH_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		fAmplitude[6] = (float)siAmp[nChannel_No][T_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		fAmplitude[7] = (float)siAmp[nChannel_No][U_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);
		fAmplitude[8] = (float)siAmp[nChannel_No][QRS_AMP] * (float)(ADC_VOLTAGE_RANGE / MAX_BITCOUNT);

		//Added on 28-05-2008
		//Amplitude in mV = fAmplitude/AMPLIFIER_GAIN
		
		for(int nAmp = 0; nAmp < TOTAL_AMPLITUDE_POINTS; nAmp++)
		{
	//vnp 27 may 16		fAmplitude[nAmp] = (float)((Dispaly_Amplitude_Buffer[nAmp] / (float)AMPLIFIER_GAIN) * 1000);
			fAmplitude[nAmp] = (float)((fAmplitude[nAmp] / (float)AMPLIFIER_GAIN) * 1000);

		}

		
	}

	void Calculate_Interpretation (short nchannel_no)
//vnp 5 july 16	void Calculate_Interpretation ()
	{
		strInterpretation_Status = "";
		for(int i=0; i<18; i++) {
//			nInterpretation_Status[i] = 0;
			nInterpretation_Status[i] = 9;	//bmp 28 dec 16
		}
		NoOfabnormalities=0; //vnp 22 march 16
		bOtherwiseNormal=false;//vnp 27 may 16
		float []fAmp = new float[TOTAL_AMPLITUDE_POINTS];
		//vnp 5 july 16 short nchannel_no =1;
		
		Get_Amplitudes_in_millivolts(fAmp, nchannel_no);
		if(fAmp[P_PLUS_AMP] > (0.0))
		{
			if((current_HR_copy >= 60) && (current_HR_copy <= 99))
			{
				// "Sinus Rhythm";
				nInterpretation_Status[0]=0; //vnp 26 nov 15
				strInterpretation_Status = strInterpretation_Status.concat("Sinus Rhythm. ");
			}
			else if (current_HR_copy < 60)
			{
				// "Sinus Bradycardia";
				nInterpretation_Status[0]=1; //vnp 26 nov 15
				strInterpretation_Status = strInterpretation_Status.concat("Sinus Bradycardia. ");
				bOtherwiseNormal=true; //vnp 22 march 16
			}
			else if(current_HR_copy > 99)
			{
				//"Sinus tachycardia";
				nInterpretation_Status[0]=2; //vnp 26 nov 15
				strInterpretation_Status = strInterpretation_Status.concat("Sinus tachycardia. ");
				bOtherwiseNormal=true;
			}
		}
		else
		{
			// "Junctional/NonSinus Rhythm suspected";
			nInterpretation_Status[0]=3; //vnp 26 nov 15
			strInterpretation_Status = strInterpretation_Status.concat("Junctional/NonSinus Rhythm suspected. ");
			NoOfabnormalities++;
		}
		//Check PR interval

		Display_Interval_in_millisecs(nchannel_no); //vnp 27 may 16
		if(nInterval[1] < 120)
		{
		/*	strPRIntervalStatement = "PR is short";
			nNoOfabnormalities++;
			bNormalPRInterval = false;
			strNotNormal += strPRIntervalStatement + ", ";*/
			nInterpretation_Status[1]=0; //vnp 26 nov 15
			strInterpretation_Status = strInterpretation_Status.concat("PR is short. ");
			NoOfabnormalities++;
		
		}
		else if(nInterval[1] > 200)
		{
		/*	strPRIntervalStatement = "PR is long";
			nNoOfabnormalities++;
			bNormalPRInterval = false;
			strNotNormal += strPRIntervalStatement + ", ";*/

			nInterpretation_Status[1]=1; //vnp 26 nov 15
			strInterpretation_Status = strInterpretation_Status.concat("PR is long. ");
			NoOfabnormalities++;
		}
		else
		{
			/*strPRIntervalStatement = "PR is normal";
			strNormal += strPRIntervalStatement + ", ";*/
			nInterpretation_Status[1]=2; //vnp 26 nov 15
			strInterpretation_Status = strInterpretation_Status.concat("PR is normal. ");

		}

		//Check QRS width
//		bNormalQRSWidth = true;
		if(nInterval[2] > 100)
		{
		/*	strQRSWidthStatement = "Wide QRS";
			nNoOfabnormalities++;
			bNormalQRSWidth = false;
			strNotNormal += strQRSWidthStatement + ", ";*/
			nInterpretation_Status[2]=0; //vnp 26 nov 15
			strInterpretation_Status = strInterpretation_Status.concat("Wide QRS. ");
			NoOfabnormalities++;

		}
		else
		{
		/*	strQRSWidthStatement = "Normal QRS Width";
			strNormal += strQRSWidthStatement + ", ";*/
			nInterpretation_Status[2]=1; //vnp 26 nov 15
			strInterpretation_Status = strInterpretation_Status.concat("Normal QRS Width. ");
		}
		//Check QT interval if HR < 100
		
	//vnp 22 april 13	if(current_HR < 100)
	//vnp 22 april 13	{
		/*	int nQTInterval = nNewFiducialPoints[T_END] - nNewFiducialPoints[Q_START]; 
			//nRRInterval = GetRRIntervalForGivenTrendIndex(nCurrentTrendIndex);

			if(nQTInterval > (nRRInterval[nCurrentTrendIndex]/2))
			{
				strQTIntervalStatement = "QT Interval is prolonged";
				nNoOfabnormalities++;
				bNormalQTInterval = false;
				strNotNormal += strQTIntervalStatement + ", ";
			}
			else
			{
				strQTIntervalStatement = "Normal QT interval";
				strNormal += strQTIntervalStatement + ", ";
			}*/
	//vnp 22 april 13		if(QT_Interval > (RR_Interval/2))//(nDuration1> (RR_Interval/2))
			if( nInterval[3] > 480)
			{
				//	Line_Interpretation[3][i_cnt]=QTinterval_msg1[i_cnt];
				NoOfabnormalities++;
				nInterpretation_Status[3]=0; //vnp 26 nov 15
				strInterpretation_Status = strInterpretation_Status.concat("QT Interval is prolonged. ");
			}
			else
			{
//					Line_Interpretation[3][i_cnt]=QTinterval_msg2[i_cnt];
				nInterpretation_Status[3]=1; //vnp 26 nov 15
				strInterpretation_Status = strInterpretation_Status.concat("Normal QT interval. ");
			}
	//vnp 22 april 13	}
	//vnp 22 april 13	else
	//vnp 22 april 13	{
	/*		strQTIntervalStatement = "QT Interval - Little clinical significance";
			strNormal += strQTIntervalStatement + ", ";*/
	//vnp 22 april 13			for(i_cnt=0;i_cnt<43;i_cnt++)
	//vnp 22 april 13			{
	//vnp 22 april 13				Line_Interpretation[3][i_cnt]=QTinterval_msg3[i_cnt];
	//vnp 22 april 13			}

	//vnp 22 april 13	}
	  //QRS Axis
		if((nAxis[1] > 90) && (nAxis[1] < 180))
		{
		/*	strQRSAxisStatement = "Right axis deviation";
			nNoOfabnormalities++;
			bNormalQRSAxis = false;
			strNotNormal += strQRSAxisStatement + ", ";
		*/
			nInterpretation_Status[4]=0; //vnp 26 nov 15
			strInterpretation_Status = strInterpretation_Status.concat("Right axis deviation. ");
			NoOfabnormalities++;

		}
		//else if((nAxes[1] < 0) && (nAxes[1] > (-90)))
		else if((nAxis[1] < (-15)) && (nAxis[1] > (-90))) //Modified as per Dr. Chandorkar's input 18-12-2007
		{
		/*	strQRSAxisStatement = "Left axis deviation";
			nNoOfabnormalities++;
			bNormalQRSAxis = false;
			strNotNormal += strQRSAxisStatement + ", ";
		*/
			nInterpretation_Status[4]=1; //vnp 26 nov 15
			strInterpretation_Status = strInterpretation_Status.concat("Left axis deviation. ");
			NoOfabnormalities++;
		}
		else if((nAxis[1] > 180) && (nAxis[1] < 270))
		{
		/*	strQRSAxisStatement = "QRS Axis is indeterminate";
			nNoOfabnormalities++;
			bNormalQRSAxis = false;
			strNotNormal += strQRSAxisStatement + ", ";
		*/
			nInterpretation_Status[4]=2; //vnp 26 nov 15
			strInterpretation_Status = strInterpretation_Status.concat("QRS Axis is indeterminate. ");
			NoOfabnormalities++;
		}
		else
		{
		/*	strQRSAxisStatement = "QRS Axis is normal";
			strNormal += strQRSAxisStatement + ", ";
		*/
			nInterpretation_Status[4]=3; //vnp 26 nov 15
			strInterpretation_Status = strInterpretation_Status.concat("QRS Axis is normal. ");
		}
		//T wave inversion added on 18-12-2007

		short j_cnt=0;
		short i_cnt=0;

		String strTWaveStatement = "";

		if(siTAmplitude_inv[0] < (float)0.0) //T wave is always upright in leads I, II, V3-6
		{
		//	strTWaveStatement += "I, ";
		//	bNormalTWave = false;
		//	nNoOfabnormalities++;//
			bTWave_Inversion_flag=true;
			//vnp 15 feb 13 	NoOfabnormalities++;
			bOtherwiseNormal=true; //vnp 15 feb 13
			nInterpretation_Status[5]=1; //vnp 26 nov 15
			if(strTWaveStatement.length() == 0)
				strTWaveStatement = strTWaveStatement.concat("I");
			else
				strTWaveStatement = strTWaveStatement.concat(", I");
		}
		if(siTAmplitude_inv[1] < (float)0.0) //T wave is always upright in leads I, II, V3-6
		{
		//	strTWaveStatement += "II, ";
		//	bNormalTWave = false;
		//	nNoOfabnormalities++;//
			bTWave_Inversion_flag=true;
			//vnp 11 dec 12 NoOfabnormalities++;
			bOtherwiseNormal=true;  //vnp 11 dec 12
			nInterpretation_Status[6]=1; //vnp 26 nov 15
			if(strTWaveStatement.length() == 0)
				strTWaveStatement = strTWaveStatement.concat("II");
			else
				strTWaveStatement = strTWaveStatement.concat(", II");
		}
	/* //vnp 22 april 13	if(siTAmplitude_inv[2] < (float)0.0)
		{
			//strTWaveStatement += "III, ";
		//	bNormalTWave = false;
		//	bOtherwiseNormal = true;//
			TWave_Inversion_flag=1;
			for(i_cnt=0;i_cnt<5;i_cnt++)
			{
				Line_Interpretation[6][j_cnt]=TWave_msg4[i_cnt];
				j_cnt++;
			}
			OtherwiseNormal=1;
		}  
	*/
		if(siTAmplitude_inv[3] >= (float)0.0) //T wave is always inverted in aVR
		{
			//strTWaveStatement += "aVR, ";
		//	nNoOfabnormalities ++;//
			bTWave_Inversion_flag=true;
			//vnp 11 dec 12 NoOfabnormalities++;
			bOtherwiseNormal=true;  //vnp 11 dec 12
			nInterpretation_Status[8]=1; //vnp 26 nov 15
			if(strTWaveStatement.length() == 0)
			    strTWaveStatement = strTWaveStatement.concat("aVR");
            else
                strTWaveStatement = strTWaveStatement.concat(", aVR");
		}

	/* //vnp 22 april 13	if(siTAmplitude_inv[4] < (float)0.0)
		{
		//	strTWaveStatement += "aVL, ";
		//	bNormalTWave = false;
		//	bOtherwiseNormal = true;//
			TWave_Inversion_flag=1;
			for(i_cnt=0;i_cnt<5;i_cnt++)
			{
				Line_Interpretation[6][j_cnt]=TWave_msg6[i_cnt];
				j_cnt++;
			}
			OtherwiseNormal=1;
		}
		if(siTAmplitude_inv[5] < (float)0.0)
		{
		//	strTWaveStatement += "aVF, ";
		//	bNormalTWave = false;
		//	bOtherwiseNormal = true;//
			TWave_Inversion_flag=1;
			for(i_cnt=0;i_cnt<5;i_cnt++)
			{
				Line_Interpretation[6][j_cnt]=TWave_msg7[i_cnt];
				j_cnt++;
			}
			OtherwiseNormal=1;
		}
		if(siTAmplitude_inv[6] < (float)0.0)
		{
		//	strTWaveStatement += "V1, ";
		//	bNormalTWave = false;
		//	bOtherwiseNormal = true;//
			TWave_Inversion_flag=1;
			for(i_cnt=0;i_cnt<4;i_cnt++)
			{
				Line_Interpretation[6][j_cnt]=TWave_msg8[i_cnt];
				j_cnt++;
			}
			OtherwiseNormal=1;
		}
		if(siTAmplitude_inv[7] < (float)0.0)
		{
		//	strTWaveStatement += "V2, ";
		//	bNormalTWave = false;
		//	bOtherwiseNormal = true;//
			TWave_Inversion_flag=1;
			for(i_cnt=0;i_cnt<4;i_cnt++)
			{
				Line_Interpretation[6][j_cnt]=TWave_msg9[i_cnt];
				j_cnt++;
			}
			OtherwiseNormal=1;
		}
	*/
		if(siTAmplitude_inv[8] < (float)0.0) //T wave is always upright in leads I, II, V3-6
		{
		///	strTWaveStatement += "V3, ";
		//	bNormalTWave = false;
		//	nNoOfabnormalities++;//
			bTWave_Inversion_flag=true;
			//vnp 11 dec 12 NoOfabnormalities++;
			bOtherwiseNormal=true;  //vnp 11 dec 12
			nInterpretation_Status[13]=1; //vnp 26 nov 15
			if(strTWaveStatement.length() == 0)
			    strTWaveStatement = strTWaveStatement.concat("V3");
            else
                strTWaveStatement = strTWaveStatement.concat(", V3");
		}
		if(siTAmplitude_inv[9] < (float)0.0) //T wave is always upright in leads I, II, V3-6
		{
		//	strTWaveStatement += "V4, ";
		//	bNormalTWave = false;
		//	nNoOfabnormalities++;
			bTWave_Inversion_flag=true;
			//vnp 11 dec 12 NoOfabnormalities++;
			bOtherwiseNormal=true;  //vnp 11 dec 12
			nInterpretation_Status[14]=1; //vnp 26 nov 15
			if(strTWaveStatement.length() == 0)
			    strTWaveStatement = strTWaveStatement.concat("V4");
            else
                strTWaveStatement = strTWaveStatement.concat(", V4");
		}
		if(siTAmplitude_inv[10] < (float)0.0) //T wave is always upright in leads I, II, V3-6
		{
		//	strTWaveStatement += "V5, ";
		//	bNormalTWave = false;
		//	nNoOfabnormalities++;
			bTWave_Inversion_flag=true;
			//vnp 11 dec 12 NoOfabnormalities++;
			bOtherwiseNormal=true;  //vnp 11 dec 12
			nInterpretation_Status[15]=1; //vnp 26 nov 15
			if(strTWaveStatement.length() == 0)
			    strTWaveStatement = strTWaveStatement.concat("V5");
            else
                strTWaveStatement = strTWaveStatement.concat(", V5");
		}
		if(siTAmplitude_inv[11] < (float)0.0) //T wave is always upright in leads I, II, V3-6
		{
//			strTWaveStatement += "V6, ";
//			bNormalTWave = false;
//			nNoOfabnormalities++;
			bTWave_Inversion_flag=true;
			//vnp 11 dec 12 NoOfabnormalities++;
			bOtherwiseNormal=true;  //vnp 11 dec 12
			nInterpretation_Status[16]=1; //vnp 26 nov 15
			if(strTWaveStatement.length() == 0)
			    strTWaveStatement = strTWaveStatement.concat("V6");
            else
                strTWaveStatement = strTWaveStatement.concat(", V6");
		}

		if(!strTWaveStatement.equalsIgnoreCase("")) {
			strInterpretation_Status = strInterpretation_Status.concat("T wave inversion in leads ");
			strInterpretation_Status = strInterpretation_Status.concat(strTWaveStatement);
			strInterpretation_Status = strInterpretation_Status.concat(".");
		}
		if(NoOfabnormalities==0) //vnp 22 march 16
		{
			if(bOtherwiseNormal==false) //vnp 4 july 16 true)
			{
				nInterpretation_Status[17]=0;
//				strInterpretation_Status = strInterpretation_Status.concat(" Normal ECG.");
			}
			else {
				nInterpretation_Status[17]=1;
//				strInterpretation_Status = strInterpretation_Status.concat(" Otherwise normal but can refer to cardiologist.");
			}
		}
		else{
			nInterpretation_Status[17]=2;
//			strInterpretation_Status = strInterpretation_Status.concat(" ECG not normal please refer to cardiologist.");
		}


	}

	
	
}

