package id.ac.umn.keburusarjanainc;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FetchFromJSON extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ArticlesList> articlesLists;

    private static final String URL_DATA = "http://ultimagz.com/wp-json/wp/v2/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_from_json);

        recyclerView = findViewById(R.id.newfeed_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        articlesLists = new ArrayList<>();
        loadUrlData();
    }

    private void loadUrlData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading . . .");
        progressDialog.show();
        
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    for(int i=0; i<jsonObject.length(); i++){
                        JSONArray array = jsonObject.getJSONArray(String.valueOf(i));
//                        JSONObject jo = array.getJSONObject(i);
                        JSONArray jo_title = array.getJSONArray(10);
                        JSONArray jo_image = array.getJSONArray(23);
                        JSONArray jo_content = array.getJSONArray(12);
                        ArticlesList articles = new ArticlesList(jo_title.getString(0), jo_image.getString(7), jo_content.getString(0));
                        articlesLists.add(articles);
                    }
                    adapter = new ArticlesAdapter(articlesLists, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FetchFromJSON.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}