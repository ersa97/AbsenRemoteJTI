package com.e.absenremotejti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    String _Id, _Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = findViewById(R.id.text_input_serial_number);
        textViewName = findViewById(R.id.employee_name);
        buttonVerify = findViewById(R.id.btn_serial_number);
        buttonSignIn = findViewById(R.id.btn_sign_in);
        editTextLocation = findViewById(R.id.text_input_location_employee);

        _Id = editTextId.getText().toString();

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               search();
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
                upload(Id, Name, dateFormat, Location);
            }
        });

    }

    public void search(){
        db.collection("Employee_List").whereEqualTo("Id", _Id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                DocumentReference reference = null;
                for (DocumentSnapshot doc : task.getResult()) {

                    reference = doc.getReference();

                    _Name = doc.getString("Name");
                    textViewName.setText(_Name);
                }
            }
        });
    }

    public void upload(final String Id, final String Name, final DateFormat dateFormat, final String Location){
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Employee_Attendance");
        reference.add(new attendance(Id, Name, dateFormat, Location));
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
