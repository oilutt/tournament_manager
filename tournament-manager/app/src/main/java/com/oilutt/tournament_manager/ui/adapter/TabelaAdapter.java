package com.oilutt.tournament_manager.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.Time;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Túlio on 17/09/2017.
 */

public class TabelaAdapter extends RecyclerView.Adapter<TabelaAdapter.TabelaHolder>{

    private List<Time> list = new ArrayList<>();
    Context context;

    public TabelaAdapter(List<Time> list){
        this.list = list;
    }

    @Override
    public TabelaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tabela, parent, false);
        context = view.getContext();
        return new TabelaHolder(view);
    }

    @Override
    public void onBindViewHolder(TabelaHolder holder, int position) {
        Time time = list.get(position);

        holder.posicao.setText(String.valueOf(position+1));
        holder.nomeTime.setText(time.getNome());
        holder.pontos.setText(time.getPontos());
        holder.jogos.setText(time.getJogos());
        holder.vitorias.setText(time.getVitorias());
        holder.empates.setText(time.getEmpates());
        holder.derrotas.setText(time.getDerrotas());
        holder.golsFeitos.setText(time.getGolsFeitos());
        holder.golsSofridos.setText(time.getGolsSofridos());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Time> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public class TabelaHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.posicao)
        TextView posicao;
        @BindView(R.id.nomeTime)
        TextView nomeTime;
        @BindView(R.id.pontos)
        TextView pontos;
        @BindView(R.id.jogos)
        TextView jogos;
        @BindView(R.id.vitorias)
        TextView vitorias;
        @BindView(R.id.empates)
        TextView empates;
        @BindView(R.id.derrotas)
        TextView derrotas;
        @BindView(R.id.golsFeitos)
        TextView golsFeitos;
        @BindView(R.id.golsSofridos)
        TextView golsSofridos;

        public TabelaHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
