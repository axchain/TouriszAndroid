package com.tourisz.agent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tourisz.R;
import com.tourisz.api.URLS;
import com.tourisz.entity.LocalFile;
import com.tourisz.util.listener.OnFileAddClickListener;

import java.io.File;
import java.util.ArrayList;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    private final Context mContext;
    private String IMAGES[];
    private OnFileAddClickListener fileAddClickListener;

    public ImageListAdapter(Context mContext, String IMAGES[], OnFileAddClickListener fileAddClickListener) {
        this.mContext = mContext;
        this.IMAGES = IMAGES;
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
        final String path = IMAGES[position];
        Log.e( "img", path );
        if ( !path.equalsIgnoreCase( "add*" ) ) {
            Glide.with( mContext ).load( URLS.newInstance( ).getIMG_URL( ) + path )
                 .placeholder( R.drawable.ic_banner_placeholder )
                 .crossFade( )
                 .thumbnail( 0.1f )
                 .into( holder.img );
        }

        holder.img.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                if ( path.equalsIgnoreCase( "add*" ) ) {
                    if ( fileAddClickListener != null ) {
                        fileAddClickListener.onAddClick( );
                    }
                }
            }
        } );


    }

    @Override
    public int getItemCount() {
        return IMAGES.length;
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
