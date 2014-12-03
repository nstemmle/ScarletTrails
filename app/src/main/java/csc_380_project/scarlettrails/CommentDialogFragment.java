package csc_380_project.scarlettrails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Scott on 12/1/2014.
 */
public class CommentDialogFragment extends DialogFragment {

    private EditText comment;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_comments, null);
        builder.setView(dialogView);
        comment   = ((EditText) dialogView.findViewById(R.id.commentField));

        builder.setPositiveButton("Comment", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
           CommentFunctions commentFunctions = new CommentFunctions();
           commentFunctions.setComment(App.getProfileId()
                                     , ActivityTrailTabHostTest.mTrail.getTrailId()
                                     , comment.getText().toString());
                Intent i = new Intent(TabTrail.context,
                        ActivityCommentsList.class);
                i.putExtra("TRAIL_ID", ActivityTrailTabHostTest.mTrail.getTrailId());
                startActivity(i);
           // User clicked comment button
            }
        });
        builder.setNeutralButton("See Comments", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(TabTrail.context,
                        ActivityCommentsList.class);
                i.putExtra("TRAIL_ID", ActivityTrailTabHostTest.mTrail.getTrailId());
                startActivity(i);
                // User clicked comment button
            }
        });
        builder.setNegativeButton(R.string.trail_rating_dialog_negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        return builder.create();
    }
}
