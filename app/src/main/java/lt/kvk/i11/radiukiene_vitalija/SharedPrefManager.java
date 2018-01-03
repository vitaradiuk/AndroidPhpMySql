package lt.kvk.i11.radiukiene_vitalija;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vita on 1/2/2018.
 */

// Singleton pattern; talpinami prisijungusio naudotojo duomenys

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_ID = "userid";


    private SharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String username, String email){        // naudotojo pisijungimo metodas
        // SharedPreferences - interface'as, skirtas gauti ir redaguoti duomenis, kuriuos grąžino getSharedPreferences()
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE); // private - tik si programa gali pasiekti duomenis
        SharedPreferences.Editor editor = sharedPreferences.edit(); // duomenu modifikavimas vyksta per Editor objekta

        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USERNAME, username);

        editor.apply();

        return true;
    }

    public boolean isLoggedIn(){        // patikrinama ar naudotojas yra prisijunges
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME, null) != null){        // jei username nelygu null, tai naudotojas yra prisijunges
            return true;
        }
        return false;
    }

    public boolean logout(){        // metodas naudojamas naudotojui atsijungti nuo programos
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();         // pasalina visas reiksmes is Editor objekto
        editor.apply();
        return true;
    }


    public String getUsername(){        // metodas skirtas gauti naudotojo varda
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getUserEmail(){       // metodas skirtas gauti naudotojo email
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }
}
