package lt.kvk.i11.radiukiene_vitalija;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {


    private TextView textViewUsername, textViewUserEmail;   // apibreziami GUI vaizdiniai objektai


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){          // jei naudotojas nera prisijunges
            finish();                                                   // ProfileActivity sustabdoma
            startActivity(new Intent(this, LoginActivity.class));   // atidaromas LoginActivity langas
        }

        textViewUsername = (TextView) findViewById(R.id.textViewUsername);  // inicializuojami views is xml failo
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);


        textViewUserEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());      // vaizduojami duomenys is DB i textview
        textViewUsername.setText(SharedPrefManager.getInstance(this).getUsername());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {         // pridedamas menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       // paspaudziamas menu item
        switch(item.getItemId()){
            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).logout();
                finish();                                       // atsijungiama is sito lango
                startActivity(new Intent(this, LoginActivity.class));      // atidaromas LogoutActivity langas
                break;
        }
        return true;
    }
}
