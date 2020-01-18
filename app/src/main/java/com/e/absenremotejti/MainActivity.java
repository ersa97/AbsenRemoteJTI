package com.e.absenremotejti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();

    private boolean doubleBackPressed;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            doubleBackPressed = false;
        }
    };

    TextInputEditText editTextId;
    TextView textViewName;
    Button buttonVerify;
    Button buttonSignIn;
    EditText editTextLocation;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference reference = db.collection("Employee_List");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = findViewById(R.id.text_input_serial_number);
        textViewName = findViewById(R.id.employee_name);
        buttonVerify = findViewById(R.id.btn_serial_number);
        buttonSignIn = findViewById(R.id.btn_sign_in);
        editTextLocation = findViewById(R.id.text_input_location_employee);


        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = editTextId.getText().toString();
                db.collection("Employee_List").whereEqualTo("Id", id).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot snapshot : task.getResult()) {
                                        textViewName.setText(snapshot.get("Name").toString());
                                    }
                                }
                            }
                        });
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Id = editTextId.getText().toString();
                String Name = textViewName.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssz");
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                String Location = editTextLocation.getText().toString();

                if (Id.trim().isEmpty() || Name.trim().isEmpty() || Location.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in the required information", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> data = new HashMap<>();
                data.put("Id", Id);
                data.put("Name", Name);
                data.put("Date", new Timestamp(new Date()));
                data.put("Location", Location);

                db.collection("Employee_Attendance")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MainActivity.this, "Attendance Recorded", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }




    @Override
    public void onBackPressed() {
        if (doubleBackPressed){
            super.onBackPressed();
            return;
        }

        this.doubleBackPressed = true;
        Toast.makeText(this, "press again to exit app", Toast.LENGTH_SHORT).show();
        handler.postDelayed(runnable, 2000);
    }
}
