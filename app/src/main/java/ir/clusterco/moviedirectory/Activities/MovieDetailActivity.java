package ir.clusterco.moviedirectory.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.clusterco.moviedirectory.Model.Movie;
import ir.clusterco.moviedirectory.R;
import ir.clusterco.moviedirectory.Util.Constants;

public class MovieDetailActivity extends AppCompatActivity {
    private Movie movie;
    private TextView movieTitle;
    private ImageView movieImage;
    private TextView movieYear;
    private TextView director;
    private TextView actors;
    private TextView Genre;
    private TextView rating;
    private TextView writers;
    private TextView plot;
    private TextView boxOffice;
    private TextView runtime;

    private RequestQueue queue;
    private String movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        queue= Volley.newRequestQueue(this);
        movie=(Movie) getIntent().getSerializableExtra("movie");
        movieId=movie.getImdbId();

        //get IDS of controls
        setupUI();
        getMovieDetails(movieId);
       // movieTitle.setText(Constants.URL + movieId +Constants.URL_RIGHT);

    }

    private void getMovieDetails(String id) {
        //send request to webServer to get data
                 JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                Constants.URL + id+Constants.URL_RIGHT, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    try{

                        //if has Rating. Just for Rating
                          if(response.has("Rating")){

                              JSONArray ratings=response.getJSONArray("Ratings");
                              String source=null;
                              String value=null;
                              if(ratings.length()>0){
                                  //get second object
                                  JSONObject mRating=ratings.getJSONObject(ratings.length()-1);
                                  source=mRating.getString("Source");
                                  value=mRating.getString("Value");
                                  rating.setText(source+":"+value);
                              }
                              else
                              {
                                  rating.setText("Rating NA");
                              }
                          }
                            //  Toast.makeText(MovieDetailActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                              movieTitle.setText(response.getString("Title"));
                             Genre.setText("Genre: " + response.getString("Genre"));
                              movieYear.setText("Released: " + response.getString("Released"));
                              director.setText("Director: " + response.getString("Director"));
                              writers.setText("Writers: " + response.getString("Writer"));
                              plot.setText("Plot: " + response.getString("Plot"));
                              runtime.setText("Runtime: " + response.getString("Runtime"));
                              actors.setText("Actors: " + response.getString("Actors"));

                              Glide.with(getApplicationContext())
                                      .load(response.getString("Poster"))
                                      .into(movieImage);

                              boxOffice.setText("Box Office: " + response.getString("BoxOffice"));



                    }catch (JSONException e){
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error",error.getMessage());
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void setupUI() {

        movieTitle = (TextView) findViewById(R.id.movieTitleIDDets);
        movieImage = (ImageView) findViewById(R.id.movieImageIDDet);
        movieYear = (TextView) findViewById(R.id.movieReleaseIDDets);
        director = (TextView) findViewById(R.id.directedByDet);
        Genre = (TextView) findViewById(R.id.movieGenreIDDet);
        rating = (TextView) findViewById(R.id.movieRatingIDDet);
        writers = (TextView) findViewById(R.id.writersDet);
        plot = (TextView) findViewById(R.id.plotDet);
        boxOffice = (TextView) findViewById(R.id.boxOfficeDet);
        runtime = (TextView) findViewById(R.id.runtimeDet);
        actors = (TextView) findViewById(R.id.actorsDet);
    }


}
