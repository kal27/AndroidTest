package developer.test.com.androiddevelopertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import developer.test.com.androiddevelopertest.ResponseClasses.Item;
import developer.test.com.androiddevelopertest.ResponseClasses.Repos;
import developer.test.com.androiddevelopertest.Utils.ApiClient;
import developer.test.com.androiddevelopertest.Utils.ReposArrayAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener, View.OnClickListener {
    private ListView reposList;
    private ArrayList<Item> allItems;
    private int page = 0;
    private final int PER_PAGE = 10;
    private int currentLast;
    private ApiClient restClient;
    private EditText inputEditText;
    private Button button;
    private String currentQuery = "tetris";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //could have used butterknife or databinding library but I'm not sure whether I should
        reposList = (ListView) findViewById(R.id.listView);
        reposList.setOnScrollListener(this);

        button = (Button) findViewById(R.id.request);
        button.setOnClickListener(this);

        inputEditText = (EditText) findViewById(R.id.userInput);

        allItems = new ArrayList<>();

        restClient = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build()
                .create(ApiClient.class);

        loadItems();


    }

    private void loadItems() {
        Call<Repos> reposCall = restClient.searchForRepos(currentQuery, PER_PAGE, page);

        reposCall.enqueue(new Callback<Repos>() {
            @Override
            public void onResponse(Call<Repos> call, Response<Repos> response) {
                Repos responseRepos = response.body();
                if (responseRepos != null) {
                    allItems.addAll(responseRepos.getItems());
                    ReposArrayAdapter reposArrayAdapter = new ReposArrayAdapter(MainActivity.this, allItems);
                    reposList.setAdapter(reposArrayAdapter);
                } else {
                    //limit was reached
                    currentLast = 0;
                    Toast.makeText(MainActivity.this, "You've reached limit of 10 API calls per minute. Please wait a bit.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Repos> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        switch (absListView.getId()) {
            case R.id.listView:
                final int lastItem = firstVisibleItem + visibleItemCount;
                //check whether user has scrolled to the end
                if (lastItem == totalItemCount) {
                    if (currentLast != lastItem) {
                        //to avoid multiple calls for last item
                        currentLast = lastItem;

                        page++;
                        loadItems();
                    }
                }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.request) {
            String input = inputEditText.getText().toString();
            if(!input.isEmpty()) {
                currentQuery = input;

                //reset values
                currentLast = 0;
                page = 0;
                allItems.clear();

                loadItems();
            }
        }

    }
}
