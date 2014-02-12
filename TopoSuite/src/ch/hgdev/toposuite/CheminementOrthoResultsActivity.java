package ch.hgdev.toposuite;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CheminementOrthoResultsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheminement_ortho_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cheminement_ortho_results, menu);
        return true;
    }

}
