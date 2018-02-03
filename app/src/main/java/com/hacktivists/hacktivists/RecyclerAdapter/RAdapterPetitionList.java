package com.hacktivists.hacktivists.RecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hacktivists.hacktivists.CourseInfoActivity;
import com.hacktivists.hacktivists.GetterSetter.RParserPetitionList;
import com.hacktivists.hacktivists.NavigationDrawerActivity;
import com.hacktivists.hacktivists.R;

import java.util.List;

/**
 * Created by steven on 2/2/18.
 */

public class RAdapterPetitionList extends RecyclerView.Adapter<RAdapterPetitionList.ViewHolder> {

    Context context;
    List<RParserPetitionList> rParserPetitionLists;

    public RAdapterPetitionList(Context context, List<RParserPetitionList> rParserPetitionLists){
        this.context = context;
        this.rParserPetitionLists = rParserPetitionLists;
    }

    @Override
    public RAdapterPetitionList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerholder_petition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RAdapterPetitionList.ViewHolder holder, final int position) {
        RParserPetitionList rParserPetitionList = rParserPetitionLists.get(position);
        final String id = String.valueOf(rParserPetitionList.getId());
        final String subject = rParserPetitionList.getSubject();
        final String level = rParserPetitionList.getLevel();
        final String location = rParserPetitionList.getLocation();
        final String enrolled_students = String.valueOf(rParserPetitionList.getEnrolled_students());
        final String description = rParserPetitionList.getDescription();
        final String user_id = String.valueOf(rParserPetitionList.getUser_id());

        holder.tv_holder_petition_id.setText(id);
        holder.tv_holder_petition_subject.setText(subject);
        holder.tv_holder_petition_level.setText(level);
        holder.tv_holder_petition_location.setText(location);
        holder.tv_holder_petition_enrolled_students.setText(enrolled_students);
        holder.tv_holder_petition_description.setText(description);
        holder.tv_holder_petition_user_id.setText(user_id);


        holder.btn_viewClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("petition_id", id);
                bundle.putString("subject", subject);
                bundle.putString("level", level);
                bundle.putString("location", location);
                bundle.putString("enrolled_students", enrolled_students);
                bundle.putString("description", description);
                bundle.putString("user_id", user_id);

                Intent intent = new Intent(context, CourseInfoActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return rParserPetitionLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_holder_petition_id,
                tv_holder_petition_subject,
                tv_holder_petition_level,
                tv_holder_petition_location,
                tv_holder_petition_enrolled_students,
                tv_holder_petition_description,
                tv_holder_petition_user_id;

        Button btn_viewClass;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_holder_petition_id = itemView.findViewById(R.id.tv_holder_petition_id);
            tv_holder_petition_subject = itemView.findViewById(R.id.tv_holder_petition_subject);
            tv_holder_petition_level = itemView.findViewById(R.id.tv_holder_petition_level);
            tv_holder_petition_location = itemView.findViewById(R.id.tv_holder_petition_location);
            tv_holder_petition_enrolled_students = itemView.findViewById(R.id.tv_holder_petition_enrolled_students);
            tv_holder_petition_description = itemView.findViewById(R.id.tv_holder_petition_description);
            tv_holder_petition_user_id = itemView.findViewById(R.id.tv_holder_petition_user_id);

            btn_viewClass = itemView.findViewById(R.id.btn_viewClass);

        }
    }
}
