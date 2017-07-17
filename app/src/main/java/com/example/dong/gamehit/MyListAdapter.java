package com.example.dong.gamehit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ljl on 2017/7/16.
 */
public class MyListAdapter extends BaseAdapter {

    private List<ListItemModel> listItems;
    private LayoutInflater layoutInflater;

    public MyListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public List<ListItemModel> getListItems() {
        return listItems;
    }

    public void setListItems(List<ListItemModel> listItems) {
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
// TODO Auto-generated method stub
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
// TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
// TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
        ListItemView myListItemView;
        if (convertView == null) {
            myListItemView = new ListItemView();
            convertView = layoutInflater
                    .inflate(R.layout.item_listsimple, null);
            myListItemView.setTv_title((TextView) convertView
                    .findViewById(R.id.item_title));
            myListItemView.setTv_data((TextView) convertView
                    .findViewById(R.id.item_data));
            myListItemView.setIv_image((ImageView) convertView
                    .findViewById(R.id.item_image));
            convertView.setTag(myListItemView);
        } else {
            myListItemView = (ListItemView) convertView.getTag();
        }
        myListItemView.getTv_title()
                .setText(listItems.get(position).getTitle());
        myListItemView.getTv_data().setText(listItems.get(position).getData());
        myListItemView.getIv_image().setImageResource(
                listItems.get(position).getRid());

        return convertView;
    }

    class ListItemView {
        private TextView tv_title;
        private TextView tv_data;
        private ImageView iv_image;

        public TextView getTv_title() {
            return tv_title;
        }

        public void setTv_title(TextView tv_title) {
            this.tv_title = tv_title;
        }

        public TextView getTv_data() {
            return tv_data;
        }

        public void setTv_data(TextView tv_data) {
            this.tv_data = tv_data;
        }

        public ImageView getIv_image() {
            return iv_image;
        }

        public void setIv_image(ImageView iv_image) {
            this.iv_image = iv_image;
        }
    }
}
