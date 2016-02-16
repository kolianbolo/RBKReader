package ru.bolobanov.rbkreader.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import ru.bolobanov.rbkreader.Article;
import ru.bolobanov.rbkreader.ArticleAdapter;
import ru.bolobanov.rbkreader.BusProvider;
import ru.bolobanov.rbkreader.Constants;
import ru.bolobanov.rbkreader.LoadService_;
import ru.bolobanov.rbkreader.R;
import ru.bolobanov.rbkreader.database.RbkDatabaseHelper;

/**
 * Created by Bolobanov Nikolay on 10.12.15.
 */
@EActivity(R.layout.a_list)
public class ListActivity extends RbkActivity {

    @ViewById
    ListView list;

    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;

    @AfterViews
    protected void init() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startService(new Intent(ListActivity.this, LoadService_.class));
            }
        });
        listInit();
    }

    public void onResume(){
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    public void listInit() {
        list.setAdapter(new ArticleAdapter(new RbkDatabaseHelper(this).getArticles(), this));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = (Article) parent.getItemAtPosition(position);
                Intent intent = new Intent(ListActivity.this, WebActivity_.class);
                intent.putExtra(Constants.LINK_KEY, article.mLink);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void getMessage(String message) {
        if ("refreshed".equals(message)) {
            listInit();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
