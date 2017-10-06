package com.oilutt.tournament_manager.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.Time;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tulio on 06/10/2017.
 */

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.TeamsHolder> {

    private List<Time> list = new ArrayList<>();
    Context context;

    public TeamsAdapter(List<Time> list) {
        this.list = list;
    }

    @Override
    public TeamsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_detail, parent, false);
        context = view.getContext();
        return new TeamsHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamsHolder holder, int position) {
        Time time = list.get(position);

        holder.nome.setText(time.getNome());
        if(time.isCampeao())
            holder.campeao.setVisibility(View.VISIBLE);
        if (position % 2 == 0) {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight2));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Time> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class TeamsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nome)
        TextView nome;
        @BindView(R.id.campeao)
        ImageView campeao;
        @BindView(R.id.layout)
        LinearLayout layout;

        public TeamsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
