package iit.valentinpichavant.newsgateway;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {

    static final String BROADCAST_SOURCES = "BROADCAST_SOURCES";
    static final String BROADCAST_ARTICLES = "BROADCAST_ARTICLES";
    static final String DATA_SOURCES = "DATA_SOURCES";
    static final String DATA_ARTICLES = "DATA_ARTICLES";

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] items;
    private volatile ArrayList<Source> sourcesList;
    private ArrayList<Article> articlesList;
    private ArrayList<String> categoryList;
    private Menu menu;
    private MainActivity mainActivity;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragmentList;
    private RequestSourceReceiver requestSourceReceiver;
    private RequestArticleReceiver requestArticleReceiver;
    private String actionBarName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mainActivity = this;
        Intent intent = new Intent(MainActivity.this, NewsGatewayService.class);
        startService(intent);
        this.actionBarName = "News Gateway";
        this.requestSourceReceiver = new RequestSourceReceiver(this);
        IntentFilter filter = new IntentFilter(BROADCAST_SOURCES);
        registerReceiver(requestSourceReceiver, filter);

        this.requestArticleReceiver = new RequestArticleReceiver(this);
        IntentFilter filter2 = new IntentFilter(BROADCAST_ARTICLES);
        registerReceiver(requestArticleReceiver, filter2);

        if (savedInstanceState == null) {
            this.sourcesList = new ArrayList<>();
            this.articlesList = new ArrayList<>();
            this.categoryList = new ArrayList<>();
            this.fragmentList = new ArrayList<>();
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.left_drawer);
            mDrawerToggle = new ActionBarDrawerToggle(
                    this,                  /* host Activity */
                    mDrawerLayout,         /* DrawerLayout object */
                    R.string.drawer_open,  /* "open drawer" description for accessibility */
                    R.string.drawer_close  /* "close drawer" description for accessibility */
            );

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

        } else {
            this.sourcesList = new ArrayList<>();
            this.articlesList = (ArrayList<Article>) savedInstanceState.getSerializable("articlesList");
            this.categoryList = (ArrayList<String>) savedInstanceState.getSerializable("categoryList");
            this.fragmentList = new ArrayList<>();
            this.actionBarName = savedInstanceState.getString("actionBarName");
            savedInstanceState.remove("articlesList");
            savedInstanceState.remove("categoryList");
            savedInstanceState.remove("actionBarName");
            getSupportActionBar().setTitle(actionBarName);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.left_drawer);
            mDrawerToggle = new ActionBarDrawerToggle(
                    this,                  /* host Activity */
                    mDrawerLayout,         /* DrawerLayout object */
                    R.string.drawer_open,  /* "open drawer" description for accessibility */
                    R.string.drawer_close  /* "close drawer" description for accessibility */
            );

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("articlesList", this.articlesList);
        outState.putSerializable("categoryList", this.categoryList);
        outState.putSerializable("actionBarName", this.actionBarName);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(requestSourceReceiver);
        unregisterReceiver(requestArticleReceiver);
        Intent intent = new Intent(MainActivity.this, NewsGatewayService.class);
        stopService(intent);
        super.onDestroy();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        this.menu = menu;
        if (sourcesList.isEmpty()) {
            sendSourceRequest();
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menuAll:
                items = new String[sourcesList.size()];
                for (int i = 0; i < items.length; i++)
                    items[i] = sourcesList.get(i).getName();
                mDrawerList.setAdapter(new ArrayAdapter<>(mainActivity,
                        R.layout.drawer_list_item, items));
                mDrawerList.setOnItemClickListener(
                        new ListView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                selectItem(position);
                            }
                        }
                );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void selectItem(int position) {
        for (Source source : sourcesList) {
            if (items[position].equals(source.getName())) {
                this.actionBarName = source.getName();
                getSupportActionBar().setTitle(actionBarName);
                sendArticleRequest(source.getId());
            }
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public List<Source> getSourcesList() {
        return sourcesList;
    }

    public void setSourcesList(ArrayList<Source> sourcesList) {
        this.sourcesList = sourcesList;
        this.categoryList = new ArrayList<>();
        for (Source source : sourcesList) {
            if (!categoryList.contains(source.getCategory())) {
                categoryList.add(source.getCategory());
                addMenu(source.getCategory());
            }
        }
        items = new String[sourcesList.size()];
        for (int i = 0; i < items.length; i++)
            items[i] = sourcesList.get(i).getName();

        mDrawerList.setAdapter(new ArrayAdapter<>(mainActivity,
                R.layout.drawer_list_item, items));
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                }
        );
        mDrawerLayout.setVisibility(View.INVISIBLE);
        mDrawerLayout.setVisibility(View.VISIBLE);
    }

    public List<Article> getArticlesList() {
        return articlesList;
    }

    public void setArticlesList(List<Article> articlesList) {
        this.articlesList.clear();
        if (fragmentList != null) {
            for (Fragment frag : fragmentList) {
                getSupportFragmentManager().beginTransaction().remove(frag).commit();
            }
        }
        this.articlesList = (ArrayList<Article>) articlesList;
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void addMenu(String category) {
        MenuItem item = menu.add(category);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                List<Source> tmpList = new ArrayList<Source>();
                for (Source source : sourcesList) {
                    if (item.getTitle().equals(source.getCategory())) {
                        tmpList.add(source);
                    }
                }
                items = new String[tmpList.size()];
                for (int i = 0; i < items.length; i++)
                    items[i] = tmpList.get(i).getName();

                mDrawerList.setAdapter(new ArrayAdapter<>(mainActivity,
                        R.layout.drawer_list_item, items));
                mDrawerList.setOnItemClickListener(
                        new ListView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                selectItem(position);
                            }
                        }
                );
                return true;
            }
        });
    }

    public void sendSourceRequest() {
        Intent intent = new Intent();
        intent.setAction(BROADCAST_SOURCES);
        sendBroadcast(intent);
    }

    public void sendArticleRequest(String source) {
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ARTICLES);
        intent.putExtra("source", source);
        sendBroadcast(intent);
    }

    public void articleClicked(View v) {
        int index = mViewPager.getCurrentItem();
        String url = articlesList.get(index).getUrl();
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, Article article, int size) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt("size", size);
            args.putSerializable("article", article);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Bundle args = getArguments();
            if (args != null) {
                final Article article = (Article) args.getSerializable("article");
                int sectionNumber = args.getInt(ARG_SECTION_NUMBER);
                int size = args.getInt("size");
                if (article != null) {
                    TextView textViewTitle = (TextView) rootView.findViewById(R.id.textViewArticleTitle);
                    textViewTitle.setText(article.getTitle());
                    TextView textViewContent = (TextView) rootView.findViewById(R.id.textViewArticleContent);
                    textViewContent.setText(article.getDescription());
                    if (article.getUrlToImage() != null) {
                        Picasso picasso = new Picasso.Builder(getContext()).listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) { // Here we try https if the http image attempt failed
                                final String changedUrl = article.getUrlToImage().replace("http:", "https:");
                                picasso.load(changedUrl).into((ImageView) rootView.findViewById(R.id.imageViewArticle));
                            }
                        }).build();
                        picasso.load(article.getUrlToImage()).into((ImageView) rootView.findViewById(R.id.imageViewArticle));
                    } else {
                    }
                    TextView textViewNumber = (TextView) rootView.findViewById(R.id.textViewArticleNumber);
                    textViewNumber.setText(sectionNumber + " of " + size);
                    TextView textViewDate = (TextView) rootView.findViewById(R.id.textViewDate);
                    if(article.getPublishedAt() != null) {
                        textViewDate.setText(article.getPublishedAt().replace("T"," ").replace("Z",""));
                    }else{
                        ((LinearLayout)textViewDate.getParent()).removeView(textViewDate);
                    }
                    TextView textViewAuthor = (TextView) rootView.findViewById(R.id.textViewAuthor);
                    if(article.getAuthor() != null && !"null".equals(article.getAuthor())) {
                        textViewAuthor.setText(article.getAuthor());
                    }else{
                        ((LinearLayout)textViewDate.getParent()).removeView(textViewAuthor);
                    }
                }
            }
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = PlaceholderFragment.newInstance(position + 1, articlesList.get(position), articlesList.size());
            fragmentList.add(frag);
            return frag;
        }

        @Override
        public int getCount() {
            // Show total pages.
            return articlesList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return articlesList.get(position).getTitle();
        }
    }
}
