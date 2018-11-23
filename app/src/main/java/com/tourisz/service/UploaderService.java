package com.tourisz.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.tourisz.Application;
import com.tourisz.R;
import com.tourisz.api.URLS;
import com.tourisz.api.request.AddFlyerRequest;
import com.tourisz.api.request.Object;
import com.tourisz.api.request.UpdateEventBookingRequest;
import com.tourisz.api.request.UpdateFlyerRequest;
import com.tourisz.api.request.UpdateHotelBookingRequest;
import com.tourisz.api.response.EventData;
import com.tourisz.api.response.HotelData;
import com.tourisz.api.util.JsonObjectRequestWithHeader;
import com.tourisz.entity.LocalFile;
import com.tourisz.util.Constants;
import com.tourisz.util.helper.AndroidHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.tourisz.service.UploaderService.type.ADD_EVENT;
import static com.tourisz.service.UploaderService.type.ADD_FLYER;
import static com.tourisz.service.UploaderService.type.ADD_HOTEL;
import static com.tourisz.service.UploaderService.type.UPDATE_BOOKED_EVENT;
import static com.tourisz.service.UploaderService.type.UPDATE_BOOKED_HOTEL;
import static com.tourisz.service.UploaderService.type.UPDATE_EVENT;
import static com.tourisz.service.UploaderService.type.UPDATE_FLYER;
import static com.tourisz.service.UploaderService.type.UPDATE_HOTEL;


public class UploaderService extends Service {

    private static final String TAG = UploaderService.class.getSimpleName( );


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private NotificationChannel mChannel;
    private NotificationManager notifManager;
    private int ongoing_noti_id = 1;
    private int progress_noti_id = 2;

    private String channelid = "touriszuploader";
    private Notification progress_notification;
    private NotificationCompat.Builder progress_builder;
    private ArrayList<LocalFile> localFiles = new ArrayList<>( );
    private HotelData hotelData = null;
    private EventData eventData = null;
    private UpdateHotelBookingRequest updateHotelBookingRequest = null;
    private UpdateEventBookingRequest updateEventBookingRequest = null;
    private AddFlyerRequest flyerRequest = null;
    private UpdateFlyerRequest updateFlyerRequest = null;

    private boolean isUpdate = false;

    public interface type {
        int ADD_HOTEL = 0;
        int ADD_EVENT = 1;
        int UPDATE_HOTEL = 2;
        int UPDATE_EVENT = 3;
        int UPDATE_BOOKED_HOTEL = 4;
        int UPDATE_BOOKED_EVENT = 5;
        int ADD_FLYER = 6;
        int UPDATE_FLYER = 7;

    }

    private int ADD_TYPE = ADD_HOTEL;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if ( notifManager == null ) {
            notifManager = ( NotificationManager ) getSystemService
                    ( Context.NOTIFICATION_SERVICE );
        }

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            NotificationCompat.Builder builder;

            if ( mChannel == null ) {
                mChannel = new NotificationChannel
                        ( channelid, "Uploading photos", NotificationManager.IMPORTANCE_DEFAULT );
                mChannel.setDescription( "Uploading photos" );
                notifManager.createNotificationChannel( mChannel );

            }

            builder = new NotificationCompat.Builder( this, channelid );
            builder.setContentTitle( "Uploading photos" )
                   .setTicker( "Uploading photos" )
                   .setSmallIcon( R.drawable.ic_notification )
                   .setChannelId( channelid )
                   .setOngoing( true )
                   .build( );

            progress_builder = new NotificationCompat.Builder( this, channelid );
            progress_builder.setContentTitle( "uploading.." )
                            .setTicker( "uploading.." )
                            .setSmallIcon( R.drawable.ic_notification )
                            .setChannelId( channelid )
                            .setOngoing( true )
                            .setVibrate(null )
                            .setProgress( 100, 0, false )
                            .setOnlyAlertOnce( true )
                            .build( );

         /*   Notification notification = builder.build();
            notifManager.notify(ongoing_noti_id, notification);
            startForeground(ongoing_noti_id,
                    notification);*/
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( this )
                    .setTicker( "Uploading photos" )
                    .setContentTitle( "Uploading photos" )
                    .setSmallIcon( R.drawable.ic_notification );

            notifManager.notify( ongoing_noti_id, notificationBuilder.build( ) );


            progress_builder = new NotificationCompat.Builder( this )
                    .setTicker( "uploading.." )
                    .setContentTitle( "uploading.." )
                    .setVibrate( new long[]{0L} )
                    .setOnlyAlertOnce( true )
                    .setSmallIcon( R.drawable.ic_notification );

            progress_builder.setProgress( 100
                    , 0, false );

        }

        if ( intent.hasExtra( "action" ) ) {
            isUpdate = intent.getBooleanExtra( "action", false );
        }
        if ( intent.hasExtra( "filepath" ) ) {
            localFiles = intent.getParcelableArrayListExtra( "filepath" );
            setObject( intent );
            uploadFile( );

        } else if ( intent.hasExtra( "path" ) ) {
            String path = intent.getStringExtra( "path" );
            setObject( intent );
            if ( TextUtils.isEmpty( path ) ){
                callAPI();
            }else {
                localFiles.add( new LocalFile( path, path, 0 ) );
                uploadFile( );
            }

        } else if ( intent.hasExtra( "data" ) ) {
            setObject( intent );
            callAPI( );
        }

        return START_NOT_STICKY;
    }


    private Object object;

    private void setObject(Intent intent) {
        object = ( Object ) intent.getSerializableExtra( "data" );

        if ( object instanceof HotelData ) {
            hotelData = ( HotelData ) object;
        } else if ( object instanceof EventData ) {
            eventData = ( EventData ) object;
        } else if ( object instanceof UpdateHotelBookingRequest ) {
            updateHotelBookingRequest = ( UpdateHotelBookingRequest ) object;
        }else if ( object instanceof UpdateEventBookingRequest ) {
            updateEventBookingRequest = ( UpdateEventBookingRequest ) object;
        }else if ( object instanceof AddFlyerRequest ) {
            flyerRequest = ( AddFlyerRequest ) object;
        }else if ( object instanceof UpdateFlyerRequest ) {
            updateFlyerRequest = ( UpdateFlyerRequest ) object;
        }

    }

    private String photos = "";



    @Override
    public void onDestroy() {
        super.onDestroy( );
        if ( isUpdate ) {
            sendBroadcast( new Intent( Constants.Companion.getACTION_DONE( ) )
                                   .putExtra( "type", ADD_TYPE )
                                   .putExtra( "obj", object ) );
        } else {
            sendBroadcast( new Intent( Constants.Companion.getACTION_DONE( ) )
                                   .putExtra( "type", ADD_TYPE ) );
        }
        if ( notifManager != null ) {
            notifManager.cancelAll( );
        }
    }


    private void callAPI() {
        if ( hotelData != null ) {
            Log.e( "req", hotelData.toString( ) );
            if ( isUpdate ) {
                ADD_TYPE = UPDATE_HOTEL;
                callAddHotelAPI( URLS.newInstance( ).AGENT_UPDATE_HOTEL( ) );
            } else {
                ADD_TYPE = ADD_HOTEL;
                callAddHotelAPI( URLS.newInstance( ).AGENT_ADD_HOTEL( ) );
            }
        } else if ( eventData != null ) {
            Log.e( "req", eventData.toString( ) );
            if ( isUpdate ) {
                ADD_TYPE = UPDATE_EVENT;
                callAddEventAPI( URLS.newInstance( ).AGENT_UPDATE_EVENT( ) );
            } else {
                ADD_TYPE = ADD_EVENT;
                callAddEventAPI( URLS.newInstance( ).AGENT_ADD_EVENT( ) );
            }
        } else if ( updateHotelBookingRequest != null ) {
            ADD_TYPE = UPDATE_BOOKED_HOTEL;
            callUpdateHotelBookingAPI( );

        } else if ( updateEventBookingRequest != null ) {
            ADD_TYPE = UPDATE_BOOKED_EVENT;
            callUpdateEventBookingAPI( );
        } else if ( flyerRequest != null ) {
            ADD_TYPE = ADD_FLYER;
            callAddFlyerAPI();
        }else if ( updateFlyerRequest != null ) {
            ADD_TYPE = UPDATE_FLYER;
            callUpdateFlyerAPI();
        }
    }



    private void uploadFile() {
        if ( localFiles.size( ) <= 0 ) {
            callAPI( );
            return;
        }


        SimpleMultiPartRequest smr = new SimpleMultiPartRequest( Request.Method.POST, URLS.newInstance( ).UPLOAD_IMAGES( ),
                                                                 new Response.Listener<String>( ) {
                                                                     @Override
                                                                     public void onResponse(String response) {
                                                                         //{"result":"true","response":"Images uploaded","photos":"1535468140.jpeg,1535468140.jpeg,1535468140.jpeg"}
                                                                         Log.e( "Response", response );
                                                                         try {
                                                                             JSONObject jsonObject = new JSONObject( response );
                                                                             if ( jsonObject.getBoolean( "result" ) ) {
                                                                                 Toast.makeText( getApplicationContext( ), jsonObject.getString( "response" ), Toast.LENGTH_LONG ).show( );
                                                                                 photos = jsonObject.getString( "photos" );
                                                                             }
                                                                             callAPI( );
                                                                         } catch (Exception e) {
                                                                             stopSelf( );
                                                                         }


                                                                     }
                                                                 }, new Response.ErrorListener( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText( getApplicationContext( ), "Image upload error!", Toast.LENGTH_LONG ).show( );
                notifManager.cancel( progress_noti_id );
                stopSelf( );

            }
        }
        );

        for (int i = 0; i < localFiles.size( ); i++) {
            smr.addFile( "image" + (i + 1), localFiles.get( i ).getPath( ) );
        }
        smr.setOnProgressListener( new Response.ProgressListener( ) {
            @Override
            public void onProgress(long transferredBytes, long totalSize) {
                int progress = ( int ) (( double ) transferredBytes * 100 / totalSize);

                if ( progress_builder != null ) {
                    if ( progress < 100 && progress > 0 ) {

                        progress_builder.setProgress( 100, progress, false );
                        progress_notification = progress_builder.build( );
                        notifManager.notify( progress_noti_id, progress_notification );

                    }
                }

            }
        } );

        Application.getInstance( ).addToRequestQueue( smr );

    }



    private void callAddHotelAPI(String url) {

        if ( hotelData != null ) {
            hotelData.setPoster( photos );
            Log.e( "req", AndroidHelper.objectToString( hotelData ) );
            try {
                JSONObject jsonObj = new JSONObject( AndroidHelper.objectToString( hotelData ) );
                JsonObjectRequestWithHeader jsonObjReq = new JsonObjectRequestWithHeader( Request.Method.POST,
                                                                                          url, jsonObj,
                                                                                          new Response.Listener<JSONObject>( ) {
                                                                                              @Override
                                                                                              public void onResponse(JSONObject response) {
                                                                                                  Log.e( "Response", response.toString( ) );

                                                                                                  try {
                                                                                                      Toast.makeText( getApplicationContext( ), response.getString( "response" ), Toast.LENGTH_LONG ).show( );
                                                                                                  } catch (Exception e) {
                                                                                                  }
                                                                                                  notifManager.cancel( progress_noti_id );
                                                                                                  stopSelf( );


                                                                                              }
                                                                                          }, new Response.ErrorListener( ) {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Toast.makeText( getApplicationContext( ), "Error", Toast.LENGTH_LONG ).show( );
                        notifManager.cancel( progress_noti_id );
                        stopSelf( );

                    }
                }
                );

                Application.getInstance( ).addToRequestQueue( jsonObjReq );
            } catch (JSONException e) {
                e.printStackTrace( );
            }
        }
    }


    private void callAddEventAPI(String url) {

        if ( eventData != null ) {
            eventData.setPoster( photos );
            Log.e( "req", AndroidHelper.objectToString( eventData ) );
            try {
                JSONObject jsonObj = new JSONObject( AndroidHelper.objectToString( eventData ) );
                JsonObjectRequestWithHeader jsonObjReq = new JsonObjectRequestWithHeader( Request.Method.POST,
                                                                                          url, jsonObj,
                                                                                          new Response.Listener<JSONObject>( ) {
                                                                                              @Override
                                                                                              public void onResponse(JSONObject response) {
                                                                                                  Log.e( "Response", response.toString( ) );

                                                                                                  try {
                                                                                                      Toast.makeText( getApplicationContext( ), response.getString( "response" ), Toast.LENGTH_LONG ).show( );
                                                                                                  } catch (Exception e) {
                                                                                                  }
                                                                                                  notifManager.cancel( progress_noti_id );
                                                                                                  stopSelf( );

                                                                                              }
                                                                                          }, new Response.ErrorListener( ) {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText( getApplicationContext( ), "Error", Toast.LENGTH_LONG ).show( );
                        notifManager.cancel( progress_noti_id );
                        stopSelf( );

                    }
                }
                );

                Application.getInstance( ).addToRequestQueue( jsonObjReq );
            } catch (JSONException e) {
                e.printStackTrace( );
            }
        }
    }


    private void callUpdateHotelBookingAPI() {

        if ( updateHotelBookingRequest != null ) {
            updateHotelBookingRequest.setAttachment( photos );
            Log.e( "req", updateHotelBookingRequest.toString( ) );

            try {
                JSONObject jsonObj = new JSONObject( AndroidHelper.objectToString( updateHotelBookingRequest ) );

                JsonObjectRequestWithHeader jsonObjReq = new JsonObjectRequestWithHeader( Request.Method.POST,
                                                                                          URLS.newInstance( ).UPDATE_BOOKED_HOTEL( ), jsonObj,
                                                                                          new Response.Listener<JSONObject>( ) {
                                                                                              @Override
                                                                                              public void onResponse(JSONObject response) {
                                                                                                  Log.e( "Response", response.toString( ) );

                                                                                                  try {
                                                                                                      Toast.makeText( getApplicationContext( ), response.getString( "response" ), Toast.LENGTH_LONG ).show( );
                                                                                                  } catch (Exception e) {
                                                                                                  }
                                                                                                  notifManager.cancel( progress_noti_id );
                                                                                                  stopSelf( );

                                                                                              }
                                                                                          }, new Response.ErrorListener( ) {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText( getApplicationContext( ), "Error", Toast.LENGTH_LONG ).show( );
                        notifManager.cancel( progress_noti_id );
                        stopSelf( );

                    }
                }
                );

                Application.getInstance( ).addToRequestQueue( jsonObjReq );
            } catch (JSONException e) {
                e.printStackTrace( );
            }
        }
    }

    private void callUpdateEventBookingAPI() {

        if ( updateEventBookingRequest != null ) {
            updateEventBookingRequest.setAttachment( photos );
            Log.e( "req", updateEventBookingRequest.toString( ) );

            try {
                JSONObject jsonObj = new JSONObject( AndroidHelper.objectToString( updateEventBookingRequest ) );

                JsonObjectRequestWithHeader jsonObjReq = new JsonObjectRequestWithHeader( Request.Method.POST,
                                                                                          URLS.newInstance( ).UPDATE_BOOKED_EVENT( ), jsonObj,
                                                                                          new Response.Listener<JSONObject>( ) {
                                                                                              @Override
                                                                                              public void onResponse(JSONObject response) {
                                                                                                  Log.e( "Response", response.toString( ) );

                                                                                                  try {
                                                                                                      Toast.makeText( getApplicationContext( ), response.getString( "response" ), Toast.LENGTH_LONG ).show( );
                                                                                                  } catch (Exception e) {
                                                                                                  }
                                                                                                  notifManager.cancel( progress_noti_id );
                                                                                                  stopSelf( );

                                                                                              }
                                                                                          }, new Response.ErrorListener( ) {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText( getApplicationContext( ), "Error", Toast.LENGTH_LONG ).show( );
                        notifManager.cancel( progress_noti_id );
                        stopSelf( );

                    }
                }
                );

                Application.getInstance( ).addToRequestQueue( jsonObjReq );
            } catch (JSONException e) {
                e.printStackTrace( );
            }
        }
    }


    private void callAddFlyerAPI() {

        if ( flyerRequest != null ) {
            flyerRequest.setPoster( photos );
            Log.e( "req", flyerRequest.toString( ) );

            try {
                JSONObject jsonObj = new JSONObject( AndroidHelper.objectToString( flyerRequest ) );

                JsonObjectRequestWithHeader jsonObjReq = new JsonObjectRequestWithHeader( Request.Method.POST,
                                                                                          URLS.newInstance( ).CREATE_FLYER( ), jsonObj,
                                                                                          new Response.Listener<JSONObject>( ) {
                                                                                              @Override
                                                                                              public void onResponse(JSONObject response) {
                                                                                                  Log.e( "Response", response.toString( ) );

                                                                                                  try {
                                                                                                      Toast.makeText( getApplicationContext( ), response.getString( "response" ), Toast.LENGTH_LONG ).show( );
                                                                                                  } catch (Exception e) {
                                                                                                  }
                                                                                                  notifManager.cancel( progress_noti_id );
                                                                                                  stopSelf( );

                                                                                              }
                                                                                          }, new Response.ErrorListener( ) {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText( getApplicationContext( ), "Error", Toast.LENGTH_LONG ).show( );
                        notifManager.cancel( progress_noti_id );
                        stopSelf( );

                    }
                }
                );

                Application.getInstance( ).addToRequestQueue( jsonObjReq );
            } catch (JSONException e) {
                e.printStackTrace( );
            }
        }
    }


    private void callUpdateFlyerAPI() {

        if ( updateFlyerRequest != null ) {
            if ( !photos.isEmpty() ) {
                updateFlyerRequest.setPoster( photos );
            }
            Log.e( "req", AndroidHelper.objectToString(  updateFlyerRequest));

            try {
                JSONObject jsonObj = new JSONObject( AndroidHelper.objectToString( updateFlyerRequest ) );

                JsonObjectRequestWithHeader jsonObjReq = new JsonObjectRequestWithHeader( Request.Method.POST,
                                                                                          URLS.newInstance( ).UPDATE_FLYER( ), jsonObj,
                                                                                          new Response.Listener<JSONObject>( ) {
                                                                                              @Override
                                                                                              public void onResponse(JSONObject response) {
                                                                                                  Log.e( "Response", response.toString( ) );

                                                                                                  try {
                                                                                                      Toast.makeText( getApplicationContext( ), response.getString( "response" ), Toast.LENGTH_LONG ).show( );
                                                                                                  } catch (Exception e) {
                                                                                                  }
                                                                                                  notifManager.cancel( progress_noti_id );
                                                                                                  stopSelf( );

                                                                                              }
                                                                                          }, new Response.ErrorListener( ) {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText( getApplicationContext( ), "Error", Toast.LENGTH_LONG ).show( );
                        notifManager.cancel( progress_noti_id );
                        stopSelf( );

                    }
                }
                );

                Application.getInstance( ).addToRequestQueue( jsonObjReq );
            } catch (JSONException e) {
                e.printStackTrace( );
            }
        }
    }





}
