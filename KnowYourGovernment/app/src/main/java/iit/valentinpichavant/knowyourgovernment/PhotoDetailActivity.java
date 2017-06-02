package iit.valentinpichavant.knowyourgovernment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {

    private Office office;
    private Official official;
    private String address;

    private ImageView imageViewOfficial;
    private ConstraintLayout constraintLayout;
    private TextView textViewOfficialLocation;
    private TextView textViewOfficialJobTitle;
    private TextView textViewOfficialName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        office = (Office) getIntent().getSerializableExtra("office");
        address = (String) getIntent().getSerializableExtra("address");
        official = office.getOfficial();
        imageViewOfficial = (ImageView) findViewById(R.id.imageViewPhotoDetail);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayoutPhotoDetail);
        textViewOfficialLocation = (TextView) findViewById(R.id.textViewPhotoLocation);
        textViewOfficialJobTitle = (TextView) findViewById(R.id.textViewTitlePhotoDetail);
        textViewOfficialName = (TextView) findViewById(R.id.textViewNamePhotoDetail);
        setData();
    }

    private void setData() {
        setData(textViewOfficialJobTitle, office.getName());
        setData(textViewOfficialName, official.getName());
        setData(textViewOfficialLocation, address);
        setDataParty(official.getParty());
        downloadPhoto(official.getPhotoUrl());
    }

    private void setData(TextView tv, String data) {
        if (data != null) {
            tv.setText(data);
        } else {
            tv.setText("No Data Provided");
        }
    }

    private void setDataParty(String data) {
        constraintLayout.setBackgroundColor(Color.rgb(0, 0, 0));
        if (data != null) {
            if (data.equals("Democratic")) {
                constraintLayout.setBackgroundColor(Color.rgb(0, 0, 255));
            }
            if (data.equals("Republican")) {
                constraintLayout.setBackgroundColor(Color.rgb(255, 0, 0));
            }
        }
    }

    private void downloadPhoto(final String photoUrl) {
        if (photoUrl != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) { // Here we try https if the http image attempt failed
                    final String changedUrl = photoUrl.replace("http:", "https:");
                    picasso.load(changedUrl).error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder).into(imageViewOfficial);
                }
            }).build();
            picasso.load(photoUrl).error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder).into(imageViewOfficial);
        } else {
            Picasso.with(this).load(photoUrl).error(R.drawable.brokenimage).placeholder(R.drawable.missingimage).into(imageViewOfficial);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.putExtra("office", office);
                intent.putExtra("address", address);
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                        // Add all of this activity's parents to the back stack
                        .addNextIntentWithParentStack(intent)
                        // Navigate up to the closest parent
                        .startActivities();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
