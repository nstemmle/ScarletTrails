package csc_380_project.scarlettrails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

/**
 * Created by Scott on 11/24/2014.
 */
public class RatingDialogFragment extends DialogFragment {

    private Double rating;

    private RatingBar rb;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_ratings, null);
        builder.setView(dialogView);

        //create ratings bar
        rb = ((RatingBar)dialogView.findViewById(R.id.trail_rating_dialog_bar));
        //Change colors
        LayerDrawable stars = (LayerDrawable) rb.getProgressDrawable();
        //Fully shaded color (4/5 rating = 4 stars shaded)
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        //Partially shaded color
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        //No shade color
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        builder.setPositiveButton(R.string.trail_rating_dialog_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                 // User clicked ok button
                //this is where we check if user has already made a rating and then add it to the database

                //and this is what we do if user has already rated a trail
                RatingFunctions ratingFunctions = new RatingFunctions();
                ratingFunctions.setRating(App.getProfileId()
                                        , ActivityTrailTabHostTest.mTrail.getTrailId()
                                        , Double.toString(rb.getRating()));
                Context context = App.getContext();
                CharSequence text = "Rated Sucessfully!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        builder.setNegativeButton(R.string.trail_rating_dialog_negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked cancel button, so far nothing.
            }
        });

        return builder.create();
    }
}
