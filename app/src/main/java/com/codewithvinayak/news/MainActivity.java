package com.codewithvinayak.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.jacksonandroidnetworking.JacksonParserFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // TODO : set the API_KEY variable to your api key
    private static String API_KEY="b24d6eb15d264f1a8098978fa597fd20";

    // setting the TAG for debugging purposes
    private static String TAG="MainActivity";

    // declaring the views
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;


    private ArrayList<NewsArticle> mArticleList;

    private ArticleAdapter mArticleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       AndroidNetworking.initialize(getApplicationContext());


        AndroidNetworking.setParserFactory(new JacksonParserFactory());


        mProgressBar=(ProgressBar)findViewById(R.id.progressbar_id);
        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_id);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mArticleList=new ArrayList<>();


        get_news_from_api();
    }

    public void get_news_from_api(){

        mArticleList.clear();


        AndroidNetworking.get("https://newsapi.org/v2/top-headlines")
                .addQueryParameter("country", "in")
                .addQueryParameter("apiKey",API_KEY)
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {

                        mProgressBar.setVisibility(View.GONE);


                        try {


                            JSONArray articles=response.getJSONArray("articles");


                            for (int j=0;j<articles.length();j++)
                            {

                                JSONObject article=articles.getJSONObject(j);


                                NewsArticle currentArticle=new NewsArticle();


                                String author=article.getString("author");
                                String title=article.getString("title");
                                String description=article.getString("description");
                                String url=article.getString("url");
                                String urlToImage=article.getString("urlToImage");
                                String publishedAt=article.getString("publishedAt");
                                String content=article.getString("content");


                                currentArticle.setAuthor(author);
                                currentArticle.setTitle(title);
                                currentArticle.setDescription(description);
                                currentArticle.setUrl(url);
                                currentArticle.setUrlToImage(urlToImage);
                                currentArticle.setPublishedAt(publishedAt);
                                currentArticle.setContent(content);

                                mArticleList.add(currentArticle);
                            }


                            mArticleAdapter=new ArticleAdapter(getApplicationContext(),mArticleList);
                            mRecyclerView.setAdapter(mArticleAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.d(TAG,"Error : "+e.getMessage());
                        }

                    }
                    @Override
                    public void onError(ANError error) {

                        Log.d(TAG,"Error detail : "+error.getErrorDetail());
                        Log.d(TAG,"Error response : "+error.getResponse());
                    }
                });
    }

    public static class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
        // setting the TAG for debugging purposes
        private static String TAG="com.codewithvinayak.mynewapp.MainActivity.ArticleAdapter";

        private ArrayList<NewsArticle> mArrayList;
        private Context mContext;

        public ArticleAdapter(Context context,ArrayList<NewsArticle> list){
            // initializing the constructor
            this.mContext=context;
            this.mArrayList=list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(mContext).inflate(R.layout.article_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            NewsArticle currentArticle=mArrayList.get(position);


            holder.title.setText(currentArticle.getTitle());
            holder.description.setText(currentArticle.getDescription());


            holder.contributordate.setText(currentArticle.getAuthor()+
                    " | "+currentArticle.getPublishedAt().substring(0,10));


            holder.image.setDefaultImageResId(R.drawable.ic_launcher_background);
            holder.image.setErrorImageResId(R.drawable.ic_launcher_foreground);
            holder.image.setImageUrl(currentArticle.getUrlToImage());


            holder.image.setContentDescription(currentArticle.getContent());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url_key",currentArticle.getUrl());


                    mContext.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{


            private TextView title,description,contributordate;
            private ANImageView image;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                title=itemView.findViewById(R.id.title_id);
                description=itemView.findViewById(R.id.description_id);
                image=itemView.findViewById(R.id.image_id);
                contributordate=itemView.findViewById(R.id.contributordate_id);
            }
        }

    }
}
