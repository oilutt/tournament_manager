package com.oilutt.tournament_manager.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.Partida;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Túlio on 17/09/2017.
 */

public class PartidaAdapter extends RecyclerView.Adapter<PartidaAdapter.PartidaHolder> {

    private List<Partida> list = new ArrayList<>();
    Context context;

    public PartidaAdapter(List<Partida> list){
        this.list = list;
    }

    @Override
    public PartidaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partida, parent, false);
        context = view.getContext();
        return new PartidaHolder(view);
    }

    @Override
    public void onBindViewHolder(PartidaHolder holder, int position) {
        Partida partida = list.get(position);
        holder.nomeTimeCasa.setText(partida.getTime1());
        holder.nomeTimeFora.setText(partida.getTime2());
        if(partida.getValorTime1()!= null) {
            holder.golsTimeCasa.setText(partida.getValorTime1());
            holder.golsTimeFora.setText(partida.getValorTime2());
        } else {
            holder.golsTimeCasa.setVisibility(View.INVISIBLE);
            holder.golsTimeFora.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Partida> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public class PartidaHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nomeTimeCasa)
        TextView nomeTimeCasa;
        @BindView(R.id.golsTimeCasa)
        TextView golsTimeCasa;
        @BindView(R.id.golsTimeFora)
        TextView golsTimeFora;
        @BindView(R.id.nomeTimeFora)
        TextView nomeTimeFora;

        public PartidaHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
