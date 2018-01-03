package lt.kvk.i11.radiukiene_vitalija;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername, editTextPassword;        // apibreziami GUI vaizdiniai objektai
    private Button buttonLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){                       // jei naudotojas yra prisijunges
            finish();                                                               // sis langas uzdaromas
            startActivity(new Intent(this, ProfileActivity.class));     // atidaromas ProfileActivity langas
            return;                                                                 // tolesni veiksmai nevykdomi
        }

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);      // inicializuojami views is xml failo
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        progressDialog = new ProgressDialog(this);      // sukuriamas Progressdialog objektas
        progressDialog.setMessage("Please wait...");

        buttonLogin.setOnClickListener(this);

    }

    private void userLogin(){
        final String username = editTextUsername.getText().toString().trim();       // gaunamos ivestos reiksmes
        final String password = editTextPassword.getText().toString().trim();

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(        // siunciama POST uzklausa web servisui
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {       // jei nera klaidos atliekant POST uzklausa, atliekamas sis metodas
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);      // apibreziamas JSON objektas ir perduodama uzklausos atsakymo reiksme
                            if(!obj.getBoolean("error")){               // jei JSON atsakymas nera error, tada naudotojas prisijungia
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getInt("id"),
                                                obj.getString("username"),
                                                obj.getString("email")
                                        );
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));  // atsidaro ProfileActivity langas
                                finish();
                            }else{                                          // jei JSON atsakymas error, parodoma klaidos zinute
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {              // jei yra klaida atliekant POST uzklausa, atliekamas sis metodas
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {     // metodas, kuriuo sudedami reikalingi parametrai i HashMap
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin){            // jei paspaudziamas mygtukas prisijungti
            userLogin();                    // naudotojas prisijungia
        }
    }
}
