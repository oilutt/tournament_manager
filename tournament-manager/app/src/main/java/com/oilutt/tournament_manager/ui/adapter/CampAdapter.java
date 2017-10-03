package com.oilutt.tournament_manager.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.ui.activity.CampeonatoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by oilut on 25/08/2017.
 */

public class CampAdapter extends RecyclerView.Adapter<CampAdapter.CampHolder>{

    private List<Campeonato> list = new ArrayList<>();
    Activity activity;

    public CampAdapter(Activity context){
        this.activity = context;
    }

    @Override
    public CampHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_camp, null);
        return new CampHolder(view);
    }

    @Override
    public void onBindViewHolder(CampHolder holder, int position) {
        Campeonato camp = list.get(position);
        holder.tvCampNome.setText(camp.getNome());
        holder.tvDescricao.setText(camp.getDescricao() != null ? camp.getDescricao() : "");
        if(camp.getFoto() != null)
            Glide.with(activity).load(Base64.decode(camp.getFoto(), Base64.DEFAULT)).into(holder.imageCamp);
        if(camp.getStatus() == 1) {
            holder.tvCampStatus.setText(R.string.aberto);
            holder.tvCampStatus.setTextColor(activity.getResources().getColor(R.color.jungle_green));
        }else if(camp.getStatus() == 2) {
            holder.tvCampStatus.setText(R.string.em_andamento);
            holder.tvCampStatus.setTextColor(activity.getResources().getColor(R.color.mango));
        }else {
            holder.tvCampStatus.setText(R.string.concluido);
            holder.tvCampStatus.setTextColor(activity.getResources().getColor(R.color.red));
        }
        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(activity, CampeonatoActivity.class);
            intent.putExtra("campeonatoId", camp.getId());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Campeonato> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public class CampHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_camp_nome)
        TextView tvCampNome;
        @BindView(R.id.tv_descricao)
        TextView tvDescricao;
        @BindView(R.id.tv_camp_status)
        TextView tvCampStatus;
        @BindView(R.id.layout)
        LinearLayout layout;
        @BindView(R.id.image_camp)
        ImageView imageCamp;

        public CampHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
