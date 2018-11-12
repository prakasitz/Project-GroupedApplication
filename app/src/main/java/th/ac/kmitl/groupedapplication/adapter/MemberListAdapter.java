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
import th.ac.kmitl.groupedapplication.model.Member;

public class MemberListAdapter extends RecyclerView.Adapter<MemberViewHolder> {
    private List<Member> membersList;
    private MemberItemClickListener memberItemClickListener;

    public MemberListAdapter(List<Member> membersList, MemberItemClickListener memberItemClickListener){
        Log.d("crListAdapter",String.valueOf(membersList.size()));
        this.membersList = membersList;
        this.memberItemClickListener = memberItemClickListener;
    }


    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int i) {
        Member member = membersList.get(i);
        memberViewHolder.setMember(member);
        memberViewHolder.tvId.setText(member.getMember_id());
        memberViewHolder.tvName.setText(member.getMember_name());
        memberViewHolder.tvPost.setText(member.getMember_post());
    }


    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_member, viewGroup, false);
        return new MemberViewHolder(itemView, memberItemClickListener);
    }

    @Override
    public int getItemCount(){
        return membersList.size();
    }
}
class MemberViewHolder extends RecyclerView.ViewHolder{

    private MemberItemClickListener memberItemClickListener;
    private Member member;
    protected TextView tvId;
    protected TextView tvName;
    protected TextView tvPost;

    private Context ctx;

    public Context getCtx(){
        return ctx;
    }

    public MemberViewHolder(View v, MemberItemClickListener memberItemClickListener){
        super(v);
        this.memberItemClickListener = memberItemClickListener;
        ctx = v.getContext();
        tvId = v.findViewById(R.id.tvIdMember);
        tvName = v.findViewById(R.id.tvNameMember);
        tvPost = v.findViewById(R.id.tvPostMember);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MemberViewHolder.this.memberItemClickListener.onMemberItemClick(member.getMember_id());
            }
        });
    }

    public void setMember(Member member){
        this.member = member;
    }

}
