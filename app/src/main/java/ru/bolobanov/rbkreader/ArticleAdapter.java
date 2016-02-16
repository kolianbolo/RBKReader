package ru.bolobanov.rbkreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bolobanov Nikolay on 11.12.15.
 */
public class ArticleAdapter extends BaseAdapter{

    private List<Article> mList;
    private Context mContext;

    public ArticleAdapter(List<Article> pList, Context pContext){
        mList = pList;
        mContext = pContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.i_article, parent, false);
            ViewHolder viewHolder = new ViewHolder((TextView) convertView.findViewById(R.id.titleText));
            convertView.setTag(viewHolder);
        }

        Article article = mList.get(position);
        ViewHolder holder = (ViewHolder)convertView.getTag();
        holder.mTitleText.setText(article.mTitle);
        return convertView;
    }

    class ViewHolder{
        public final TextView mTitleText;
        public ViewHolder(TextView pTitleText){
            mTitleText = pTitleText;

        }
    }
}
