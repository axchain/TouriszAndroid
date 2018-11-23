package com.tourisz.util;

public interface Constant {

    String URL_PAYTM_VERIFICATION = "http://ec2-18-188-157-92.us-east-2.compute.amazonaws.com/api/?r=Payment/VerifyPayment";
    //String URL_PAYTM_GENERATECHECKSUM = "http://appsplanet.co.in/tourisz/?r=Payment/GenerateChecksum";
    String URL_PAYTM_GENERATECHECKSUM = "http://ec2-18-188-157-92.us-east-2.compute.amazonaws.com/api/?r=Payment/GenerateChecksumDV";
    //String PAYTM_URL_CALL_BACK="http://appsplanet.co.in/tourisz/?r=Payment/VerifyChecksum";
    String PAYTM_URL_CALL_BACK = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=";

    String DATE_FORMAT = "dd/MM/yyyy";

    String TIME_FORMAT = "HH:mm:ss";

    String DATE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

    String SERVER_DATE_FORMAT = "yyyy-MM-dd";

    String SERVER_TIME_FORMAT = "HH:mm:ss";

    String SERVER_DATE_TIME_FORMAT = SERVER_DATE_FORMAT + " " + SERVER_TIME_FORMAT;

}
