package com.android.samiha.bookapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView list;
    TextView totalCountTextView;
    TextView noThingFound;
    EditText searchText;
    ImageView searchClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.list);
        totalCountTextView = (TextView) findViewById(R.id.total_count);
        noThingFound = (TextView) findViewById(R.id.no_text);
        searchText = (EditText) findViewById(R.id.search_text);
        searchClick = (ImageView) findViewById(R.id.search_click);

        searchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (searchText.getText().toString().isEmpty()) {
                BookAsync task = new BookAsync();
                task.execute(searchText.getText().toString());
                Log.v("WE_DID", searchText.getText().toString());
//                } else
//                    Toast.makeText(MainActivity.this, "What you would like to read, Please enter on the Search bar.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class BookAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder jsonData = new StringBuilder();
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;

            try {
                String query = URLEncoder.encode(strings[0], "UTF-8");
                String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query + "/";
                URL url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();

                while (line != null) {
                    jsonData.append(line);
                    line = reader.readLine();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return jsonData.toString();
        }

        @Override
        protected void onPostExecute(String o) {
            if (o != null || o.isEmpty())
                UpdateUI(o);
            else
                Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateUI(String s) {
        BookArrayAdapter adapter;
        try {
            JSONObject root = new JSONObject(s);
            int totalItems = root.getInt("totalItems");

            if (totalItems > 0) {
                noThingFound.setVisibility(View.GONE);
                JSONArray jsonBooks = root.getJSONArray("items");
                ArrayList<Book> books = new ArrayList<>();
                for (int i = 0; i < jsonBooks.length(); i++) {
                    ArrayList<String> authors = new ArrayList<>();

                    JSONObject jsonBook = jsonBooks.getJSONObject(i);
                    JSONObject jsonInfo = jsonBook.getJSONObject("volumeInfo");
                    String title;
                    if (jsonInfo.has("title"))
                        title = jsonInfo.getString("title");
                    else
                        title = "No title";
                    String description;
                    if (jsonInfo.has("description"))
                        description = jsonInfo.getString("description");
                    else
                        description = "No Description";

                    String author;
                    if (jsonInfo.has("authors")) {
                        JSONArray authorJsonArray = jsonInfo.getJSONArray("authors");
                        for (int j = 0; j < authorJsonArray.length(); j++) {
                            author = authorJsonArray.getString(j);
                            authors.add(author);
                        }
                    } else
                        author = "No Author";

                    Book book = new Book(title, authors, description);
                    books.add(book);
                }
                BookSearchedList bookList = new BookSearchedList(totalItems, books);
                adapter = new BookArrayAdapter(this, R.layout.book_list, bookList.getBooks());
                list.setAdapter(adapter);
                list.setVisibility(View.VISIBLE);
            } else {
                totalItems = 0;
                noThingFound.setVisibility(View.VISIBLE);
                list.setVisibility(View.INVISIBLE);

            }
            totalCountTextView.setText("Total Result : " + String.valueOf(totalItems));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

