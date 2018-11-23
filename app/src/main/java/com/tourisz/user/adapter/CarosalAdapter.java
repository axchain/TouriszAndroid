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
import com.tourisz.api.response.FlyerData;

import java.util.ArrayList;


public class CarosalAdapter extends PagerAdapter {


    private ArrayList<FlyerData> data;
    private LayoutInflater inflater;
    private Context context;


    public CarosalAdapter(Context context, ArrayList<FlyerData> data) {
        this.context = context;
        this.data=data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate( R.layout.fragment_carousel, view, false );

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.img);
        Glide.with( context ).load( URLS.newInstance().getFLYERSIMG_URL() + data.get( position ).getPoster() )
             .placeholder(R.drawable.ic_banner_placeholder)
             .error(R.drawable.ic_banner_placeholder)
             .crossFade()
             .diskCacheStrategy( DiskCacheStrategy.ALL )
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