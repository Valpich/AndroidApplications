package iit.valentinpichavant.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class OfficialActivity extends AppCompatActivity {

    private Office office;
    private Official official;
    private String address;

    private ConstraintLayout constraintLayout;
    private TextView textViewOfficialLocation;
    private TextView textViewOfficialJobTitle;
    private TextView textViewOfficialName;
    private TextView textViewParty;
    private ImageView imageViewOfficial;
    private TextView textViewAddressOfficialContent;
    private TextView textViewWebsiteOfficialContent;
    private TextView textViewPhoneOfficialContent;
    private TextView textViewEmailOfficialContent;

    private ImageView imageViewYoutube;
    private ImageView imageViewFacebook;
    private ImageView imageViewGooglePlus;
    private ImageView imageViewTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        office = (Office) getIntent().getSerializableExtra("office");
        official = office.getOfficial();
        address = (String) getIntent().getSerializableExtra("address");
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayoutOfficial);
        textViewOfficialLocation = (TextView) findViewById(R.id.textViewOfficialLocation);
        textViewOfficialJobTitle = (TextView) findViewById(R.id.textViewOfficialJobTitle);
        textViewOfficialName = (TextView) findViewById(R.id.textViewOfficialName);
        textViewParty = (TextView) findViewById(R.id.textViewParty);
        imageViewOfficial = (ImageView) findViewById(R.id.imageViewOfficial);
        textViewAddressOfficialContent = (TextView) findViewById(R.id.textViewAddressOfficialContent);
        textViewWebsiteOfficialContent = (TextView) findViewById(R.id.textViewWebsiteOfficialContent);
        textViewPhoneOfficialContent = (TextView) findViewById(R.id.textViewPhoneOfficialContent);
        textViewEmailOfficialContent = (TextView) findViewById(R.id.textViewEmailOfficialContent);
        imageViewYoutube = (ImageView) findViewById(R.id.imageViewYoutube);
        imageViewFacebook = (ImageView) findViewById(R.id.imageViewFacebook);
        imageViewGooglePlus = (ImageView) findViewById(R.id.imageViewGooglePlus);
        imageViewTwitter = (ImageView) findViewById(R.id.imageViewTwitter);
        imageViewYoutube.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                youTubeClicked(v);
            }
        });
        imageViewFacebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                facebookClicked(v);
            }
        });
        imageViewTwitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                twitterClicked(v);
            }
        });
        imageViewYoutube.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                youTubeClicked(v);
            }
        });
        imageViewOfficial.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photoClicked(v);
            }
        });
        setData();
        link();
    }

    public void link() {
        if (official.getOfficeUrl() != null) {
            Linkify.addLinks(textViewWebsiteOfficialContent, Linkify.WEB_URLS);
            textViewWebsiteOfficialContent.setLinkTextColor(Color.rgb(255, 255, 255));
        }
        if (official.getOfficePhone() != null) {
            Linkify.addLinks(textViewPhoneOfficialContent, Linkify.PHONE_NUMBERS);
            textViewPhoneOfficialContent.setLinkTextColor(Color.rgb(255, 255, 255));
        }
        if (!official.getAddress().equals("No Data Provided")) {
            Pattern pattern = Pattern.compile(".*", Pattern.DOTALL);
            Linkify.addLinks(textViewAddressOfficialContent, pattern, "geo:0,0?q=");
            textViewAddressOfficialContent.setLinkTextColor(Color.rgb(255, 255, 255));
        }
        if (official.getEmail() != null) {
            Linkify.addLinks(textViewEmailOfficialContent, Linkify.EMAIL_ADDRESSES);
            textViewEmailOfficialContent.setLinkTextColor(Color.rgb(255, 255, 255));
        }
    }

    private void setData() {
        setData(textViewOfficialLocation, address);
        setData(textViewOfficialJobTitle, office.getName());
        setData(textViewOfficialName, official.getName());
        setDataParty(textViewParty, official.getParty());
        downloadPhoto(official.getPhotoUrl());
        setData(textViewAddressOfficialContent, official.getAddress());
        setData(textViewEmailOfficialContent, official.getEmail());
        setData(textViewWebsiteOfficialContent, official.getOfficeUrl());
        setData(textViewPhoneOfficialContent, official.getOfficePhone());
        setSocials();
    }

    private void setData(TextView tv, String data) {
        if (data != null) {
            tv.setText(data);
        } else {
            tv.setText("No Data Provided");
        }
    }

    private void setDataParty(TextView tv, String data) {
        constraintLayout.setBackgroundColor(Color.rgb(0, 0, 0));
        if (data != null) {
            tv.setText("(" + data + ")");
            if (data.equals("Democratic")) {
                constraintLayout.setBackgroundColor(Color.rgb(0, 0, 255));
            }
            if (data.equals("Republican")) {
                constraintLayout.setBackgroundColor(Color.rgb(255, 0, 0));
            }
        } else {
            tv.setText("(Unknown)");
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

    private void setSocials() {
        if (official.getYoutube() == null) {
            this.imageViewYoutube.setVisibility(View.INVISIBLE);
        } else {

        }
        if (official.getFacebook() == null) {
            this.imageViewFacebook.setVisibility(View.INVISIBLE);
        } else {

        }
        if (official.getGooglePlus() == null) {
            this.imageViewGooglePlus.setVisibility(View.INVISIBLE);
        } else {

        }
        if (official.getTwitter() == null) {
            this.imageViewTwitter.setVisibility(View.INVISIBLE);
        } else {

        }
    }

    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + official.getFacebook();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + official.getFacebook();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClicked(View v) {
        Intent intent = null;
        String name = official.getTwitter();
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
// no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void googlePlusClicked(View v) {
        String name = official.getGooglePlus();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void youTubeClicked(View v) {
        String name = official.getYoutube();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void photoClicked(View v) {
        if (this.official.getPhotoUrl() != null) {
            Intent intent = new Intent(OfficialActivity.this, PhotoDetailActivity.class);
            intent.putExtra("office", office);
            intent.putExtra("address", address);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}