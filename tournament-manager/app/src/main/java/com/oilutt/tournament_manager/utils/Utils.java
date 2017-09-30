package com.oilutt.tournament_manager.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;

import com.desmond.squarecamera.CameraActivity;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.app.Constants;
import com.oilutt.tournament_manager.ui.activity.BaseActivity;
import com.oilutt.tournament_manager.ui.OnGetContacts;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by oilut on 21/08/2017.
 */

public class Utils {

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static void hideKeyboard(Context context, View view) {
        if (view == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideFocusEdt(AppCompatActivity context) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null || TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Object jsonToObject(String obj, Class<?> classModel) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();
        return gson.fromJson(obj, classModel);
    }

    public static Object jsonToObject(String obj, Type classModel) {
        Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();
        return gson.fromJson(obj, classModel);
    }

    public static String objectToString(Object obj) {
        try {
            if (obj == null) {
                return "";
            }
            Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    .serializeNulls()
                    .create();
            return gson.toJson(obj);
        } catch (Exception ex) {
            return "";
        }
    }

    public static void showDialogCameraGallery(final Activity activity) {
        new SweetAlertDialog(activity)
                .setTitleText("Adicionar brasão")
                .setContentText(activity.getString(R.string.imageResource))
                .setConfirmText(activity.getString(R.string.camera))
                .setCancelText(activity.getString(R.string.gallery))
                .setConfirmClickListener(sweetAlertDialog -> {
                    launchCamera(activity);
                    sweetAlertDialog.dismiss();
                })
                .setCancelClickListener(sweetAlertDialog -> {
                    launchGallery(activity);
                    sweetAlertDialog.dismiss();
                })
                .show();
    }

//    public static void showDialogCameraGalleryWithResposse(final Context context, Action1<Result<Activity>> callback) {
//        new MaterialDialog.Builder(context)
//                .content(R.string.imageResource)
//                .contentGravity(GravityEnum.START)
//                .positiveText(R.string.camera)
//                .negativeText(R.string.gallery)
//                .onPositive((dialog, which) -> launchCameraWithResponse(context, callback))
//                .onNegative((dialog, which) -> pickPhotoFromGallery(context))
//                .show();
//    }

//    public static void launchCameraWithResponse(final Context context, Action1<Result<Activity>> callback) {
//        Dexter.withActivity((Activity) context)
//                .withPermission(Manifest.permission.CAMERA)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
//                        RxActivityResult.on((Activity) context)
//                                .startIntent(startCustomCameraIntent)
//                                .subscribe(activityResult -> {
//                                    if (activityResult.resultCode() == Activity.RESULT_OK) {
//                                        launchCropWithRespose(activityResult.data().getData(), context, callback);
//                                    }
//                                });
//
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                })
//                .check();
//    }

    public static void launchCamera(final Activity activity) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent startCustomCameraIntent = new Intent(activity, CameraActivity.class);
                        activity.startActivityForResult(startCustomCameraIntent, Constants.REQUEST_CAMERA);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    public static void launchGallery(final Activity activity) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        pickPhotoFromGallery(activity);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

//    public static void pickPhotoFromGalleryWithRespose(Context context, Action1<Result<Activity>> callback) {
////        Intent intent = new Intent(Intent.ACTION_PICK,
////                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        Intent intent = CropImage.getPickImageChooserIntent(context);
//
//        RxActivityResult.on((Activity) context)
//                .startIntent(intent)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(activityResult -> {
//                    if (activityResult.resultCode() == Activity.RESULT_OK) {
//                        launchCropWithRespose(activityResult.data().getData(), context, callback);
//                    }
//                });
//    }

    public static void pickPhotoFromGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, Constants.PICK_PHOTO_CODE);
        }
    }

//    public static void launchCropWithRespose(Uri uri, Context context, Action1<Result<Activity>> callback) {
//        Intent intent = CropImage.activity(uri)
//                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
//                .setCropShape(CropImageView.CropShape.OVAL)
//                .setAutoZoomEnabled(false)
//                .setActivityMenuIconColor(R.color.colorPrimary)
//                .setAllowRotation(true)
//                .setFixAspectRatio(true)
//                .setMinCropResultSize(30, 30)
//                .setActivityMenuIconColor(Color.WHITE)
//                .setActivityTitle("Cortar imagem")
//                .getIntent(context, CropImageActivity.class);
//
//        RxActivityResult.on((Activity) context)
//                .startIntent(intent)
//                .subscribe(callback);
//    }

    public static void launchCrop(Uri uri, Context context) {
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAutoZoomEnabled(false)
                .setActivityMenuIconColor(R.color.colorPrimary)
                .setAllowRotation(true)
                .setFixAspectRatio(true)
                .setMinCropResultSize(30, 30)
                .setActivityMenuIconColor(Color.WHITE)
                .setActivityTitle("Cortar imagem")
                .start((Activity) context);
    }

    public static void launchCropChat(Uri uri, Context context) {
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAutoZoomEnabled(false)
                .setActivityMenuIconColor(R.color.colorPrimary)
                .setAllowRotation(true)
                .setFixAspectRatio(false)
                .setMinCropResultSize(30, 30)
                .setActivityMenuIconColor(Color.WHITE)
                .setActivityTitle("Cortar imagem")
                .start((Activity) context);
    }

    public static void launchCropOval(Uri uri, Context context) {
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAutoZoomEnabled(false)
                .setActivityMenuIconColor(R.color.colorPrimary)
                .setAllowRotation(true)
                .setFixAspectRatio(true)
                .setMinCropResultSize(30, 30)
                .setActivityMenuIconColor(Color.WHITE)
                .setActivityTitle("Cortar imagem")
                .start((Activity) context);
    }

//    public static void showSimpleDialog(Context context, int message, final MaterialDialog.SingleButtonCallback listener) {
//        showSimpleDialog(context, context.getString(message), listener);
//    }
//
//    public static void showSimpleDialog(Context context, String message, final MaterialDialog.SingleButtonCallback listener) {
//        new MaterialDialog.Builder(context)
//                .content(message)
//                .onPositive(listener)
//                .positiveText(R.string.ok)
//                .contentColorRes(R.color.black)
//                .cancelable(false)
//                .show();
//    }
//
//    public static MaterialDialog showSimpleSimNaoDialog(Context context, int message, final MaterialDialog.SingleButtonCallback listener) {
//        return new MaterialDialog.Builder(context)
//                .content(message)
//                .titleGravity(GravityEnum.CENTER)
//                .positiveText(R.string.sim)
//                .negativeText(R.string.nao)
//                .onPositive(listener)
//                .cancelable(false)
//                .show();
//    }
//
//    public static MaterialDialog showSimpleDialog(Context context, int tittle, int message, final MaterialDialog.SingleButtonCallback listener) {
//        return new MaterialDialog.Builder(context)
//                .content(message)
//                .onNeutral(listener)
//                .neutralText(R.string.ok)
//                .title(tittle)
//                .titleColorRes(R.color.dark_hot_pink)
//                .contentColorRes(R.color.black)
//                .titleGravity(GravityEnum.CENTER)
//                .contentGravity(GravityEnum.CENTER)
//                .buttonsGravity(GravityEnum.CENTER)
//                .cancelable(false)
//                .show();
//    }

    public static void showSweetDialogSuccess(Context context, String msg) {
        SweetAlertDialog sa = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sa.setContentText(msg);
        sa.setCanceledOnTouchOutside(true);
        sa.show();
    }

    public static void showSweetDialogSuccess(Context context, String msg, SweetAlertDialog.OnSweetClickListener callback) {
        SweetAlertDialog sa = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sa.setTitleText("");
        sa.setContentText(msg);
        sa.setCanceledOnTouchOutside(true);
        sa.setConfirmClickListener(sweetAlertDialog -> {
            if (callback != null) callback.onClick(sweetAlertDialog);
            sweetAlertDialog.dismiss();
        });
        sa.show();
    }

    public static void showSweetDialogError(Context context, String msg, SweetAlertDialog.OnSweetClickListener callback) {
        SweetAlertDialog sa = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        sa.setTitleText("Oops...");
        sa.setContentText(msg);
        sa.setCanceledOnTouchOutside(true);
        sa.setConfirmClickListener(sweetAlertDialog -> {
            if (callback != null) callback.onClick(sweetAlertDialog);
            sweetAlertDialog.dismiss();
        });
        sa.show();
    }

    public static void showSweetDialogError(Context context, int msg, SweetAlertDialog.OnSweetClickListener callback) {
        SweetAlertDialog sa = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        sa.setTitleText("Oops...");
        sa.setContentText(context.getString(msg));
        sa.setCanceledOnTouchOutside(true);
        sa.setConfirmClickListener(sweetAlertDialog -> {
            if (callback != null) callback.onClick(sweetAlertDialog);
            sweetAlertDialog.dismiss();
        });
        sa.show();
    }

//    public static void showDialogThrowable(Throwable t, MaterialDialog.SingleButtonCallback callback, Context context) {
//        if (t instanceof HttpException) {
//            HttpException httpException = ((HttpException) t);
//            if (httpException.code() == 401) {  //&& ProConnectApp.getInstance().getUser() != null
//                //logout
//                //abrir login
//            } else if (httpException.code() == 401 || httpException.code() == 422) {
//                try {
//                    JsonObject jsonObject = (JsonObject) Utils.jsonToObject(httpException.response().errorBody().string(), JsonObject.class);
//                    if (jsonObject.has("errors") && jsonObject.get("errors").isJsonArray()) {
//                        CustomSimpleDialog.show(context, context.getString(R.string.atencao), jsonObject.get("errors").getAsJsonArray().get(0).getAsString(), callback);
//                    } else if (jsonObject.has("errors") && jsonObject.get("errors").isJsonObject()) {
//                        CustomSimpleDialog.show(context, context.getString(R.string.atencao), jsonObject.get("errors").getAsJsonObject().getAsString(), callback);
//                    } else {
//                        CustomSimpleDialog.show(context, context.getString(R.string.atencao), jsonObject.get("errors").toString(), callback);
//                    }
//                } catch (IOException e) {
//                    CustomSimpleDialog.show(context, context.getString(R.string.atencao), t.getMessage(), callback);
//                }
//            } else if (httpException.code() == 503) {
//                CustomSimpleDialog.show(context, R.string.atencao, R.string.servico_indisponivel, callback);
//            } else {
//                CustomSimpleDialog.show(context, context.getString(R.string.atencao), t.getMessage(), callback);
//            }
//        } else if (t instanceof ConnectException || t instanceof IOException) {
//            CustomSimpleDialog.show(context, R.string.atencao, R.string.sem_conexao, callback);
//        } else {
//            CustomSimpleDialog.show(context, context.getString(R.string.atencao), t.getMessage(), callback);
//        }
//    }

    public static void getContactsWithEmail(Context context, OnGetContacts callback) {
        new AsyncTask<Void, Void, List>() {
            @Override
            protected List doInBackground(Void... params) {
                ArrayList<String> listEmails = new ArrayList<String>();
                ContentResolver cr = context.getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor cur1 = cr.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (cur1.moveToNext()) {
                            String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            if (!TextUtils.isEmpty(email)) {
                                listEmails.add(email.trim().replace(" ", ""));
                            }
                        }
                        cur1.close();
                    }
                }
                return listEmails;
            }

            @Override
            protected void onPostExecute(List list) {
                super.onPostExecute(list);
                if (callback != null) callback.onSuccess(list);
            }
        }.execute();
    }

    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, String path) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(path);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public final static void changeTextRubikRegutal(Context context, TextView textView) {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder.append(textView.getText());
        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(context.getAssets(), context.getString(R.string.rubik_regular)));
        sBuilder.setSpan(typefaceSpan, 0, textView.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(sBuilder, TextView.BufferType.SPANNABLE);
    }

    public final static void changeTextRubikBold(Context context, TextView textView) {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder.append(textView.getText());
        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(context.getAssets(), context.getString(R.string.rubik_bold)));
        sBuilder.setSpan(typefaceSpan, 0, textView.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(sBuilder, TextView.BufferType.SPANNABLE);
    }

    public final static void sharedContent(Context context) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Texto de compartilhamento");
        context.startActivity(Intent.createChooser(sharingIntent, "Convidar"));
    }

    public final static boolean isListEmpty(List<?> arrayList) {
        return arrayList == null || arrayList.isEmpty();
    }


    public static String getSHA256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();
            return String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDateTime() {
        SimpleDateFormat simpleDateFormatString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return simpleDateFormatString.format(Calendar.getInstance().getTime());
    }

    public static String getDateForDateTime(String startDate) {
        if (!TextUtils.isEmpty(startDate)) {
            SimpleDateFormat simpleDateFormatString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormatString.parse(startDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                return simpleDateFormat.format(calendar.getTime());
            } catch (ParseException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String formatString(String string, String format){
        if(string.contains("%s")){
            return string.replace("%s", format);
        } else {
            return string;
        }
    }

    public static long getTimeStamp() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getDataFormatBackup(long date) {
        if (date != 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm '–' dd 'de' MMMM '–' yyyy");
            return simpleDateFormat.format(date);
        } else {
            return "";
        }

    }

    public static String getHourFromTimeStamp(long startDate) {
        if (startDate != 0) {
//            SimpleDateFormat simpleDateFormatString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            return simpleDateFormat.format(startDate);
        } else {
            return "";
        }
    }

    public static String getDataFromTimeStamp(long startDate) {
        if (startDate != 0) {
//            SimpleDateFormat simpleDateFormatString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return simpleDateFormat.format(startDate);
        } else {
            return "";
        }
    }

    public static boolean isDateToday(long date) {
        long dateToday = getTimeStamp();
        if (getDayFromTimeStamp(date) == getDayFromTimeStamp(dateToday)) {
            return true;
        }

        return false;
    }

    public static String getDateFormatFromTimeStamp(long startDate) {
        if (startDate != 0) {
            if (isDateToday(startDate)) {
                return "HOJE";
            } else if (isDateIsYesterday(startDate)) {
                return "ONTEM";
            }
//            SimpleDateFormat simpleDateFormatString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
            return simpleDateFormat.format(startDate).toUpperCase();
        } else {
            return "";
        }
    }

    public static String getDataFormatNotificacao(String startDate) {
        if (!TextUtils.isEmpty(startDate)) {
            SimpleDateFormat simpleDateFormatString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy '-' HH:mm");
            try {
                Date date = simpleDateFormatString.parse(startDate);
                return simpleDateFormat.format(date.getTime());
            } catch (ParseException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getDateApiFormatFromTimeStamp(String dateApi) {
        try {

            SimpleDateFormat simpleDateFormatString = new SimpleDateFormat("yyyy-MM-dd");
            long startDate = 0;

            startDate = simpleDateFormatString.parse(dateApi).getTime();

//                if (getDayFromTimeStamp(startDate) == getDayFromTimeStamp(getTimeStamp())) {
//                    return "HOJE";
//                } else if (isDateIsYesterday(startDate)) {
//                    return "ONTEM";
//                }
//            SimpleDateFormat simpleDateFormatString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
            return simpleDateFormat.format(startDate).toUpperCase();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getHoraFormatadaConversa(long startDate) {
        if (startDate != 0) {
            if (getDayFromTimeStamp(startDate) == getDayFromTimeStamp(getTimeStamp())) {
                return getHourFromTimeStamp(startDate);
            } else if (isDateIsYesterday(startDate)) {
                return "ontem";
            }
            return getDataFromTimeStamp(startDate);
        } else {
            return "";
        }
    }

    public static long getDayFromTimeStamp(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static boolean isDateIsYesterday(long time) {
        Calendar yesterdayFromTime = Calendar.getInstance();
        yesterdayFromTime.setTimeInMillis(getDayFromTimeStamp(time));

        Calendar yesterday = Calendar.getInstance();
        yesterday.setTimeInMillis(getDayFromTimeStamp(yesterday.getTimeInMillis()));
        yesterday.add(Calendar.DAY_OF_MONTH, -1);

        if (yesterday.getTimeInMillis() == yesterdayFromTime.getTimeInMillis()) {
            return true;
        }


        return false;
    }

    public static boolean isDateToday(String date) {
        String dateToday = getDateForDateTime(getDateTime());
        String dateResposta = getDateForDateTime(date);
        return dateToday.equalsIgnoreCase(dateResposta);
    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public static String compareDates(String d1,String d2)
    {
        try{
            // If you already have date objects then skip 1

            //1
            // Create 2 dates starts
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            System.out.println("Date1"+sdf.format(date1));
            System.out.println("Date2"+sdf.format(date2));
            System.out.println();

            // Create 2 dates ends
            //1

            // Date object is having 3 methods namely after,before and equals for comparing
            // after() will return true if and only if date1 is after date 2
            if(date1.after(date2)){
                return "after";
            }
            // before() will return true if and only if date1 is before date2
            if(date1.before(date2)){
                return "before";
            }

            //equals() returns true if both the dates are equal
            if(date1.equals(date2)){
                return "equal";
            }
        }
        catch(ParseException ex){
            ex.printStackTrace();
        }
        return "";
    }

    public static int getLengthFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
            return file_size;
        } else {
            return 0;
        }
    }

    public static boolean isFileLarger(String pathFile, boolean isVideo) {
        int value = getLengthFile(pathFile);
        if (value > (isVideo ? Constants.MAX_LEGHT_FILE_VIDEO : Constants.MAX_LEGHT_FILE) ||
                value == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void createFolders() {
        File folderImagens = new File(Utils.getPathImage());
        if (!folderImagens.exists()) {
            folderImagens.mkdirs();
        }

        File folderVideos = new File(Utils.getPathVideo());
        if (!folderVideos.exists()) {
            folderVideos.mkdirs();
        }

        File folerDocuments = new File(Utils.getPathDocumento());
        if (!folerDocuments.exists()) {
            folerDocuments.mkdirs();
        }

        File folderAudio = new File(Utils.getPathAudio());
        if (!folderAudio.exists()) {
            folderAudio.mkdirs();
        }

        File folderBackup = new File(Utils.getPathBackupRealm());
        if (!folderBackup.exists()) {
            folderBackup.mkdirs();
        }
    }

    public static String getDataFormatBackup(Date date) {
        SimpleDateFormat simpleDateFormatString = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
        return simpleDateFormatString.format(date);
    }

    private static String getDateTimeFile() {
        SimpleDateFormat simpleDateFormatString = new SimpleDateFormat("yyyyMMdd-HHmmss");
        return simpleDateFormatString.format(Calendar.getInstance().getTime());
    }

    public static String getImageName(String link) {
        return String.format("IMG-%s.%s", getDateTimeFile() + getRandomNumber(), link.substring(link.length() - 3, link.length()));
    }

    public static String getPathImage() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + R.string.app_name +"/Media/" + R.string.app_name + " Images";
    }

    public static String getVideoName(String link) {
        return String.format("VID-%s.%s", getDateTimeFile() + getRandomNumber(), link.substring(link.length() - 3, link.length()));
    }

    public static String getPathVideo() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + R.string.app_name +"/Media/" + R.string.app_name + " Video";
    }

    public static String getDocumentoName(String link) {
        String[] arrayString = link.split("/");
        String nomeArquivo = arrayString[arrayString.length - 1];
        return nomeArquivo;
    }

    public static String getPathDocumento() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + R.string.app_name +"/Media/" + R.string.app_name + " Documents";
    }

    public static String getAudioName() {
        return String.format("AUD-%s.mp4", getDateTimeFile() + getRandomNumber());
    }

    public static String getAudioName(String link) {
        String[] arrayString = link.split("/");
        String nomeArquivo = arrayString[arrayString.length - 1];
        return String.format("AUD-%s", nomeArquivo);
    }

    public static String getPathAudio() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + R.string.app_name +"/Media/" + R.string.app_name + " Audio";
    }

    public static String getPathBackupRealm() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + R.string.app_name +"/Databases";
    }

    public static String getTamanhoDataArquivo(String path, long timeStamp) {
//        ((double) getLengthFile(path) / 1024)
        int value = getLengthFile(path);
        String tamanhoFormatado = "";
        if (value > 900) {
            double valueD = (double) value / 1024;
            tamanhoFormatado = String.format("%sMB", String.format("%.2f", round(valueD, 2)));
        } else {
            tamanhoFormatado = String.format("%sKB", String.valueOf(value));
        }
        return String.format("%s - %s", tamanhoFormatado, getDataFromTimeStamp(timeStamp));
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static void openImagem(Context context, String path) {
        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Uri uri = Uri.parse("content://" + context.getApplicationContext().getPackageName() + path);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "image/*");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        try {
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
//                            CustomSimpleDialog.show(context, R.string.atencao, R.string.erro_app_nao_encontrado);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    public static void openVideo(Context context, String path) {
        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Uri uri = Uri.parse("content://" + context.getApplicationContext().getPackageName() + path);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "video/*");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        try {
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
//                            CustomSimpleDialog.show(context, R.string.atencao, R.string.erro_app_nao_encontrado);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    public static void openPdf(Context context, String path) {
        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(path)), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        try {
                            context.startActivity(Intent.createChooser(intent, context.getString(R.string.selecione_aplicativo)));
                        } catch (ActivityNotFoundException ex) {
//                            CustomSimpleDialog.show(context, R.string.atencao, R.string.erro_app_nao_encontrado);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    public static void openAudio(Context context, String path) {
        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Uri uri = Uri.fromFile(new File(path));
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "audio/*");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        try {
                            context.startActivity(Intent.createChooser(intent, context.getString(R.string.selecione_aplicativo)));
                        } catch (ActivityNotFoundException ex) {
//                            CustomSimpleDialog.show(context, R.string.atencao, R.string.erro_app_nao_encontrado);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    public static String getTimeFormat(long seconds) {
        long minutes = (seconds % 3600) / 60;
        long secondsFormat = seconds % 60;

        return String.format("%02d:%02d", minutes, secondsFormat);
    }

    public static void restartApp(Activity context) {
        Intent mStartActivity = new Intent(context.getApplicationContext(), BaseActivity.class);  //SPLASHACTIVITY
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context.getApplicationContext(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, mPendingIntent);
        System.exit(0);
    }

    public static String getIDFormatedFirebase(String idFirebase) {
        StringTokenizer stringTokenizer = new StringTokenizer(idFirebase, "|");
        return stringTokenizer.nextToken();
    }

    public static int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(99);
    }
    
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
}
