package mahmoud.com.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by mahmoud on 3/27/2017.
 */
public class MoviesAuthenticatorService extends Service {

    private MoviesAuthenticator mAuthenticator;


    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator = new MoviesAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mAuthenticator.getIBinder();
    }
}
