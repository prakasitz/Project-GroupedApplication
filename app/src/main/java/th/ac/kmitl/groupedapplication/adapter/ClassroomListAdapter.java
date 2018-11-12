package th.ac.kmitl.groupedapplication.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import th.ac.kmitl.groupedapplication.R;
import th.ac.kmitl.groupedapplication.model.Classroom;

public class ClassroomListAdapter extends RecyclerView.Adapter<ClassroomViewHolder>{
    private List<Classroom> classroomsList;
    private ClassroomItemClickListener classroomItemClickListener;

    public ClassroomListAdapter(List<Classroom> classroomsList, ClassroomItemClickListener classroomItemClickListener){
        Log.d("crListAdapter",String.valueOf(classroomsList.size()));
        this.classroomsList = classroomsList;
        this.classroomItemClickListener = classroomItemClickListener;
    }


    @Override
    public void onBindViewHolder(ClassroomViewHolder classroomViewHolder, int i){
        Classroom classroom = classroomsList.get(i);
        classroomViewHolder.setClassroom(classroom);
        classroomViewHolder.tvId.setText("ID: " + classroom.getClass_id());
        classroomViewHolder.tvName.setText(classroom.getClass_subject());
    }

    @Override
    public ClassroomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_classroom, viewGroup, false);
        return new ClassroomViewHolder(itemView, classroomItemClickListener);
    }

    @Override
    public int getItemCount(){
        return classroomsList.size();
    }
}
 class ClassroomViewHolder extends RecyclerView.ViewHolder{

    private ClassroomItemClickListener classroomItemClickListener;
    private Classroom classroom;
    protected TextView tvId;
    protected TextView tvName;

    private Context ctx;

    public Context getCtx(){
        return ctx;
    }

    public ClassroomViewHolder(View v, ClassroomItemClickListener classroomItemClickListener){
        super(v);
        this.classroomItemClickListener = classroomItemClickListener;
        ctx = v.getContext();
        tvId = (TextView) v.findViewById(R.id.tvIdClassroom);
        tvName = (TextView) v.findViewById(R.id.tvNameClassroom);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ClassroomViewHolder.this.classroomItemClickListener.onClassroomItemClick(classroom.getClass_id(), classroom.getClass_subject());
            }
        });
    }

    public void setClassroom(Classroom classroom){
        this.classroom = classroom;
    }

}