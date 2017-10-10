package com.oilutt.tournament_manager.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oilutt.tournament_manager.R;
import com.oilutt.tournament_manager.model.Campeonato;
import com.oilutt.tournament_manager.utils.Utils;

import java.util.List;

/**
 * Created by Tulio on 03/10/2017.
 */

public class TabAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<Fragment> fragmentList;
    private Campeonato camp;
    private boolean edit;

    public TabAdapter(Context context, List<Fragment> fragmentList, FragmentManager fm, Campeonato camp, boolean edit) {
        super(fm);
        this.fragmentList = fragmentList;
        this.context = context;
        this.camp = camp;
        this.edit = edit;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (camp.getFormato().getNome().equals(context.getString(R.string.liga))) {
            if (!edit) {
                switch (position) {
                    case 0:
                        return "Tabela";
                    default:
                        return "Rodada " + position;
                }
            } else {
                switch (position) {
                    default:
                        return "Rodada " + (position + 1);
                }
            }
        } else if (camp.getFormato().getNome().equals(context.getString(R.string.matamata))) {
            if (camp.getFormato().getFases().size() == 5) {
                if (position == 0)
                    return Utils.formatString(context.getString(R.string.fase_title), String.valueOf(position + 1));
                else if (position == 1)
                    return context.getString(R.string.oitavas);
                else if (position == 2)
                    return context.getString(R.string.quartas);
                else if (position == 3)
                    return context.getString(R.string.semi);
                else if (position == 4)
                    return context.getString(R.string.final_title);
            } else if (camp.getFormato().getFases().size() == 4) {
                if (position == 0)
                    return context.getString(R.string.oitavas);
                else if (position == 1)
                    return context.getString(R.string.quartas);
                else if (position == 2)
                    return context.getString(R.string.semi);
                else if (position == 3)
                    return context.getString(R.string.final_title);
            } else if (camp.getFormato().getFases().size() == 3) {
                if (position == 0)
                    return context.getString(R.string.quartas);
                else if (position == 1)
                    return context.getString(R.string.semi);
                else if (position == 2)
                    return context.getString(R.string.final_title);
            } else if (camp.getFormato().getFases().size() == 2) {
                if (position == 0)
                    return context.getString(R.string.semi);
                else if (position == 1)
                    return context.getString(R.string.final_title);
            } else {
                if (position == 0)
                    return context.getString(R.string.final_title);
            }
        } else {
            int grupos = camp.getFormato().getGrupos().size();
            if(!edit) {
                if (position <= grupos * 4 - 1) {
                    if (position % 4 == 0) {
                        return "Grupo " + (position / 4 + 1);
                    } else {
                        return "Rodada " + (position % 4);
                    }
                } else {
                    if (camp.getFormato().getFases().size() == 4) {
                        if (position == grupos * 4)
                            return context.getString(R.string.oitavas);
                        else if (position == grupos * 4 + 1)
                            return context.getString(R.string.quartas);
                        else if (position == grupos * 4 + 2)
                            return context.getString(R.string.semi);
                        else if (position == grupos * 4 + 3)
                            return context.getString(R.string.final_title);
                    } else if (camp.getFormato().getFases().size() == 3) {
                        if (position == grupos * 4)
                            return context.getString(R.string.quartas);
                        else if (position == grupos * 4 + 1)
                            return context.getString(R.string.semi);
                        else if (position == grupos * 4 + 2)
                            return context.getString(R.string.final_title);
                    } else if (camp.getFormato().getFases().size() == 2) {
                        if (position == grupos * 4)
                            return context.getString(R.string.semi);
                        else if (position == grupos * 4 + 1)
                            return context.getString(R.string.final_title);
                    }
                }
            } else {
                if(position <= grupos * 3 -1) {
                    return "Rodada " + ((position % 3) +1);
                } else {
                    if (camp.getFormato().getFases().size() == 4) {
                        if (position == grupos * 3)
                            return context.getString(R.string.oitavas);
                        else if (position == grupos * 3 + 1)
                            return context.getString(R.string.quartas);
                        else if (position == grupos * 3 + 2)
                            return context.getString(R.string.semi);
                        else if (position == grupos * 3 + 3)
                            return context.getString(R.string.final_title);
                    } else if (camp.getFormato().getFases().size() == 3) {
                        if (position == grupos * 3)
                            return context.getString(R.string.quartas);
                        else if (position == grupos * 3 + 1)
                            return context.getString(R.string.semi);
                        else if (position == grupos * 3 + 2)
                            return context.getString(R.string.final_title);
                    } else if (camp.getFormato().getFases().size() == 2) {
                        if (position == grupos * 3)
                            return context.getString(R.string.semi);
                        else if (position == grupos * 3 + 1)
                            return context.getString(R.string.final_title);
                    }
                }
            }
        }
        return "";
    }

    public List<Fragment> getFragmentList(){
        return fragmentList;
    }

    public void setData(List<Fragment> fragmentList){
        this.fragmentList = fragmentList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
