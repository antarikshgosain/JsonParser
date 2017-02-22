package antarikshgosain.jsonparser.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Antariksh Gosain on 2/16/2017.
 */

public class MoviesModel {
    private String story;
    private String duration;
    private String image;
    private String movie;
    private int year;
    private String rating;
    private String director;
    private String tagline;


    @SerializedName("cast")
    private List<Cast> castList;
    public static class Cast{
        private String name ;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public List<Cast> getCastList() {
        return castList;
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    public String getStory ()
    {
        return story;
    }

    public void setStory (String story)
    {
        this.story = story;
    }

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

//    public List[] getCast ()
//    {
//        return cast;
//    }
//
//    public void setCast (List[] cast)
//    {
//        this.cast = cast;
//    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getMovie ()
    {
        return movie;
    }

    public void setMovie (String movie)
    {
        this.movie = movie;
    }

    public int getYear ()
    {
        return year;
    }

    public void setYear (int year)
    {
        this.year = year;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public String getDirector ()
    {
        return director;
    }

    public void setDirector (String director)
    {
        this.director = director;
    }

    public String getTagline ()
    {
        return tagline;
    }

    public void setTagline (String tagline)
    {
        this.tagline = tagline;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [story = "+story+", duration = "+duration+", image = "+image+", movie = "+movie+", year = "+year+", rating = "+rating+", director = "+director+", tagline = "+tagline+"]";
    }

}
