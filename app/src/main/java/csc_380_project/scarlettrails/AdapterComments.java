package csc_380_project.scarlettrails;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterComments extends ArrayAdapter<Comment> {

    private final Context context;
    private final ArrayList<Comment> commentsArrayList;

    public AdapterComments(Context context, ArrayList<Comment> commentsArrayList) {

        super(context, R.layout.activity_comments_list,  commentsArrayList);

        this.context = context;
        this.commentsArrayList = commentsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get commentView from inflater
        View commentView = inflater.inflate(R.layout.activity_comments_list, parent, false);

        // 3. Get the two text view from the commentView
        TextView commentOwner = (TextView) commentView.findViewById(R.id.commentOwner);
        TextView commentDate = (TextView) commentView.findViewById(R.id.commentDate);
        TextView commentText = (TextView) commentView.findViewById(R.id.comment);

        // 4. Set the text for textView
        commentOwner.setText(commentsArrayList.get(position).getProfileUsername());
        commentDate.setText(commentsArrayList.get(position).getDateSubmitted());
        commentText.setText(commentsArrayList.get(position).getCommentText());

        // 5. return commentView
        return commentView;
    }

    public ArrayList<Comment> getCommentsArrayList() {
        return commentsArrayList;
    }
}