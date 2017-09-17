package com.oilutt.tournament_manager.ui.adapter;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Túlio on 16/09/2017.
 */

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.TeamListHolder> {

    private List<String> list = new ArrayList<>();
    private int size;
    Context context;

    public TeamListAdapter(int size, Context context){
        this.context = context;
        this.size = size;
    }

    public TeamListAdapter (List<String> list, int size, Context context){
        this.context = context;
        this.size = size;
        this.list = list;
        updateList();
    }

    @Override
    public TeamListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_team_list, parent, false);
        return new TeamListHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamListHolder holder, int position) {
        holder.inputLayout.setHint(Utils.formatString(context.getString(R.string.time_numero), String.valueOf(position+1)));
        if(list.size() > 0 && list.get(position) != null) {
            holder.nomeTime.setText(list.get(position));
        }

        holder.nomeTime.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                if(!holder.nomeTime.getText().toString().equals("")) {
                    if(list.size() > 0 && list.get(position) != null){
                        list.set(position, holder.nomeTime.getText().toString());
                    } else {
                        list.add(position, holder.nomeTime.getText().toString());
                    }
                }
            }
        });

        holder.nomeTime.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId==EditorInfo.IME_ACTION_DONE || actionId==EditorInfo.IME_ACTION_NEXT){
                if(!holder.nomeTime.getText().toString().equals("")) {
                    if(list.size() > 0 && list.get(position) != null){
                        list.set(position, holder.nomeTime.getText().toString());
                    } else {
                        list.add(position, holder.nomeTime.getText().toString());
                    }
                }
            }
            return false;
        });

        if(position == size -1) {
            holder.nomeTime.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void setData(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public List<String> getData(){
        return  list;
    }

    private void updateList(){
        for (int x = 0; x <= size; x++){
            if(list.size() < x ){
                list.add("");
            }
        }
    }

    public class TeamListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nome_time)
        EditText nomeTime;
        @BindView(R.id.inputLayout)
        TextInputLayout inputLayout;

        public TeamListHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
