package com.tourisz.agent.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tourisz.R;
import com.tourisz.entity.LocalFile;
import com.tourisz.util.listener.OnFileAddClickListener;

import java.io.File;
import java.util.ArrayList;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.MyViewHolder> {

    private final Context mContext;
    private ArrayList<LocalFile> list;
    private OnFileAddClickListener fileAddClickListener;

    public FileListAdapter(Context mContext, ArrayList<LocalFile> localFiles, OnFileAddClickListener fileAddClickListener) {
        this.mContext = mContext;
        this.list = localFiles;
        this.fileAddClickListener = fileAddClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext( ) )
                                      .inflate( R.layout.add_images_list_item, parent, false );

        return new MyViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final LocalFile localFile = list.get( position );
        if ( !localFile.getFname( ).equalsIgnoreCase( "add*" ) ) {
            Glide.with( mContext ).load( new File( localFile.getPath( ) ) )
                 .placeholder( R.drawable.ic_banner_placeholder )
                 .crossFade( )
                 .thumbnail( 0.1f )
                 .into( holder.img );
        }

        holder.img.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                if ( localFile.getFname( ).equalsIgnoreCase( "add*" ) ) {
                    if ( fileAddClickListener != null ) {
                        fileAddClickListener.onAddClick( );
                    }
                }
            }
        } );


    }

    @Override
    public int getItemCount() {
        return list.size( );
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        final ImageView img;


        MyViewHolder(View view) {
            super( view );
            img = ( ImageView ) view.findViewById( R.id.img );
        }
    }
}
