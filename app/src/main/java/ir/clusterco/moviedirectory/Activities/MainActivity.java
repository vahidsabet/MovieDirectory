package ir.clusterco.moviedirectory.Activities;

import android.app.VoiceInteractor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//add request to web server
        queue= Volley.newRequestQueue(this);

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

    //get movies
    public List<Movie> getMovies(String searchTerm) {
        movieList.clear();

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
                        movie.setImdbId(movieObject.getString("imdbID"));

                        //Log.d("Movies:",movie.getTitle());

                        movieList.add(movie);


                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
        return movieList;

    }
}
