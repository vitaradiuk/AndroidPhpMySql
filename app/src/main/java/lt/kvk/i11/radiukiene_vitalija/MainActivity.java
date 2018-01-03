package lt.kvk.i11.radiukiene_vitalija;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextUsername, editTextEmail, editTextPassword;     // apibreziami GUI vaizdiniai objektai
    private Button buttonRegister;
    private ProgressDialog progressDialog;

    private TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){       // jei naudotojas yra prisijunges
            finish();                                               // sis langas uzdaromas
            startActivity(new Intent(this, ProfileActivity.class));     // atidaromas ProfileActivity langas
            return;     // tolesni veiksmai nevykdomi
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);        // inicializuojamis views is xml failo
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);


        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(this);    // pridedamas listener mygtukui
        textViewLogin.setOnClickListener(this);     // pridedamas listener tekstui
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();             // gaunamos ivestos reiksmes
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,        // siunciama POST uzklausa web servisui
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {       // jei nera klaidos atliekant POST uzklausa, atliekamas sis metodas
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response); // apibreziamas JSON objektas ir perduodama uzklausos atsakymo reiksme

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {                      // jei yra klaida atliekant POST uzklausa, atliekamas sis metodas
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {  // metodas, kuriuo sudedami reikalingi parametrai i HashMap
                Map<String, String> params = new HashMap<>();       //String - raktai, reiksmes - objektai; pagal rakta surandamas objektas
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest); // prodedama String uzklausa


    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister)                                                 // jei paspaudziamas mygtukas registruoti
            registerUser();                                                         // registruojamas naujas naudotojas
        if(view == textViewLogin)                                                   // jei paspaudziamas tekstas
            startActivity(new Intent(this, LoginActivity.class));     // atidaromas LoginActivity langas
    }
}
