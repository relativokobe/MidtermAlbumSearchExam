package com.example.asus.midtermalbumsearchexam;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Album>>{

    private static final String REQUEST_URL = "http://ws.audioscrobbler.com/2.0/?" ;

    private static final int ALBUM_LOADER_ID1 = 1;
    private static final int ALBUM_LOADER_ID2 = 2;
    private int searchBy = 1;
    private TextView indicator;

    private TextView searchTxt;
    private RecyclerView recyclerView;
    private String search = "";
    private AlbumsAdapter albumsAdapter;
    private LoaderManager loaderManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchTxt = (TextView) findViewById(R.id.search);
        indicator = (TextView)findViewById(R.id.empty);

        indicator.setText("No Albums");

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {


        } else {
            indicator.setText("No Connection");
        }

        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.e("kobe","click");
                    setSearch(searchTxt.getText().toString());
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }
    public String getSearch() {
        return search;
    }

    public void performSearch(){
        indicator.setVisibility(View.GONE);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(searchBy, null, this);
        hideSoftKeyboard(MainActivity.this);

    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public Loader<ArrayList<Album>> onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if(id == ALBUM_LOADER_ID1) {
            uriBuilder.appendQueryParameter("method", "artist.gettopalbums");
            uriBuilder.appendQueryParameter("artist", getSearch()); // edit later
            uriBuilder.appendQueryParameter("api_key", "490b41d76995ab4e15ca4d9d04e015a9");
            uriBuilder.appendQueryParameter("limit", "50");
            uriBuilder.appendQueryParameter("format", "json");
        }
        else if (id == ALBUM_LOADER_ID2){
            uriBuilder.appendQueryParameter("method", "album.search");
            uriBuilder.appendQueryParameter("album", getSearch()); // edit later
            uriBuilder.appendQueryParameter("api_key", "490b41d76995ab4e15ca4d9d04e015a9");
            //12696eb0c1e42d1b92a4293a54269236
            uriBuilder.appendQueryParameter("limit", "50");
            uriBuilder.appendQueryParameter("format", "json");
        }
        Log.d("kobe",uriBuilder.toString());

        return new AlbumLoader(this,uriBuilder.toString(),id);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Album>> loader, ArrayList<Album> data) {
        recyclerView.setVisibility(View.VISIBLE);
        Log.e("kobe","daa size "+data.size());
        albumsAdapter = new AlbumsAdapter(this,data);
        if(data != null && !data.isEmpty()){
            indicator.setVisibility(View.GONE);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(albumsAdapter);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            indicator.setVisibility(View.VISIBLE);
        }
        getLoaderManager().destroyLoader(searchBy);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.clear){
            searchTxt.setText("");
        }
        if(id == R.id.artistName){
            indicator.setText("Search album by artist");
            searchBy = 1;
        }
        if(id == R.id.albumName){
            indicator.setText("Search album by name");
            searchBy = 2;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Album>> loader) {

    }
}
