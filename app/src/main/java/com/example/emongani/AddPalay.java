package com.example.emongani;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class AddPalay extends AppCompatActivity implements View.OnClickListener {

    // product image
    private ImageButton productimage;
    private Uri imageUri = null;
    private static final int GALLERY_REQUEST = 2;

    // add palay variables
    EditText productname, productlocation, productkg, productdesc, startingbid, dudate, dutime;
    ProductHelper productHelper;
    RadioButton wholeharvest, wholesale;

    private StorageReference storageRef;
    private FirebaseStorage storage;
    private DatabaseReference root;

    int i = 0;

    // duration // initialize variables
    private DatePickerDialog dpicker;
    private int mHour;
    private int mMinute;

    // yes button to start bidding and store the data in the database
    Button postbtn;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_palay);

            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            root = FirebaseDatabase.getInstance().getReference("Product").child("Pending");

            postbtn = findViewById(R.id.postpalaybtn);
            productimage = findViewById(R.id.prodimg_iv);
            productname = findViewById(R.id.productname_et);
            productlocation = findViewById(R.id.location_et);
            wholeharvest = findViewById(R.id.wholeharvest_rb);
            wholesale = findViewById(R.id.wholesale_rb);
            productkg = findViewById(R.id.kg_et);
            productdesc = findViewById(R.id.proddesc_et);
            startingbid = findViewById(R.id.startingbid_et);
            dudate = findViewById(R.id.date_et);
            dutime = findViewById(R.id.time_et);

            productHelper = new ProductHelper();

                productimage.setOnClickListener(view -> {
                   Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent , GALLERY_REQUEST);
                });
                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            i = (int) snapshot.getChildrenCount();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddPalay.this, "Adding new product failed", Toast.LENGTH_SHORT).show();
                    }
                });
                dudate.setOnClickListener(view -> {
                    final Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    dpicker = new DatePickerDialog(AddPalay.this, (datePicker, year1, month1, dayOfMonth) ->
                        dudate.setText(dayOfMonth + "/" + (month1 +1)+ "/" + year1), year, month, day);
                        root.child(String.valueOf(i+1)).setValue(productHelper);
                    dpicker.show();
                });
                dutime.setOnClickListener (view1 -> {
                    TimePickerDialog tpicker = new TimePickerDialog(AddPalay.this, (timePicker, hourOfDay, minute) -> {
                    mHour = hourOfDay;
                    mMinute = minute;
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(0, 0, 0, mHour, mMinute);
                        dutime.setText(DateFormat.format("hh:mm aa", calendar));
                        root.child(String.valueOf(i+1)).setValue(productHelper);
                        }, 12, 0, false
                    );
                    tpicker.show();
                });
                postbtn.setOnClickListener(view -> {
                    if(imageUri != null){
                        ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();
                        StorageReference ref = storageRef.child("prodImage/" + UUID.randomUUID().toString());
                        ref.putFile(imageUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    progressDialog.dismiss();
                                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    result.addOnSuccessListener(uri -> {
                                        String prodImageLink = uri.toString();
                                        productHelper.setProdImg(prodImageLink);
                                        root.child(String.valueOf(i+1)).setValue(productHelper);
                                        String prodName = productname.getText().toString().trim();
                                        String prodLocation = productlocation.getText().toString().trim();
                                        String prodCategory = wholesale.getText().toString().trim();
                                        String prodCategory2 = wholeharvest.getText().toString().trim();
                                        String catkg = productkg.getText().toString().trim();
                                        String prodDesc = productdesc.getText().toString().trim();
                                        long startingBid = Long.parseLong(startingbid.getText().toString().trim());
                                        String date = dudate.getText().toString();
                                        String time = dutime.getText().toString();
                                        productHelper.setProdName(prodName);
                                        productHelper.setProdLocation(prodLocation);
                                        productHelper.setCatKg(catkg);
                                        productHelper.setProdDesc(prodDesc);
                                        productHelper.setStartingBid(startingBid);
                                        productHelper.setDate(date);
                                        productHelper.setTime(time);
                                        root.child(String.valueOf(i+1)).setValue(productHelper);
                                            if(wholesale.isChecked()){
                                                productHelper.setProdCategory(prodCategory);
                                                root.child(String.valueOf(i+1)).setValue(productHelper);
                                            }else{
                                                productHelper.setProdCategory(prodCategory2);
                                                root.child(String.valueOf(i+1)).setValue(productHelper);
                                            }
                                    });
                                    Toast.makeText(this, "New Product has been added.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(AddPalay.this, MySales.class);
                                    startActivity(intent);

                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddPalay.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }else{
                        Toast.makeText(this, "Please enter product details.", Toast.LENGTH_LONG).show();
                    }

                });

            // call the action bar
                ActionBar actionBar = getSupportActionBar();
            // show back button
                Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        }

    // choose image
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode,resultCode,data);
                if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
                    {
                        imageUri = data.getData();
                            try{
                                Bitmap bitmap = MediaStore
                                                    .Images
                                                    .Media
                                                    .getBitmap(
                                                            getContentResolver(),
                                                            imageUri);
                                productimage.setImageBitmap(bitmap);
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                }
            }

    // event will enable the back function to the button on press
        public boolean onOptionsItemSelected(@NonNull MenuItem item){
            if (item.getItemId() == android.R.id.home) {
                this.finish();
                return true;
            }
        return super.onOptionsItemSelected(item);
        }

    @Override
    public void onClick(View view) {

    }


}