package com.etiennelawlor.loop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.etiennelawlor.loop.R;
import com.etiennelawlor.loop.fragments.SearchableFragment;
import com.etiennelawlor.loop.otto.BusProvider;
import com.etiennelawlor.loop.otto.events.UpNavigationClickedEvent;
import com.google.android.gms.actions.SearchIntents;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by etiennelawlor on 9/23/15.
 */
public class SearchableActivity extends AppCompatActivity {

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        ButterKnife.bind(this);

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())
                ||  SearchIntents.ACTION_SEARCH.equals(getIntent().getAction())) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, SearchableFragment.newInstance(getIntent().getExtras()), "")
                    .commit();
        }

        BusProvider.get().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.get().unregister(this);
    }
    // endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
//                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // region Otto Methods
    @Subscribe
    public void onUpNavigationClickedEvent(UpNavigationClickedEvent event) {
        Timber.d("onUpNavigationClickedEvent");
        UpNavigationClickedEvent.Type type = event.getType();

        if(type == UpNavigationClickedEvent.Type.BACK)
            onBackPressed();
    }
    // endregion
}