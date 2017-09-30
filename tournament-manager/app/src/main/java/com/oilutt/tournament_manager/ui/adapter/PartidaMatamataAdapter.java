package com.oilutt.tournament_manager.ui.adapter;

import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.BestOf;
import com.oilutt.tournament_manager.model.Partida;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by oilut on 28/09/2017.
 */

public class PartidaMatamataAdapter extends RecyclerView.Adapter<PartidaMatamataAdapter.PartidaMatamataHolder> {

    private List<BestOf> list = new ArrayList<>();
    Context context;

    public PartidaMatamataAdapter(List<BestOf> list){
        if(list!= null)
            this.list = list;
        else
            this.list = new ArrayList<>();
    }

    @Override
    public PartidaMatamataAdapter.PartidaMatamataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partida_matamata, parent, false);
        context = view.getContext();
        return new PartidaMatamataAdapter.PartidaMatamataHolder(view);
    }

    @Override
    public void onBindViewHolder(PartidaMatamataAdapter.PartidaMatamataHolder holder, int position) {
        BestOf partida = list.get(position);
        holder.nomeTimeCasa.setText(partida.getTime1());
        holder.nomeTimeFora.setText(partida.getTime2());
        if(partida.getValorTime1()!= null) {
            holder.partidasTime1.setText(partida.getValorTime1());
            holder.partidasTime2.setText(partida.getValorTime2());
        } else {
            holder.partidasTime1.setVisibility(View.INVISIBLE);
            holder.partidasTime2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PartidaMatamataHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time1)
        TextView nomeTimeCasa;
        @BindView(R.id.golsTime1)
        TextView partidasTime1;
        @BindView(R.id.golsTime2)
        TextView partidasTime2;
        @BindView(R.id.time2)
        TextView nomeTimeFora;

        public PartidaMatamataHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
