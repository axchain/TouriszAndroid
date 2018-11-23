package com.tourisz.paytm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.tourisz.Application;
import com.tourisz.R;
import com.tourisz.api.request.BookEventRequest;
import com.tourisz.api.request.BookHotelRequest;
import com.tourisz.api.request.Object;
import com.tourisz.api.response.VerifyPaymentResponse;
import com.tourisz.api.util.JsonObjectRequestWithHeader;
import com.tourisz.util.Constant;
import com.tourisz.util.Constants;
import com.tourisz.util.helper.AndroidHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

    private Context mContext;
    private BookHotelRequest bookHotelRequest = null;
    private BookEventRequest bookEventRequest = null;
    private final String MID = "soften14395217357553";
    private String mOrderId;
    private String TAG = "PaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_payment );
        mContext = this;
        getWindow( ).setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );

        if ( !getIntent( ).hasExtra( "data" ) ) {
            errorFinish( "Error ! No booking data" );
        } else {
            Object object = ( Object ) getIntent( ).getSerializableExtra( "data" );
            if ( object == null ) {
                errorFinish( "Error ! No booking data" );
                return;
            }

            Random randomGenerator = new Random( );
            int randomInt = randomGenerator.nextInt( 1000000000 );
            mOrderId = "ORDER123" + randomInt;

            if ( object instanceof BookHotelRequest ) {
                bookHotelRequest = ( BookHotelRequest ) object;
                Log.e( "data", bookHotelRequest.toString( ) );
                paytmChecksumHash( );

            } else if ( object instanceof BookEventRequest ) {
                bookEventRequest = ( BookEventRequest ) object;
                Log.e( "data", bookEventRequest.toString( ) );
                paytmChecksumHash( );
            } else {
                errorFinish( "Error ! Invalid booking data" );
            }
        }
    }


    //This is to refresh the order id: Only for the Sample App's purpose.
    @Override
    protected void onStart() {
        super.onStart( );
        //initOrderId();
        getWindow( ).setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
    }


    /**
     * generate checksum
     */
    private void paytmChecksumHash() {

        final ProgressDialog progressDialog = ProgressDialog.show( mContext, "", "Loading..." );
        final JSONObject jsonObject = new JSONObject( );

        try {
            if ( bookHotelRequest != null ) {
                jsonObject.put( "CUST_ID", String.valueOf( bookHotelRequest.getUserId( ) ) );
                jsonObject.put( "TXN_AMOUNT", String.valueOf( bookHotelRequest.getTotal( ) ) );
            } else if ( bookEventRequest != null ) {
                jsonObject.put( "CUST_ID", String.valueOf( bookEventRequest.getUserId( ) ) );
                jsonObject.put( "TXN_AMOUNT", String.valueOf( bookEventRequest.getTotal( ) ) );
            } else {
                errorFinish( "Error ! Invalid booking data" );
                return;

            }

            jsonObject.put( "ORDER_ID", mOrderId );
            jsonObject.put( "CALLBACK_URL", Constant.PAYTM_URL_CALL_BACK + mOrderId );
            jsonObject.put( "MID", MID );
            jsonObject.put( "INDUSTRY_TYPE_ID", "Retail" );
            jsonObject.put( "CHANNEL_ID", "WAP" );
            jsonObject.put( "WEBSITE", "APPSTAGING" );
            jsonObject.put( "REQUEST_TYPE", "DEFAULT" );
            jsonObject.put( "THEME", "merchant" );

        } catch (JSONException e) {
            e.printStackTrace( );
        }
        Log.e( "paytm", "checksum gen:" + jsonObject.toString( ) );

        JsonObjectRequestWithHeader jsonObjReq = new JsonObjectRequestWithHeader( Request.Method.POST,
                                                                                  Constant.URL_PAYTM_GENERATECHECKSUM, jsonObject,
                                                                                  new Response.Listener<JSONObject>( ) {
                                                                                      @Override
                                                                                      public void onResponse(JSONObject response) {

                                                                                          try {
                                                                                              progressDialog.dismiss( );
                                                                                              String checksumhash = response.getString( "CHECKSUMHASH" );
                                                                                              Log.e( TAG, "hash:" + response.toString( ) );
                                                                                              onStartTransaction( checksumhash );

                                                                                          } catch (Exception e) {
                                                                                              e.printStackTrace( );
                                                                                          }
                                                                                      }
                                                                                  }, new Response.ErrorListener( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss( );
                Log.e( TAG, "Error: " + error.getMessage( ) );
                errorFinish( "Error: " + error.getMessage( ) );

            }
        }
        );

        Application.getInstance( ).addToRequestQueue( jsonObjReq );

    }


    /**
     * @param checksumhash
     */
    public void onStartTransaction(final String checksumhash) {

        PaytmPGService Service = PaytmPGService.getStagingService( );

        Log.e( "test", "orderId:" + mOrderId );
        Log.e( "test", "checksum:" + checksumhash );

        Map<String, String> paramMap = new HashMap<String, String>( );
        if ( bookHotelRequest != null ) {
            paramMap.put( "CUST_ID", String.valueOf( bookHotelRequest.getUserId( ) ) );
            paramMap.put( "TXN_AMOUNT", String.valueOf( bookHotelRequest.getTotal( ) ) );
        } else if ( bookEventRequest != null ) {
            paramMap.put( "CUST_ID", String.valueOf( bookEventRequest.getUserId( ) ) );
            paramMap.put( "TXN_AMOUNT", String.valueOf( bookEventRequest.getTotal( ) ) );
        } else {
            errorFinish( "Error ! Invalid booking data" );
            return;

        }

        paramMap.put( "MID", MID );
        paramMap.put( "ORDER_ID", mOrderId );
        paramMap.put( "INDUSTRY_TYPE_ID", "Retail" );
        paramMap.put( "CHANNEL_ID", "WAP" );
        paramMap.put( "WEBSITE", "APPSTAGING" );
        paramMap.put( "CALLBACK_URL", Constant.PAYTM_URL_CALL_BACK + mOrderId );
        paramMap.put( "CHECKSUMHASH", checksumhash );
        paramMap.put( "REQUEST_TYPE", "DEFAULT" );
        paramMap.put( "THEME", "merchant" );

        Log.e( "req", paramMap.toString( ) );

        PaytmOrder Order = new PaytmOrder( paramMap );

        Service.initialize( Order, null );

        Service.startPaymentTransaction( this, true, true,
                                         new PaytmPaymentTransactionCallback( ) {

                                             @Override
                                             public void someUIErrorOccurred(String inErrorMessage) {

                                                 errorFinish( "Payment Transaction Failed " + inErrorMessage );
                                             }

                                             @Override
                                             public void onTransactionResponse(Bundle bundle) {
                                                 //success bundle
                                                 // Bundle[{STATUS=TXN_SUCCESS, CHECKSUMHASH=RTPekDMsLkXnO/vhkrzga2EFLM9xl9vhdVj29ELjzNx/FTXBhTfM1TDv73o2MmVZWy9MosBTGeJddcVT/BnBCthvjE7MizjuYkLT/zE87fk=, BANKNAME=, ORDERID=ORDER123793405329, TXNAMOUNT=5.00, TXNDATE=2018-09-11 14:18:58.0, MID=soften14395217357553, TXNID=70001065702, RESPCODE=01, PAYMENTMODE=PPI, BANKTXNID=1287546, CURRENCY=INR, GATEWAYNAME=WALLET, RESPMSG=Txn Successful.}]
                                                 try {
                                                     Log.e( "onTransactionResponse", bundle.toString( ) );

                                                     if ( bundle.containsKey( "IS_CHECKSUM_VALID" ) && bundle.getString( "IS_CHECKSUM_VALID" ).equalsIgnoreCase( "N" ) ) {

                                                         errorFinish( "Checksum fail" );

                                                     } else {

                                                         verifyPayment( );

                                                     }


                                                 } catch (Exception e) {
                                                     e.printStackTrace( );
                                                     errorFinish( e.getMessage( ) );

                                                 }
                                             }

                                             @Override
                                             public void networkNotAvailable() {
                                                 errorFinish( "No internet" );
                                             }

                                             @Override
                                             public void clientAuthenticationFailed(String inErrorMessage) {
                                                 errorFinish( "Payment Transaction Failed " + inErrorMessage );
                                             }

                                             @Override
                                             public void onErrorLoadingWebPage(int iniErrorCode,
                                                                               String inErrorMessage, String inFailingUrl) {
                                                 Log.e( "onErrorLoadingWebPage", inErrorMessage );
                                             }

                                             // had to be added: NOTE
                                             @Override
                                             public void onBackPressedCancelTransaction() {
                                                 // TODO Auto-generated method stub
                                                 errorFinish( "Back pressed. Transaction cancelled" );
                                             }

                                             @Override
                                             public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                                 Log.e( TAG, "Payment Transaction Failed " + inErrorMessage );
                                                 Toast.makeText( getBaseContext( ), "Payment Transaction Failed ", Toast.LENGTH_LONG ).show( );
                                                 errorFinish( "Payment Transaction Failed " + inErrorMessage );
                                             }

                                         }
                                       );
    }


    /**
     * {"MID":"MID","ORDERID":"ORDERID","CHECKSUMHASH":"CHECKSUMHASH"}
     */
    private void verifyPayment() {
        final ProgressDialog progressDialog = ProgressDialog.show( mContext, "", "Loading..." );
        final JSONObject jsonObject = new JSONObject( );
        try {
            if ( bookHotelRequest != null ) {
                jsonObject.put( "userId", String.valueOf( bookHotelRequest.getUserId( ) ) );
            } else if ( bookEventRequest != null ) {
                jsonObject.put( "userId", String.valueOf( bookEventRequest.getUserId( ) ) );
            } else {
                errorFinish( "Error ! Invalid booking data" );
                return;

            }
            jsonObject.put( "orderId", mOrderId );

        } catch (JSONException e) {
            e.printStackTrace( );
        }

        Log.e( TAG, "request json:" + jsonObject.toString( ) );
        JsonObjectRequestWithHeader jsonObjReq = new JsonObjectRequestWithHeader( Request.Method.POST,
                                                                                  Constant.URL_PAYTM_VERIFICATION, jsonObject,
                                                                                  new Response.Listener<JSONObject>( ) {
                                                                                      @Override
                                                                                      public void onResponse(JSONObject response) {
                                                                                          try {
                                                                                              progressDialog.dismiss( );

                                                                                              Log.e( "Response", response.toString( ) );
                                                                                              VerifyPaymentResponse verifyPaymentResponse = new Gson( ).fromJson( response.toString( ), VerifyPaymentResponse.class );
                                                                                              if ( verifyPaymentResponse != null && verifyPaymentResponse.getResult( ) ) {
                                                                                                  AndroidHelper.showToast( getApplicationContext( ), verifyPaymentResponse.getResponse( ) );
                                                                                                  if ( bookHotelRequest != null ) {
                                                                                                      bookHotelRequest.setPaymentTransactionId( verifyPaymentResponse.getTransaction( ).getTXNID( ) );
                                                                                                      //bookHotelRequest.setBookingCharges( ( int ) bookHotelRequest.getTotal( ) );
                                                                                                      successFinish( bookHotelRequest );
                                                                                                  } else if ( bookEventRequest != null ) {
                                                                                                      bookEventRequest.setPaymentTransactionId( verifyPaymentResponse.getTransaction( ).getTXNID( ) );
                                                                                                      //bookEventRequest.setBookingCharges( ( int ) bookEventRequest.getTotal( ) );

                                                                                                      successFinish( bookEventRequest );
                                                                                                  }
                                                                                              } else {

                                                                                                  errorFinish( verifyPaymentResponse == null ? "Verify Payment Error!" : verifyPaymentResponse.getResponse( ) );
                                                                                              }

                                                                                          } catch (Exception e) {
                                                                                              e.printStackTrace( );
                                                                                          }
                                                                                      }
                                                                                  }, new Response.ErrorListener( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss( );
                errorFinish( "Error: " + error.getMessage( ) );
                Log.e( TAG, "Error: " + error.getMessage( ) );
            }
        }
        );
        Application.getInstance( ).addToRequestQueue( jsonObjReq );

    }

    private void errorFinish(String error) {
        finish( );
        sendBroadcast( new Intent( Constants.Companion.getACTION_PAYMENT_FAIL( ) ).putExtra( "error", error ) );
    }

    private void successFinish(Object updatedObj) {
        finish( );
        sendBroadcast( new Intent( Constants.Companion.getACTION_PAYMENT_DONE( ) ).putExtra( "data", updatedObj ) );
    }


}
