package com.oilutt.tournament_manager.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

public class PartidaMatamataEditAdapter extends RecyclerView.Adapter<PartidaMatamataEditAdapter.PartidaMatamataEditHolder> {

    private List<BestOf> list = new ArrayList<>();
    Context context;

    public PartidaMatamataEditAdapter(List<BestOf> list) {
        if (list != null)
            this.list.addAll(list);
        else
            this.list = new ArrayList<>();
    }

    @Override
    public PartidaMatamataEditAdapter.PartidaMatamataEditHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partida_matamata_edit, parent, false);
        context = view.getContext();
        return new PartidaMatamataEditAdapter.PartidaMatamataEditHolder(view);
    }

    @Override
    public void onBindViewHolder(PartidaMatamataEditAdapter.PartidaMatamataEditHolder holder, int position) {
        BestOf partida = list.get(position);
        holder.nomeTimeCasa.setText(partida.getTime1());
        holder.nomeTimeFora.setText(partida.getTime2());
        if(partida.getValorTime1() != null) {
            holder.partidasTime1.setText(partida.getValorTime1());
            holder.partidasTime2.setText(partida.getValorTime2());
            holder.partidasTime1.setFocusable(false);
            holder.partidasTime2.setFocusable(false);
        } else {
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (Integer.parseInt(s.toString()) < Math.round(partida.getQuantity()))
                        partida.setValorTime1(s.toString());
                    else
                        Snackbar.make(holder.itemView, R.string.valor_invalido, Snackbar.LENGTH_SHORT).show();
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
                    if (Integer.parseInt(s.toString()) < Math.round(partida.getQuantity()))
                        partida.setValorTime2(s.toString());
                    else
                        Snackbar.make(holder.itemView, R.string.valor_invalido, Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
            holder.partidasTime1.addTextChangedListener(watcher);
            holder.partidasTime2.addTextChangedListener(watcher2);
        }
        if (position == list.size() - 1)

        {
            holder.partidasTime2.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<BestOf> getData() {
        return list;
    }

    public class PartidaMatamataEditHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time1)
        TextView nomeTimeCasa;
        @BindView(R.id.golsTime1)
        EditText partidasTime1;
        @BindView(R.id.golsTime2)
        EditText partidasTime2;
        @BindView(R.id.time2)
        TextView nomeTimeFora;

        public PartidaMatamataEditHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
