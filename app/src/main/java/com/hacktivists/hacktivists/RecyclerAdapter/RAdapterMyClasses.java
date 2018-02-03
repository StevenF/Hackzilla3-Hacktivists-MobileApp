package com.hacktivists.hacktivists.RecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hacktivists.hacktivists.GetterSetter.RParserMyClasses;
import com.hacktivists.hacktivists.R;
import com.hacktivists.hacktivists.SharedPreferences.SharedPreferencesManager;
import com.hacktivists.hacktivists.SharedPreferences.SharedPreferencesTutorName;

import java.util.List;

/**
 * Created by JMM on 2/3/2018.
 */

public class RAdapterMyClasses extends RecyclerView.Adapter<RAdapterMyClasses.ViewHolder> {


    Context context;
    List<RParserMyClasses> rParserMyClassesList;
    SharedPreferencesManager sharedPreferencesManager;
    SharedPreferencesTutorName sharedPreferencesTutorName;
    RAdapterMyClassesOnItemClick rAdapterMyClassesOnItemClick;

    public RAdapterMyClasses(Context context, List<RParserMyClasses> rParserMyClassesList, RAdapterMyClassesOnItemClick rAdapterMyClassesOnItemClick) {
        this.context = context;
        this.rParserMyClassesList = rParserMyClassesList;
        sharedPreferencesManager = new SharedPreferencesManager(context);
        this.rAdapterMyClassesOnItemClick = rAdapterMyClassesOnItemClick;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerholder_myclasses, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RParserMyClasses rParserMyClasses = rParserMyClassesList.get(position);

        final int id = rParserMyClasses.getId();
        final int user_id = rParserMyClasses.getUser_id();
        final int petition_id = rParserMyClasses.getPetition_id();
        String date = rParserMyClasses.getDate();

        String petition_name = sharedPreferencesManager.getPetitionTitle(rParserMyClasses.getPetition_id());
        String tutor_name = rParserMyClasses.getTutorname();
        String status = rParserMyClasses.getStatus();

        if (rParserMyClasses.getTutorname().isEmpty() || rParserMyClasses == null) {
            holder.tv_myclasses_tutor_fullname.setText("No available instructor yet.");
        } else {
            holder.tv_myclasses_tutor_fullname.setText(tutor_name);
        }

        if(rParserMyClasses.getStatus().equals("false")){
            holder.tv_myclasses_status.setText("Not posted yet.");
        }else{
            holder.tv_myclasses_status.setText("POSTED");
        }

        holder.tv_myclasses_user_id.setText(String.valueOf(user_id));
        holder.tv_myclasses_petition_id.setText(String.valueOf(petition_name));
        holder.tv_myclasses_date_enrolled.setText(date);
        holder.btn_myclassess_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rAdapterMyClassesOnItemClick.onItemClick(id, petition_id);
            }
        });


    }

    @Override
    public int getItemCount() {
        return rParserMyClassesList.size();
    }

    public interface RAdapterMyClassesOnItemClick {
        void onItemClick(int user_id, int petition_id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_myclasses_user_id, tv_myclasses_date_enrolled, tv_myclasses_petition_id, tv_myclasses_tutor_fullname, tv_myclasses_status;
        Button btn_myclassess_cancel;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_myclasses_user_id = itemView.findViewById(R.id.tv_myclasses_user_id);
            tv_myclasses_date_enrolled = itemView.findViewById(R.id.tv_myclasses_date_enrolled);
            tv_myclasses_petition_id = itemView.findViewById(R.id.tv_myclasses_petition_id);
            tv_myclasses_tutor_fullname = itemView.findViewById(R.id.tv_myclasses_tutor_fullname);
            tv_myclasses_status = itemView.findViewById(R.id.tv_myclasses_status);

            btn_myclassess_cancel = itemView.findViewById(R.id.btn_myclasses_cancel);


        }
    }


}
