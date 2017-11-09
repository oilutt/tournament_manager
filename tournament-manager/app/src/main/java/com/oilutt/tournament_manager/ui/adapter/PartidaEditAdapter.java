package com.oilutt.tournament_manager.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class PartidaEditAdapter extends RecyclerView.Adapter<PartidaEditAdapter.PartidaEditHolder> {

    private List<Partida> list = new ArrayList<>();
    private Context context;
    private int impar;

    public PartidaEditAdapter(List<Partida> list, int impar) {
        if(list != null)
            this.list.addAll(list);
        this.impar = impar;
    }

    @Override
    public PartidaEditHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partida_edit, parent, false);
        context = view.getContext();
        return new PartidaEditHolder(view);
    }

    @Override
    public void onBindViewHolder(PartidaEditHolder holder, int position) {
        Partida partida = list.get(position);
        holder.nomeTimeCasa.setText(partida.getTime1());
        holder.nomeTimeFora.setText(partida.getTime2());
        if(partida.getValorTime1() != null) {
            holder.golsTimeCasa.setText(partida.getValorTime1());
            holder.golsTimeFora.setText(partida.getValorTime2());
            holder.golsTimeCasa.setFocusable(false);
            holder.golsTimeFora.setFocusable(false);
        } else {
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    partida.setValorTime1(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
            TextWatcher watcher2 = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    partida.setValorTime2(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
            holder.golsTimeCasa.addTextChangedListener(watcher);
            holder.golsTimeFora.addTextChangedListener(watcher2);
        }
        if (impar == 0) {
            if (position % 2 == 1) {
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight2));
            }
        } else {
            if (position % 2 == 0) {
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight2));
            }
        }
        if(position == list.size() -1){
            holder.golsTimeFora.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Partida> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Partida> getData(){
        return list;
    }

    public class PartidaEditHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nomeTimeCasa)
        TextView nomeTimeCasa;
        @BindView(R.id.golsTimeCasa)
        EditText golsTimeCasa;
        @BindView(R.id.golsTimeFora)
        EditText golsTimeFora;
        @BindView(R.id.nomeTimeFora)
        TextView nomeTimeFora;
        @BindView(R.id.layout)
        LinearLayout layout;

        public PartidaEditHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
