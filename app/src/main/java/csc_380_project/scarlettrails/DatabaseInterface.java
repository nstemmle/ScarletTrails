package csc_380_project.scarlettrails;

/**
 * Created by Roach on 10/14/2014.
 */
interface DatabaseInterface {

    public void query(String lookup);
    //Where is the lookup string generated? Should it be generated in this method or passed to this method as a parameter?

    public void updateData();

    public String insertData();

}
