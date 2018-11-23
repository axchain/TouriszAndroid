package com.tourisz.user.adapter;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tourisz.R;
import com.tourisz.api.URLS;


public class SlidingImageAdapter extends PagerAdapter {


    private String IMAGES[];
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapter(Context context,String IMAGES[]) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate( R.layout.fragment_carousel, view, false );

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.img);
        Glide.with( context ).load( URLS.newInstance().getIMG_URL() + IMAGES[ position ] )
             .placeholder(R.drawable.ic_banner_placeholder)
             .error(R.drawable.ic_banner_placeholder)
             .crossFade()
             .diskCacheStrategy( DiskCacheStrategy.NONE )
             .into(imageView);


        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}