import java.util.Date;

/**
 * Created by vadim on 27.10.2017.
 */
public class Program {

    private String year;
    private String title;
    private String description;
    private String genreTitle;
    private String categoryTitle;
    private Date startDate;
    private Date endDate;

    public Program(String year, String title, String description,
                   String genreTitle, String categoryTitle, Date startDate, Date endDate){

        this.year = year;
        this.title = title;
        this.description = description;
        this.genreTitle = genreTitle;
        this.categoryTitle = categoryTitle;
        this.startDate = startDate;
        this.endDate = endDate;

    }

    @Override
    public String toString() {
        return "YEAR: "+this.year+"\n"
                +"TITLE: "+this.title+"\n"
                +"DESCRIPTION: "+this.description+"\n"
                +"GENRE TITLE: "+this.genreTitle+"\n"
                +"CATEGORY TITLE: "+this.categoryTitle+"\n"
                +"START DATE: "+this.startDate+"\n"
                +"END DATE: "+this.endDate+"\n"
                +"\n";

    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenreTitle() {
        return genreTitle;
    }

    public void setGenreTitle(String genreTitle) {
        this.genreTitle = genreTitle;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
