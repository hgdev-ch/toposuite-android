package ch.hgdev.toposuite.entry;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import ch.hgdev.toposuite.R;

/**
 * 
 * @author HGdev
 *
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}