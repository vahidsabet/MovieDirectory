package ir.clusterco.moviedirectory.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.ContentResolver;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.clusterco.moviedirectory.Data.MovieRecyclerViewAdapter;
import ir.clusterco.moviedirectory.Model.Movie;
import ir.clusterco.moviedirectory.R;
import ir.clusterco.moviedirectory.Util.Constants;
import ir.clusterco.moviedirectory.Util.Prefs;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    public  ProgressDialog dialogBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//add request to web server
        queue= Volley.newRequestQueue(this);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList=new ArrayList<>();

        Prefs prefs=new Prefs(MainActivity.this);
        String search=prefs.getSearch();
       // getMovies(search);
        movieList=getMovies(search);


        movieRecyclerViewAdapter =new MovieRecyclerViewAdapter(this,movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();


    }

    public void showLoadingDialog() {


        if (dialogBox == null) {
            dialogBox = new ProgressDialog(this);
            dialogBox.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogBox.setMessage("Loading. Please wait...");
        }
        dialogBox.show();
    }

    public void dismissLoadingDialog() {

        if (dialogBox != null && dialogBox.isShowing()) {
            dialogBox.dismiss();
        }
    }

    //get movies

    public List<Movie> getMovies(String searchTerm) {

        showLoadingDialog();
        movieList.clear();

      /*  if (dialogBox == null) {
            dialogBox = new ProgressDialog(this)); // this = YourActivity
            dialogBox.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogBox.setMessage("Loading. Please wait...");
           // dialogBox.setIndeterminate(true);
            dialogBox.setCanceledOnTouchOutside(false);
            dialogBox.show();
        }*/
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                Constants.URL_LEFT + searchTerm + Constants.URL_RIGHT, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray moviesArray=response.getJSONArray("Search");

                    for ( int i=0;i<moviesArray.length();i++){
                        JSONObject movieObject=moviesArray.getJSONObject(i);

                        Movie movie=new Movie();
                        movie.setTitle(movieObject.getString("Title"));
                        movie.setYear("Release Year: "+movieObject.getString("Year"));
                        movie.setMovieType("Type: " + movieObject.getString("Type"));
                        movie.setPoster(movieObject.getString("Poster"));
                     //imageView.(movieObject.getString("Poster"));

                        movie.setImdbId(movieObject.getString("imdbID"));

                        Log.d("Movies:",movie.getPoster());

                        movieList.add(movie);


                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                dismissLoadingDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);

        return movieList;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_search) {
            showInputDialog();
            // return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void showInputDialog() {

        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialof_view, null);
        final EditText newSearchEdt = (EditText) view.findViewById(R.id.searchEdt);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs = new Prefs(MainActivity.this);

                if (!newSearchEdt.getText().toString().isEmpty()) {

                    String search = newSearchEdt.getText().toString();
                    prefs.setSearch(search);
                    movieList.clear();

                    getMovies(search);

                    movieRecyclerViewAdapter.notifyDataSetChanged();//Very important!!
                }
                dialog.dismiss();


            }
        });
    }
}
