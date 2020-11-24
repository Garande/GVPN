package com.garande.g_vpn.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.garande.g_vpn.R;
import com.garande.g_vpn.databinding.ItemServerBinding;
import com.garande.g_vpn.model.Server;

import java.util.ArrayList;

public class ServersListAdapter extends RecyclerView.Adapter<ServersListAdapter.ServersListViewHolder> {
    public ArrayList<Server> servers = new ArrayList<>();
    private Context context;

    private OnServerClickListener mOnServerClickListener;

    public void setOnServerClickListener(OnServerClickListener listener){
        this.mOnServerClickListener = listener;
    }


    public ServersListAdapter(Context context, ArrayList<Server> mServers){
        this.context = context;
        this.servers = mServers;
    }


    public void updateServers(ArrayList<Server> newServers){
//        Log.e("SERVER", "Updating serves");
        servers.clear();
        if(servers.addAll(newServers)){
            notifyDataSetChanged();
        }
    }

    public interface OnServerClickListener {
        void onServerClick(Server server);
    }

    @NonNull
    @Override
    public ServersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemServerBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_server, parent, false );
        return new ServersListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServersListViewHolder holder, int position) {
        holder.binding.setServer(servers.get(position));
        holder.binding.setClickListener(mOnServerClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return servers.size();
    }

    public class ServersListViewHolder extends RecyclerView.ViewHolder {
        private final ItemServerBinding binding;
        public ServersListViewHolder(@NonNull ItemServerBinding itemServerBinding) {
            super(itemServerBinding.getRoot());
            this.binding = itemServerBinding;
        }
    }
}
