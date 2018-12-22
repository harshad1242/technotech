package com.technotech.technotechapplication.Common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.technotech.technotechapplication.Cropper.FileUtils;
import com.technotech.technotechapplication.Cropper.ImageCropActivity;
import com.technotech.technotechapplication.R;
import com.technotech.technotechapplication.util.Constants;
import com.technotech.technotechapplication.util.MaterialSpinner;
import com.technotech.technotechapplication.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;

import id.zelory.compressor.FileUtil;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_companyname,edt_address,/*edt_city,*/edt_state,edt_officeno,edt_ownername,edt_personalcontact,edt_emailid,edt_password,edt_pincode,edt_otherbussiness;
    Context context;
    MaterialSpinner biz_typespinner;
    CardView card_otherbusiness;
    SharedPreferences common_mypref;
    RelativeLayout ly_relative;
    int SELECT_FILE=2,REQUEST_CAMERA=1;
    private static int REQUEST_CROP_PICTURE = 3;
    static int REQUEST_CROP_CAMERA=4;
    CircularImageView set_icon;
    FloatingActionButton fab;
    public static final int DEFAULT_TIMEOUT_MS = 10000;
    public static final int DEFAULT_MAX_RETRIES = 10;
    public static final float DEFAULT_BACKOFF_MULT = 1f;
    private ArrayAdapter<CharSequence> spinAdaptert;
    Uri myUri=null;
    SimpleDateFormat sdf;
    String currentDateandTime;
    int spinnerid;
    private TextWatcher textWatcher;
    MaterialSpinner edt_city;
    private static final int CAMERA_PHOTO = 111;
    private Uri imageToUploadUri;
    public static Context c;
    ImageView img_categoryleft;
    String[] mainRecordimage;
    TextInputLayout txtinput_companyname,txtinput_address,txtinput_pincode,txtinput_state,txtinput_officeno,txtinputtitle_ownername,txtinput_password,txtinput_personalcontact,txtinput_emailid;
    TextView txt_pincodenote;
    //Class Declaration
    Utils util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        context=this;
        util=new Utils(context);
        initializeview();
    }
    public void initializeview() {
        TextView headertitle=(TextView)findViewById(R.id.headertitle);
        img_categoryleft=(ImageView) findViewById(R.id.txt_categoryleft);
        card_otherbusiness=(CardView)findViewById(R.id.card_otherbusiness);
        biz_typespinner =(MaterialSpinner) findViewById(R.id.spinner1);
        set_icon=(CircularImageView)findViewById(R.id.set_icon);
        edt_otherbussiness=(EditText)findViewById(R.id.edt_otherbussiness);
        edt_companyname=(EditText)findViewById(R.id.edt_companyname);
        edt_address=(EditText)findViewById(R.id.edt_address);
        // edt_city=(EditText)findViewById(R.id.edt_city);
        edt_city=(MaterialSpinner)findViewById(R.id.edt_city);
        spinAdaptert = new ArrayAdapter<CharSequence>(RegisterActivity.this, R.layout.spinner_item, new String[]{} )
        {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                return v;
            }
        };
        spinAdaptert.setDropDownViewResource(R.layout.spinner_item);
        edt_city.setAdapter(spinAdaptert);
        edt_state=(EditText)findViewById(R.id.edt_state);
        edt_officeno=(EditText)findViewById(R.id.edt_officeno);
        edt_ownername=(EditText)findViewById(R.id.edt_ownername);
        edt_personalcontact=(EditText)findViewById(R.id.edt_personalcontact);
        edt_emailid=(EditText)findViewById(R.id.edt_emailid);
        edt_password=(EditText)findViewById(R.id.edt_password);
        edt_pincode=(EditText)findViewById(R.id.edt_pincode);
        edt_pincode.addTextChangedListener(textWatcher=new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0)
                {
                  //  jsonGetinfobyPincode(s.toString());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 6)
                {
                    //jsonGetinfobyPincode(s.toString());
                }
            }
        });
        //edt_businesstype=(EditText)findViewById(R.id.edt_businesstype);
        //edt_businesstype.setTypeface(sansregular);

        ly_relative=(RelativeLayout) findViewById(R.id.ly_relative);
        fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(mClickListener);
        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0)
                {
                    ly_relative.setVisibility(View.VISIBLE);
                }
                else
                {
                    ly_relative.setVisibility(View.GONE);
                }
            }
        });
        set_icon.setOnClickListener(mClickListener);
        biz_typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerid = (int)biz_typespinner.getItemIdAtPosition(position)+1;
                if(!biz_typespinner.getSelectedItem().toString().equalsIgnoreCase("Business Type")) {
                    if (biz_typespinner.getSelectedItem().toString().equalsIgnoreCase("other")) {
                        card_otherbusiness.setVisibility(View.VISIBLE);
                        edt_otherbussiness.requestFocus();
                        Picasso.with(context)
                                .load(R.drawable.product_img)
                                .placeholder(R.drawable.product_img)
                                .error(R.drawable.product_img)
                                .into(img_categoryleft);
                        img_categoryleft.setVisibility(View.VISIBLE);
                    } else {
                        card_otherbusiness.setVisibility(View.GONE);
                        try {
                            img_categoryleft.setVisibility(View.VISIBLE);
                            Picasso.with(context)
                                    .load(mainRecordimage[position].replace(" ", "%20"))
                                    .placeholder(R.drawable.product_img)
                                    .error(R.drawable.product_img)
                                    .into(img_categoryleft);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        findViewById(R.id.parent).setOnClickListener(mClickListener);
        txtinput_companyname=(TextInputLayout)findViewById(R.id.txtinput_companyname);
        txtinput_address=(TextInputLayout)findViewById(R.id.txtinput_address);
        txtinput_pincode=(TextInputLayout)findViewById(R.id.txtinput_pincode);
        txtinput_state=(TextInputLayout)findViewById(R.id.txtinput_state);
        txtinput_officeno=(TextInputLayout)findViewById(R.id.txtinput_officeno);
        txtinputtitle_ownername=(TextInputLayout)findViewById(R.id.txtinputtitle_ownername);
        txtinput_password=(TextInputLayout)findViewById(R.id.txtinput_password);
        txtinput_personalcontact=(TextInputLayout)findViewById(R.id.txtinput_personalcontact);
        txtinput_emailid=(TextInputLayout)findViewById(R.id.txtinput_emailid);
        txt_pincodenote=(TextView)findViewById(R.id.txt_pincodenote);

        edt_emailid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(validation())
                    {
                        //saveProfileAccount();
                    }
                }
                return false;
            }
        });

    }

    final View.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(final View v)
        {
            switch (v.getId()) {
                case R.id.set_icon:
                    // util.hideKeyboard();
                    myUri=null;
                    selectImage();
                    break;
                case R.id.fab:
                    // util.hideKeyboard();
                    if(validation())
                       // saveProfileAccount();
                    break;

            }
        }
    };

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=util.checkPermission(RegisterActivity.this);
                if (items[item].equals("Take Photo")) {
                    // userChoosenTask="Take Photo";
                    if(result)
                        // cameraIntent();
                        captureCameraImage();
                } else if (items[item].equals("Choose from Library")) {
                    //  userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void captureCameraImage() {
        Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imageToUploadUri = Uri.fromFile(f);
        startActivityForResult(chooserIntent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if ((requestCode == REQUEST_CROP_PICTURE) && (resultCode == RESULT_OK)) {
                //set_icon.setImageBitmap(BitmapFactory.decodeFile((lastCaptured=croppedImageFile).getAbsolutePath()));
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String croppedImagePath = bundle.getString(ImageCropActivity.EXTRA_IMAGE_PATH);
                    myUri = Uri.parse(croppedImagePath);
                    Picasso.with(this).load(new File(croppedImagePath)).into(set_icon);
                }

            }else if ((requestCode == REQUEST_CROP_CAMERA) && (resultCode == RESULT_OK)) {
                //set_icon.setImageBitmap(BitmapFactory.decodeFile((lastCaptured=croppedImageFile).getAbsolutePath()));
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String croppedImagePath = bundle.getString(ImageCropActivity.EXTRA_IMAGE_PATH);
                    myUri = Uri.parse(croppedImagePath);
                    // set_icon.setImageURI(myUri);
                    Picasso.with(this).load(new File(croppedImagePath)).into(set_icon);
                }
            }
        }else
        {
            myUri=null;
            Picasso.with(context)
                    .load(myUri)
                    .placeholder(R.drawable.user_icon_big)
                    .error(R.drawable.user_icon_big)
                    .into(set_icon);
        }
    }
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
           /* try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
               Uri gallery= bitmapToUriConverter(bm);
                CropImageIntentBuilder cropImage = new CropImageIntentBuilder(500, 500, gallery);
                cropImage.setOutlineColor(0xFF03A9F4);
                cropImage.setScale(true);
                cropImage.setScaleUpIfNeeded(true);
                cropImage.setSourceImage(data.getData());
                startActivityForResult(cropImage.getIntent(this), REQUEST_CROP_PICTURE);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
           /* Uri croppedImage;
            croppedImage=data.getData();
            String picturePath =util.getpath(this,croppedImage);
            String copiedFilepath= FileUtils.getInstance().copyFile(picturePath,FileUtils.getInstance().getFilePath(this, FileUtils.MEDIA_TYPE.PICTURE));*/
            String copiedFilepath=null;
            try {
                File actualImage = FileUtil.from(this, data.getData());
                copiedFilepath=actualImage.getAbsolutePath();
            }catch (Exception e)
            {
                util.customToast("Image Not Available");
            }
            Intent intent = new Intent(this, ImageCropActivity.class);
            intent.putExtra(ImageCropActivity.EXTRA_X_RATIO, 26);///26,15 for banner
            intent.putExtra(ImageCropActivity.EXTRA_Y_RATIO, 26);
            intent.putExtra(ImageCropActivity.EXTRA_IMAGE_PATH, copiedFilepath);
            startActivityForResult(intent, REQUEST_CROP_PICTURE);
        }
        //set_icon.setImageBitmap(bm);
    }
    private void onCaptureImageResult(Intent data) {
       /* Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Bitmap bm=null;
        try {
            bm = (Bitmap)data.getExtras().get("data");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        if(imageToUploadUri != null) {
            Uri croppedImage = imageToUploadUri;
            String picturePath = util.getRealPathFromURI(this,croppedImage);
            String copiedFilepath= FileUtils.getInstance().copyFile(picturePath,FileUtils.getInstance().getFilePath(this, FileUtils.MEDIA_TYPE.PICTURE));
            Intent intent = new Intent(this, ImageCropActivity.class);
            intent.putExtra(ImageCropActivity.EXTRA_X_RATIO, 26);///26,15 for banner
            intent.putExtra(ImageCropActivity.EXTRA_Y_RATIO, 26);
            intent.putExtra(ImageCropActivity.EXTRA_IMAGE_PATH, copiedFilepath);
            startActivityForResult(intent, REQUEST_CROP_CAMERA);
        }
    }

    private boolean validation() {
        boolean flag = true;
        if(myUri==null)
        {
            util.customToast(getResources().getString(R.string.enterlogoimage));
            flag = false;
        }
        else if(edt_address.getText().toString().trim().length()==0)
        {  // main_categ.setError("MainCategory should not be null!");
            // edt_address.setError("Address should not be null!");
            util.customToast(getResources().getString(R.string.enteryouraadddress));
            edt_address.requestFocus();
            flag = false;
        }
        else if(edt_pincode.getText().toString().trim().length()==0){
            // edt_pincode.setError("Pincode no. should not be null!");
            util.customToast(getResources().getString(R.string.pincodenull));
            edt_pincode.requestFocus();
            flag = false;
        }
        else if(!(edt_pincode.getText().toString().trim().length()>0)&& TextUtils.isDigitsOnly(edt_pincode.getText().toString().trim()))
        {// && TextUtils.isDigitsOnly(qty.getText().toString().trim())
            util.customToast(getResources().getString(R.string.pincodenull));
            edt_pincode.requestFocus();
            flag = false;
        }
        else if((edt_pincode.getText().toString().trim().length()!=6)){
            util.customToast(getResources().getString(R.string.pincodenull));
            edt_pincode.requestFocus();
            flag = false;
        }
        else if (edt_city.getSelectedItem().toString().trim().equalsIgnoreCase("City*")) {  // main_categ.setError("MainCategory should not be null!");
            //edt_city.setError("City should not be null.Get city by entering Pincode!");
            util.customToast(getResources().getString(R.string.selectyourcity));
            edt_city.requestFocus();
            flag = false;
        }
        else if(edt_state.getText().toString().trim().length()==0)
        {
            util.customToast(getResources().getString(R.string.enteryourstate));
            edt_state.requestFocus();
            flag = false;
        }
        else if((edt_officeno.getText().toString().trim().length()==0)){
            util.customToast(getResources().getString(R.string.officenonull));
            // edt_officeno.setError(getResources().getString(R.string.officenonull));
            edt_officeno.requestFocus();
            flag = false;
        }
        else if(!(edt_officeno.getText().toString().trim().length()>0)&& TextUtils.isDigitsOnly(edt_officeno.getText().toString().trim())) {// && TextUtils.isDigitsOnly(qty.getText().toString().trim())
            //edt_officeno.setError(getResources().getString(R.string.officenonull));
            util.customToast(getResources().getString(R.string.officenonull));
            edt_officeno.requestFocus();
            flag = false;
        }
        else if((edt_officeno.getText().toString().trim().length()<10)){
            // edt_officeno.setError("Minimum 10 digits required.");
            util.customToast(getResources().getString(R.string.officenonull));
            edt_officeno.requestFocus();
            flag = false;
        }
        else if(edt_password.getText().toString().trim().length()<6)
        {
            util.customToast(getResources().getString(R.string.enteryourpassword));
            edt_password.requestFocus();
            flag = false;
        }

        else if(!(edt_personalcontact.getText().toString().trim().length()>0)&& TextUtils.isDigitsOnly(edt_personalcontact.getText().toString().trim())) {// && TextUtils.isDigitsOnly(qty.getText().toString().trim())
            util.customToast(getResources().getString(R.string.contactnumbertendigit));
            edt_personalcontact.requestFocus();
            flag = false;
        }
        else if((edt_personalcontact.getText().toString().trim().length()<10)){
            util.customToast(getResources().getString(R.string.contactnumbertendigit));
            edt_personalcontact.requestFocus();
            flag = false;
        }

        else if(edt_emailid.getText().length()==0){
            // edt_emailid.setError("Email-id should not be null!");
            util.customToast(getResources().getString(R.string.uservalidation));
            flag = false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_emailid.getText().toString().trim()).matches()){
            // edt_emailid.setError("Email-id has wrong format!");
            util.customToast(getResources().getString(R.string.validemail));
            flag = false;
        }

        else if(biz_typespinner.getSelectedItem().toString().trim().equalsIgnoreCase("Business Type")) {
            // biz_typespinner.setError("Please! Select appropriate business type!");
            util.customToast(getResources().getString(R.string.businesstypenull));

            flag = false;
        }

        else if(card_otherbusiness.getVisibility()==View.VISIBLE){
            if(edt_otherbussiness.getText().toString().trim().length()==0)
            {
                util.customToast(getResources().getString(R.string.businesstypenull));
                flag = false;
            }
        }
//////////////////////////////////////////////


       /* if(!(b_mob_no.getText().toString().trim().length()>0)&& TextUtils.isDigitsOnly(b_mob_no.getText().toString().trim())) {// && TextUtils.isDigitsOnly(qty.getText().toString().trim())
            b_mob_no.setError("Digit Only!");
            b_mob_no.requestFocus();
            flag = false;
        }
        else
            b_mob_no.setError(null);*/

        return flag;
    }


}
