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
import th.ac.kmitl.groupedapplication.model.Group;

public class GroupListAdapter extends RecyclerView.Adapter<GroupViewHolder>{
    private List<Group> groupsList;
    private GroupItemClickListener groupItemClickListener;

    public GroupListAdapter(List<Group> groupsList, GroupItemClickListener groupItemClickListener){
        this.groupsList = groupsList;
        this.groupItemClickListener = groupItemClickListener;
    }


    @Override
    public void onBindViewHolder(GroupViewHolder groupViewHolder, int i) {
        Group group = groupsList.get(i);
        groupViewHolder.setGroup(group);

        groupViewHolder.tvGroupNum.setText("กลุ่มที่ : " + group.getGroupNum());

        if(!group.getGroupME()) {
            groupViewHolder.tvGroupME.setText("");
        }

        groupViewHolder.tvHeadProject.setText(group.getGroupProjName());

        groupViewHolder.tvGroupCount.setText(group.getGroupCount() +" คน");
        groupViewHolder.tvHeadProject.setText(group.getGroupProjName());
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_group, viewGroup, false);
        return new GroupViewHolder(itemView, groupItemClickListener);
    }

    @Override
    public int getItemCount(){
        return groupsList.size();
    }
}
class GroupViewHolder extends RecyclerView.ViewHolder{

    private GroupItemClickListener groupItemClickListener;
    private Group group;
    protected TextView tvGroupNum;
    protected TextView tvGroupME;
    protected TextView tvGroupCount;
    protected TextView tvHeadProject;

    protected String groupID;

    private Context ctx;

    public Context getCtx(){
        return ctx;
    }

    public GroupViewHolder(View v, GroupItemClickListener groupItemClickListener){
        super(v);
        this.groupItemClickListener = groupItemClickListener;
        ctx = v.getContext();

        tvGroupNum = v.findViewById(R.id.tvGroupNum);
        tvGroupME= v.findViewById(R.id.tvGroupME);
        tvGroupCount = v.findViewById(R.id.tvGroupCount);
        tvHeadProject = v.findViewById(R.id.tvHeadProject);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                GroupViewHolder.this.groupItemClickListener.onGroupItemClick(group.getGroupId(), group.getGroupNum(), group.getGroupProjName(), group.getGroupME());
            }
        });
    }

    public void setGroup(Group group){
        this.group = group;
    }

}