package th.ac.kmitl.groupedapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import th.ac.kmitl.groupedapplication.R;
import th.ac.kmitl.groupedapplication.model.Project;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectViewHolder> {
    private List<Project> worksList;
    private ProjectItemClickListener projectItemClickListener;

    public ProjectListAdapter(List<Project> worksList, ProjectItemClickListener projectItemClickListener){
        Log.d("crListAdapter",String.valueOf(worksList.size()));
        this.worksList = worksList;
        this.projectItemClickListener = projectItemClickListener;
    }


    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int i) {
        Project project = worksList.get(i);
        projectViewHolder.setProject(project);
        projectViewHolder.tvId.setText("ID: " + project.getProject_id());
        projectViewHolder.tvName.setText(project.getProject_name());
    }


    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.work_item, viewGroup, false);
        return new ProjectViewHolder(itemView, projectItemClickListener);
    }

    @Override
    public int getItemCount(){
        return worksList.size();
    }
}
class ProjectViewHolder extends RecyclerView.ViewHolder{

    private ProjectItemClickListener projectItemClickListener;
    private Project project;
    protected TextView tvId;
    protected TextView tvName;

    private Context ctx;

    public Context getCtx(){
        return ctx;
    }

    public ProjectViewHolder(View v, ProjectItemClickListener projectItemClickListener){
        super(v);
        this.projectItemClickListener = projectItemClickListener;
        ctx = v.getContext();
        tvId = (TextView) v.findViewById(R.id.tvIdWork);
        tvName = (TextView) v.findViewById(R.id.tvNameWork);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ProjectViewHolder.this.projectItemClickListener.onProjectItemClick(project.getProject_id());
            }
        });
    }

    public void setProject(Project project){
        this.project = project;
    }

}
