package views.formdata;

import java.util.Calendar;
import java.util.Date;
import play.Logger;
import java.text.SimpleDateFormat;

/**
 * Created by Robin on 18/04/14.
 */
public class MyCarFormData {
    
    public String type = "";
    public String description="";
    public String price="";
    public String date="";
    public String dateDay = "";
    public String dateMonth = "";
    public String dateYear = "";
    public String id="";
    public String remove = "";
    public Integer link=0;
    
    public MyCarFormData(){}

    public MyCarFormData(int id, Integer price, long time, String type, String description, Integer link){

        this.date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(time));
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        this.id = String.valueOf(id);
        this.description = (description == null ? "" : description);
        this.type = (type == null ? "" : type);
        this.price = price.toString();
        this.dateDay = String.format("%02d",cal.get(Calendar.DAY_OF_MONTH));
        this.dateMonth = String.format("%02d",cal.get(Calendar.MONTH)+1);
        this.dateYear = String.format("%04d",cal.get(Calendar.YEAR));
        this.link = link == null ? 0 : link;

    }

}
