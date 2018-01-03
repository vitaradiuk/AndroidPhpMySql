package lt.kvk.i11.radiukiene_vitalija;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Vita on 1/2/2018.
 */

// Singleton klase is https://developer.android.com/training/volley/requestqueue.html

public class RequestHandler {                   // Singleton Pattern: rekomenduojama naudoti, kai programa nuolat naudoja tinklo paslaugas; Tai paternas naudojamas, kai sistemoje reikia vieno globalaus objekto, kurį galima naudoti iš bet kurios kodo vietos.
    private static RequestHandler mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private RequestHandler(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RequestHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestHandler(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {         // metodas grazina requestqueue; Singleton pattern, sukuriamas uzklausos objektas, kuris gyvuoja tol kol veikia programa
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {         // metodas prideda uzklausos objekta prie requestqueue
        getRequestQueue().add(req);
    }

}
