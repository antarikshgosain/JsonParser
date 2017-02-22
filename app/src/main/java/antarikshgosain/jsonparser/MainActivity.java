package antarikshgosain.jsonparser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import antarikshgosain.jsonparser.model.MoviesModel;

public class MainActivity extends AppCompatActivity {

    //private TextView tv_data;
    //private final TextView tv_data1 = (TextView)findViewById(R.id.textView1);
    ListView lvmovies ;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvmovies = (ListView)findViewById(R.id.lv);
        final Button btn_hit = (Button)findViewById(R.id.btn_hit);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);  //dont know how long will it run
        progressDialog.setCancelable(false);    //user cant cancel loading
        progressDialog.setMessage("Loading. Please Wait....");
        //TODO without caching
        //ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        //ImageLoader.getInstance().init(config);

        //TODO with caching
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        btn_hit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                btn_hit.setVisibility(View.GONE);
                new JSONTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt");
            }
        });

}
class JSONTask extends AsyncTask<String,String,List<MoviesModel>>{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected List<MoviesModel> doInBackground(String... params) {

            BufferedReader reader = null ;
            HttpURLConnection urlConn = null ;
            URL url = null ;

            try {
                url = new URL(params[0]);
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.connect();
                InputStream inputStream = urlConn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine())!=null){
                    buffer.append(line);
                }

                //return buffer.toString();
                String bigData = buffer.toString();
                StringBuffer finalString = new StringBuffer();

                JSONObject parentObject = new JSONObject(bigData);
                JSONArray parentArray = parentObject.getJSONArray("movies");
                //TODO
                List<MoviesModel> moviesModelList = new ArrayList<>();
                Gson gson = new Gson();
                for(int i=0 ; i<parentArray.length() ; i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    //MoviesModel moviesModel = new MoviesModel(); //TODO this is from JSON parsing
                    MoviesModel moviesModel = gson.fromJson(finalObject.toString(),MoviesModel.class);

                    /**
                    moviesModel.setMovie(finalObject.getString("movie"));
                    moviesModel.setDirector(finalObject.getString("director"));
                    moviesModel.setDuration(finalObject.getString("duration"));
                    moviesModel.setRating(finalObject.getString("rating"));
                    moviesModel.setStory(finalObject.getString("story"));
                    moviesModel.setTagline(finalObject.getString("tagline"));
                    moviesModel.setYear(finalObject.getInt("year"));
                    moviesModel.setImage(finalObject.getString("image"));
                    List <MoviesModel.Cast> castList = new ArrayList<>();
                    for(int j=0 ; j<finalObject.getJSONArray("cast").length() ; j++)
                    {
                        JSONObject castObject = finalObject.getJSONArray("cast").getJSONObject(j);
                        MoviesModel.Cast cast = new MoviesModel.Cast();
                        cast.setName(castObject.getString("name"));
                        castList.add(cast);

                    }
                    moviesModel.setCastList(castList);
                    **/


                    moviesModelList.add(moviesModel); //final object added
//
//                    int y = object.getInt("year");
//                    String year = Integer.toString(y);
//                    String name = object.getString("movie");
//                    finalString.append(name + " : " + year + "\n" );
                }

                return moviesModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(urlConn!=null)
                    urlConn.disconnect();
                try {
                    if(reader!=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return null;
    }

    @Override
    protected void onPostExecute(List<MoviesModel> res) {
        if(res!=null) {
            super.onPostExecute(res);
            MoviesAdapter adapter = new MoviesAdapter(getApplicationContext(), R.layout.row, res);
            lvmovies.setAdapter(adapter);
            progressDialog.dismiss();
            //tv_data.setText(s);
        }
    }
};

    public class MoviesAdapter extends ArrayAdapter{

        public List<MoviesModel> MMList = new ArrayList<MoviesModel>();
        int resource ;
        LayoutInflater layoutInflater ;
        public MoviesAdapter(Context context, int resource, List<MoviesModel> objects)
        {
            super(context, resource, objects);
            MMList=objects;
            this.resource=resource;
            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
            {
                convertView = layoutInflater.inflate(R.layout.row,null);
            }
            ImageView iv_imageView ;
            TextView tv_movieName ;
            TextView tv_tagLine ;
            TextView tv_year ;
            TextView tv_duration ;
            TextView tv_director ;

            RatingBar rb_movie ;
            TextView tv_cast ;
            TextView tv_story;

            iv_imageView = (ImageView)convertView.findViewById(R.id.imageView);
            tv_movieName = (TextView)convertView.findViewById(R.id.textView);
            tv_tagLine = (TextView)convertView.findViewById(R.id.textView2);
            tv_year = (TextView)convertView.findViewById(R.id.textView3);
            tv_duration = (TextView)convertView.findViewById(R.id.textView4);
            tv_director = (TextView)convertView.findViewById(R.id.textView5);
            ImageLoader.getInstance().displayImage(MMList.get(position).getImage(),iv_imageView);
            rb_movie = (RatingBar)convertView.findViewById(R.id.ratingBar);
            tv_cast = (TextView)convertView.findViewById(R.id.textView6);
            tv_story = (TextView)convertView.findViewById(R.id.textView7);

            tv_movieName.setText(MMList.get(position).getMovie());
            tv_tagLine.setText(MMList.get(position).getTagline());
            tv_year.setText("Year : "+MMList.get(position).getYear());
            tv_duration.setText(MMList.get(position).getDuration());
            tv_director.setText(MMList.get(position).getDirector());

            tv_movieName.setText(MMList.get(position).getMovie());

            rb_movie.setRating(Float.valueOf(MMList.get(position).getRating())/2);
            StringBuffer buffer_cast = new StringBuffer() ;
            for(MoviesModel.Cast caster  : MMList.get(position).getCastList())
            {
                buffer_cast.append(caster.getName()+" , ");
            }
            tv_cast.setText(buffer_cast);
            tv_story.setText(MMList.get(position).getStory());

            //return super.getView(position, convertView, parent);
            return convertView ;
        }
    }


}
