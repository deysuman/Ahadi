package in.ahadi.luci.ahadi;

/**
 * Created by Luci on 3/24/2017.
 */

public class Carts {
    private String title,date,process,token;

    public Carts() {
    }

    public Carts(String name, String datetime,String status,String vtoken) {
        this.title = name;
        this.date = datetime;
        this.process = status;
        this.token = vtoken;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String datetime) {
        this.date = datetime;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String status) {
        this.process = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String vtoken) {
        this.token = vtoken;
    }

}
