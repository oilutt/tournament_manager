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
import com.oilutt.tournament_manager.model.Fase;
import com.oilutt.tournament_manager.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by oilut on 28/09/2017.
 */

public class MataMataAdapter extends PagerAdapter {

    private List<Fase> list = new ArrayList<>();
    Context context;

    public MataMataAdapter(List<Fase> list, Context context){
        this.context = context;
        if(list != null)
            this.list = list;
        Collections.reverse(this.list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.adapter_matamata, collection, false);

        TextView faseTitle = (TextView) layout.findViewById(R.id.rodada_title);
        if(list.size() == 5) {
            if (position == 0)
                faseTitle.setText(Utils.formatString(context.getString(R.string.fase_title), String.valueOf(position + 1)));
            else if (position == 1)
                faseTitle.setText(context.getString(R.string.oitavas));
            else if (position == 2)
                faseTitle.setText(context.getString(R.string.quartas));
            else if (position == 3)
                faseTitle.setText(context.getString(R.string.semi));
            else if (position == 4)
                faseTitle.setText(context.getString(R.string.final_title));
        } else if (list.size() == 4){
            if (position == 0)
                faseTitle.setText(context.getString(R.string.oitavas));
            else if (position == 1)
                faseTitle.setText(context.getString(R.string.quartas));
            else if (position == 2)
                faseTitle.setText(context.getString(R.string.semi));
            else if (position == 3)
                faseTitle.setText(context.getString(R.string.final_title));
        } else if (list.size() == 3){
            if (position == 0)
                faseTitle.setText(context.getString(R.string.quartas));
            else if (position == 1)
                faseTitle.setText(context.getString(R.string.semi));
            else if (position == 2)
                faseTitle.setText(context.getString(R.string.final_title));
        } else if (list.size() == 2){
            if (position == 0)
                faseTitle.setText(context.getString(R.string.semi));
            else if (position == 1)
                faseTitle.setText(context.getString(R.string.final_title));
        } else {
            if (position == 0)
                faseTitle.setText(context.getString(R.string.final_title));
        }

        ImageView back = (ImageView) layout.findViewById(R.id.back);
        ImageView next = (ImageView) layout.findViewById(R.id.next);
        if(position == 0)
            back.setVisibility(View.INVISIBLE);
        if(position == list.size() - 1)
            next.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        PartidaMatamataAdapter adapter = new PartidaMatamataAdapter(list.get(position).getPartidas());
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

    public void setData(List<Fase> fases){
        list = fases;
        notifyDataSetChanged();
    }
}
