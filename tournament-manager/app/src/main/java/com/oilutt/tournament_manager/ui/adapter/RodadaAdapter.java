package com.oilutt.tournament_manager.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.Rodada;
import com.oilutt.tournament_manager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Túlio on 17/09/2017.
 */

public class RodadaAdapter extends PagerAdapter{

    private List<Rodada> list = new ArrayList<>();
    Context context;

    public RodadaAdapter(List<Rodada> list, Context context){
        this.context = context;
        if(list != null)
            this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.adapter_rodadas, collection, false);

        TextView rodadaTitle = (TextView) layout.findViewById(R.id.rodada_title);
        rodadaTitle.setText(Utils.formatString(context.getString(R.string.rodada_title), String.valueOf(position+1)));

        ImageView back = (ImageView) layout.findViewById(R.id.back);
        ImageView next = (ImageView) layout.findViewById(R.id.next);
        if(position == 0)
            back.setVisibility(View.INVISIBLE);
        if(position == list.size() - 1)
            next.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        PartidaAdapter adapter = new PartidaAdapter(list.get(position).getPartidas());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);

        collection.addView(layout);
        return layout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    public void setData(List<Rodada> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
