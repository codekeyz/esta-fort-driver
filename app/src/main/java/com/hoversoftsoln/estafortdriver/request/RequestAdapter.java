package com.hoversoftsoln.estafortdriver.request;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.hoversoftsoln.estafortdriver.core.data.Request;
import com.hoversoftsoln.estafortdriver.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<Request> requestList;

    public RequestAdapter() {
        this.requestList = new ArrayList<>();
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
        notifyDataSetChanged();
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

    class RequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.request_time)
        RelativeTimeTextView timePlaced;

        @BindView(R.id.status)
        TextView status;

        @BindView(R.id.driverName)
        TextView driverName;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Request request) {
            timePlaced.setReferenceTime(request.getDateCreated());
            status.setText(getStatus(request.getStatus()));
            driverName.setText(request.getDriverName());
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
