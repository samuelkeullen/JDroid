/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.ide.info;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.ide.DLog;
import com.duy.ide.R;

import java.util.ArrayList;

//import butterknife.BindView;

/**
 * Created by Duy on 28-Mar-17.
 */

public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder> {
    private static final String TAG = LicenseAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private ArrayList<ItemInfo> listData = new ArrayList<>();
    private Context mContext;

    public LicenseAdapter(Context context, ArrayList<ItemInfo> listData) {
        this.inflater = LayoutInflater.from(context);
        this.listData = listData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_info, parent, false);
        DLog.d(TAG, "onCreateViewHolder: ");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindContent(listData.get(position));
//        holder.root.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onCategoryClick(View v) {
//                Toast.makeText(context, listData.indexOf(position).toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.txt_title)
        TextView txtTitle;
//        @BindView(R.id.txt_desc)
        TextView txtDesc;
//        @BindView(R.id.container)
        View root;

        public ViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtDesc = (TextView) itemView.findViewById(R.id.txt_desc);
            root = itemView.findViewById(R.id.container);
        }


        public void bindContent(ItemInfo itemInfo) {
            txtTitle.setText(itemInfo.getTitle());
            txtDesc.setText(itemInfo.getLink());
        }
    }

}
