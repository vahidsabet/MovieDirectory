package ir.clusterco.moviedirectory.Data;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.PriorityQueue;

import ir.clusterco.moviedirectory.Model.Movie;
import ir.clusterco.moviedirectory.R;

/**
 * Created by VS on 5/21/2018.
 */

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private  Context context;
    private List<Movie> movieList;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies) {
        this.context=context;
        movieList=movies;


    }

    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //get Movie layout
       View view= LayoutInflater.from(parent.getContext())
               .inflate(R.layout.movie_row,parent,false);

       //and set layout to work in ViewHolder
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(MovieRecyclerViewAdapter.ViewHolder holder, int position) {
      //get the position of clicked movie
        Movie movie=movieList.get(position);
        //image link
        String posterLink =movie.getPoster();
        String replacedposterLink = posterLink.replace("https", "http");

        holder.title.setText(movie.getTitle());
        holder.type.setText(movie.getMovieType());


        Picasso.with(context)
                .load(replacedposterLink)
                .placeholder(android.R.drawable.ic_btn_speak_now).fit()
                .into(holder.poster);

        holder.year.setText(movie.getYear());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        ImageView poster;
        TextView year;
        TextView type;

        public ViewHolder(View itemView,Context ctx) {
            super(itemView);
            context = ctx;

            title=(TextView)itemView.findViewById(R.id.movieTitleID);
            poster=(ImageView) itemView.findViewById(R.id.movieImageID);
            year=(TextView)itemView.findViewById(R.id.movieReleaseID);
            type=(TextView)itemView.findViewById(R.id.movieCatID);

            //click on a row and send him to clicked movie
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Row tapped: "+title.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}
