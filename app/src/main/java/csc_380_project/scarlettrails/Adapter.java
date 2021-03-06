package csc_380_project.scarlettrails;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Adapter extends ArrayAdapter<Trail> {

    private final Context context;
    private final ArrayList<Trail>  trailsArrayList;

    public Adapter(Context context, ArrayList<Trail> trailsArrayList) {

        super(context, R.layout.activity_trails_list, trailsArrayList);

        this.context = context;
        this.trailsArrayList = trailsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get trailView from inflater
        View trailView = inflater.inflate(R.layout.activity_trails_list, parent, false);

        // 3. Get the two text view from the trailView
        TextView trailNameView = (TextView) trailView.findViewById(R.id.trailName);
        TextView trailParkView = (TextView) trailView.findViewById(R.id.trailPark);
        TextView trailLengthView = (TextView) trailView.findViewById(R.id.trailLength);

        // 4. Set the text for textView
        trailNameView.setText(trailsArrayList.get(position).getName());
        trailParkView.setText(trailsArrayList.get(position).getPark().isEmpty() ||
                              trailsArrayList.get(position).getPark() == null ? "" :
                              trailsArrayList.get(position).getPark() + " - ");
        trailLengthView.setText(String.valueOf(trailsArrayList.get(position).getLength()) +" ft");

        // 5. retrn trailView
        return trailView;
    }

    public ArrayList<Trail> getTrailsArrayList() {
        return trailsArrayList;
    }
}