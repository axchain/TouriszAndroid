package com.tourisz.util.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.tourisz.Application;
import com.tourisz.R;
import com.tourisz.api.response.Button;
import com.tourisz.api.response.UISettings;
import com.tourisz.api.response.UserData;
import com.tourisz.util.Constant;
import com.tourisz.util.Constants;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import top.defaults.drawabletoolbox.DrawableBuilder;

import static com.tourisz.Application.resources;


public class AndroidHelper {


    /**
     * This function opens an snack bar with the given reason
     *
     * @param view
     * @param reason
     */
    public static void openSnackBar(View view, String reason) {
        Snackbar snackbar = Snackbar.make(view, reason, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * This function stores the data in the shared preferences based on the given Key and Value
     *
     * @param context
     * @param key
     * @param value
     * @return string which is a key
     */
    public static String addSharedPreference(Context context, String key, String value) {
        SharedPreferences sharedPref = context
                .getSharedPreferences(Constants.Companion.getAPP_NAME(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
        return key;
    }

    /**
     * This function fetch the value from the shared preferences based on the key
     *
     * @param context
     * @param key
     * @return string value for the key
     */
    public static String getSharedPreferenceString(Context context, String key) {
        SharedPreferences sharedPref = null;
        if (context != null) {
            sharedPref = context.getSharedPreferences(Constants.Companion.getAPP_NAME(), Context.MODE_PRIVATE);
        }
        return sharedPref.getString(key, "");
    }


    public static UserData getUser(Context context) {
        String u = getSharedPreferenceString(context, Constants.Companion.getUSER_DATA());
        UserData userData = new UserData();
        if (!TextUtils.isEmpty(u)){
             userData = new Gson().fromJson(u, UserData.class);
        }
        return userData;
    }

    /**
     * Function to check network is available or not
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() /*&& internetConnectionAvailable(1000)*/;
    }


    public static boolean internetConnectionAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        if (inetAddress != null) {
            Log.e("net", inetAddress.getHostAddress());
        }
        return inetAddress != null && !inetAddress.equals("") && !inetAddress.getHostAddress().equals("10.0.0.1");
    }

    /**
     * This function extract the error based on the VolleyError
     *
     * @param context
     * @param error
     * @return a error string
     */
    public static String getVolleyError(Context context, VolleyError error) {
        if (error.networkResponse != null) {
            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return context.getString(R.string.server_error);
            }
            if (error instanceof TimeoutError) {
                return context.getString(R.string.time_our_error);
            } else if (error instanceof NoConnectionError && isNetworkAvailable(context)) {
                return context.getString(R.string.no_internet_available);
            } else if (error instanceof NoConnectionError && !isNetworkAvailable(context)) {
                return context.getString(R.string.can_not_connect);
            } else if (error instanceof AuthFailureError) {
                if (error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    String responseBody = null;
                    String srt = null;
                    try {
                        responseBody = new String(error.networkResponse.data,
                                "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        srt = jsonObject.get("error").toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return srt;
                }


                return context.getString(R.string.auth_fail);
            } else if (error instanceof ServerError) {
                if (error.networkResponse.statusCode != HttpURLConnection.HTTP_OK) {
                    String responseBody = null;
                    String srt = null;
                    try {
                        responseBody = new String(error.networkResponse.data,
                                "utf-8");
                        Log.e("server error", responseBody);
                        if (isValid(responseBody)) {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            srt = jsonObject.get("error").toString();
                        } else return responseBody;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return srt;
                }
                return context.getString(R.string.server_error);
            } else if (error instanceof NetworkError) {
                return context.getString(R.string.netwotk_error);
            } else if (error instanceof ParseError) {
                return context.getString(R.string.parser_error);
            }

            String msg = error.getMessage();

            if (msg != null && msg.trim().length() > 0) {
                return msg;
            }

            return context.getString(R.string.unknow_error_txt);
        } else {
            return context.getString(R.string.no_internet_available);
        }
    }


    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }


    /**
     * @param timeToDisplay
     * @return time string
     */
    public static String getTime(long timeToDisplay) {

        String timeTaken = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timeToDisplay),
                TimeUnit.MILLISECONDS.toMinutes(timeToDisplay) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.
                                toHours(timeToDisplay)),
                TimeUnit.MILLISECONDS.toSeconds(timeToDisplay) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes(timeToDisplay)));
        return timeTaken;
    }

    public static String getDays(long timeToDisplay) {
        String daysTaken = String.format("%02d",
                TimeUnit.MILLISECONDS.toDays(timeToDisplay));

        if (daysTaken.matches("01")) {
            return daysTaken + " Day";
        }
        return daysTaken + " Days";
    }



    public static Date getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( Constant.DATE_TIME_FORMAT, Locale.US);
        try {
            return simpleDateFormat.parse(  simpleDateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace( );
            return new Date(  );
        }
    }

    public static String getCurrentDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.SERVER_DATE_TIME_FORMAT, Locale.US);
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a", Locale.US);
        return simpleDateFormat.format(new Date());
    }


    // Function to get date from the timestamp
    public static Date getEventDate(String timeString) {
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_TIME_FORMAT, Locale.US);
        try {
            date = simpleDateFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertToAppDateFormat(String serverdate) {
        String appDate = "";

        try {
            Date date = new SimpleDateFormat(Constant.SERVER_DATE_FORMAT, Locale.US).parse(serverdate);
            appDate = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.US).format( date );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return appDate;
    }


    // Function to show toast with some extended time
    public static void showToast(Context context, String reason) {
        Toast.makeText(context.getApplicationContext(), reason, Toast.LENGTH_LONG).show();
    }




    public static void enableDisableEditText(EditText editText, boolean isEnable) {
        if (!isEnable) { // disable editing password
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            editText.setClickable(false); // user navigates with wheel and selects widget
            editText.setInputType(InputType.TYPE_NULL);
        } else { // enable editing of password
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setClickable(true);
            editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        }
    }


    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static String getCurrentWeekDay() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
// 3 letter name form of the day
        // System.out.println(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
// full name form of the day
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
    }

    public static String getRemainingTime(String endDateStr) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE HH:mm", Locale.ENGLISH);

        try {
            Date endDate = simpleDateFormat.parse(endDateStr);
            Date currentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));

            Log.e("end", simpleDateFormat.format(endDate));
            Log.e("curr", simpleDateFormat.format(currentDate));

            long diff = endDate.getTime() - currentDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffMinutes = diff / (60 * 1000) % 60;
            String result;
            if (diffDays > 0) {
                result = (diffHours + (24 * diffDays)) + ":" + diffMinutes;
            } else {
                result = diffHours + ":" + diffMinutes;
            }
            return result;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getTimeWithHrsMins(String time) {
        if (TextUtils.isEmpty(time) || !time.contains(":")) {
            return null;
        }

        String hrs = time.substring(0, time.indexOf(":"));
        String mins = time.substring(time.indexOf(":") + 1, time.length());

        return hrs + " Hours " + mins + " Minutes";

    }

    public static String objectToString(Object object){
        return object==null? "": new Gson().toJson(object);
    }


    public static String[] getImageArray(String images){
        String[] name = {images};


        if (TextUtils.isEmpty(images)) {
            return new String[0];
        }

        if (!images.contains(",")) {
            return name;
        }

        name = images.split(",");

        return name;
    }

    public static String[] getAddImageArray(String images){
        String[] name ;


        if (TextUtils.isEmpty(images)) {
            name = new String[]{ "add*"};
            return name;
        }

        if (!images.contains(",")) {
            name = new String[]{images, "add*"};
            return name;
        }

        images = images + ",add*";
        name = images.split(",");

        return name;
    }



    public static String getStatus(String status){
        switch (status){
            case "0": return resources.getString(R.string.inactive);
            case "1": return resources.getString(R.string.active);
            case "2": return resources.getString(R.string.deleted);
        }
        return "";
    }

    public static int getStatusId(String status){
        switch (status){
            case "Inactive": return 0;
            case "Active": return 1;
            case "Deleted": return 2;
        }
        return 0;
    }

    public static String getAvailability(String status){
        switch (status){
            case "0": return resources.getString(R.string.not_avail);
            case "1": return resources.getString(R.string.avail);
        }
        return "";
    }

    public static int getAvailabilityId(String status){
        switch (status){
            case "Not available": return 0;
            case "Available": return 1;
        }
        return 0;
    }

    public static int getRoomTypeId(String name){
        switch (name){
            case "Standard": return 1;
            case "Deluxe": return 2;
            case "Suite": return 3;
        }
        return 1;
    }

    public static String getRoomType(int id){
        switch (id){
            case 1: return "Standard";
            case 2: return "Deluxe";
            case 3: return "Suite";
        }
        return "Standard";
    }


    public static byte[] getFileData(String path) {
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

    public static String getFileNameFromPath(String path) {

        if (TextUtils.isEmpty(path))
            return "";


        return path.substring(path.lastIndexOf("/")+1, path.length());
    }



    public static Drawable getButtonShape(){
        String data = getSharedPreferenceString( Application.getInstance(), Constants.Companion.getUI_SETTINGS() );
        String startColor = "#F470F7";
        String endColor = "#9B8DF7";
        String centerColor = "#C77EF7";
        String angle = "360";

        if ( !TextUtils.isEmpty( data ) ) {
            UISettings uiSettings = new Gson( ).fromJson(data , UISettings.class );
            if ( uiSettings!=null ) {
                Button button = uiSettings.getButton( );
                if ( button != null ) {
                    startColor = button.getStartColor( );
                    endColor = button.getEndColor( );
                    centerColor = button.getCenterColor( );
                    angle = button.getAngle( );
                }
            }
        }

        return new DrawableBuilder().rectangle( )
                                    .rounded()
                                    .gradient()
                                    .linearGradient()
                                    .angle(Integer.parseInt( angle ))
                                    .startColor( Color.parseColor( startColor ) )
                                    .endColor(Color.parseColor(endColor))
                                    .centerColor(Color.parseColor(centerColor))
                                    .ripple()
                                    .build();
    }


    public static int getButtonHeight(){
        String data = getSharedPreferenceString( Application.getInstance(), Constants.Companion.getUI_SETTINGS() );
        int h = 45;

        if ( !TextUtils.isEmpty( data ) ) {
            UISettings uiSettings = new Gson( ).fromJson(data , UISettings.class );
            if ( uiSettings!=null ) {
                Button button = uiSettings.getButton( );
                if ( button != null ) {
                    h = Integer.parseInt(button.getHeight());

                }
            }
        }

        return dpToPx( h );
    }

    public static int getButtonWidth(){
        String data = getSharedPreferenceString( Application.getInstance(), Constants.Companion.getUI_SETTINGS() );
        int h = 300;

        if ( !TextUtils.isEmpty( data ) ) {
            UISettings uiSettings = new Gson( ).fromJson(data , UISettings.class );
            if ( uiSettings!=null ) {
                Button button = uiSettings.getButton( );
                if ( button != null ) {
                    h = Integer.parseInt(button.getWidth());

                }
            }
        }

        return dpToPx( h );
    }
    public static int dpToPx(int dp) {
        float density = Application.resources
                               .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }



    public static long getDayDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return elapsedDays;
    }
}
