package com.hoversoftsoln.estafortdriver.request;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.hoversoftsoln.estafortdriver.core.data.Request;
import com.hoversoftsoln.estafortdriver.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<Request> requestList;
    private Activity context;
    private OnRequestClickListener onRequestClickListener;

    public RequestAdapter(Activity context) {
        this.context = context;
        this.requestList = new ArrayList<>();
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
        Collections.sort(this.requestList, (o1, o2) -> {
            if (o1.getStatus() > o2.getStatus()){
                return 1;
            }
            if (o1.getStatus() < o2.getStatus()) {
                return -1;
            }
            return 0;
        });
        notifyDataSetChanged();
    }

    public void setOnRequestClickListener(OnRequestClickListener onRequestClickListener) {
        this.onRequestClickListener = onRequestClickListener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_request, viewGroup, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder requestViewHolder, int i) {
        requestViewHolder.bind(requestList.get(i));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public interface OnRequestClickListener {
        void onclickRequest(Request request);
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.request_time)
        RelativeTimeTextView timePlaced;

        @BindView(R.id.status)
        TextView status;

        @BindView(R.id.driverName)
        TextView userName;

        @BindView(R.id.call)
        ImageView callbtn;

        private View view;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, this.view);
        }

        void bind(Request request) {
            timePlaced.setReferenceTime(request.getDateCreated());
            status.setText(getStatus(request.getStatus()));
            userName.setText(request.getUserName());

            view.setOnClickListener(v -> {
                if (onRequestClickListener != null) {
                    onRequestClickListener.onclickRequest(request);
                }
            });

            callbtn.setOnClickListener(v -> {
                if (request.getIscompleted() || request.getIscancelled()){
                    Toast.makeText(context, "You can't place a call to User right now.", Toast.LENGTH_SHORT).show();
                    return;
                }

                launchDialer(request.getUserTelephone());
            });
        }

        void launchDialer(String telephone) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephone));
            context.startActivity(intent);
        }

        String getStatus(int status) {
            String result = "";
            switch (status){
                case -1:
                    result = "Cancelled";
                    break;
                case 0:
                    result = "Placed";
                    break;
                case 1:
                    result = "Confirmed";
                    break;
                case 2:
                    result = "In Progress";
                    break;
                case 3:
                    result = "Completed";
                    break;
            }
            return result;
        }
    }
}
