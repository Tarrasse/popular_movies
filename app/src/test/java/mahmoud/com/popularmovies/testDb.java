package mahmoud.com.popularmovies;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import mahmoud.com.popularmovies.data.DBHelper;

/**
 * Created by mahmoud on 3/29/2017.
 */
public class testDb extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        DBHelper helper = new DBHelper(context);
        helper.getWritableDatabase();

    }
}
