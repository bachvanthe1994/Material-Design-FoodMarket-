package com.thebv.foodmarket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by thebv on 25/09/2016.
 */


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private int countProduct;
    private ListProductFragment.OnClickProduct onClickProduct;
    private Map<Integer, Integer> mapCartProduct = new HashMap<>();
    private Activity context;

    public void setOnClickProduct(ListProductFragment.OnClickProduct onClickProduct) {
        this.onClickProduct = onClickProduct;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivAdd;
        public ImageView ivRemove;
        public TextView tvCart;
        public LinearLayout llCart;
        public ImageView ivImage;

        public ViewHolder(View view) {
            super(view);
            ivAdd = (ImageView) view.findViewById(R.id.ivAdd);
            ivRemove = (ImageView) view.findViewById(R.id.ivRemove);
            tvCart = (TextView) view.findViewById(R.id.tvCart);
            llCart = (LinearLayout) view.findViewById(R.id.llCart);
            ivImage = (ImageView) view.findViewById(R.id.ivImage);
        }
    }

    public ProductAdapter(Activity context, int countProduct) {
        this.context = context;
        this.countProduct = countProduct;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        float cornerRadius = context.getResources().getDimension(R.dimen._2sdp);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                context.getResources(),
                scaleImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.img1), ImageView.ScaleType.CENTER_CROP)
        );
        roundedBitmapDrawable.setCornerRadius(cornerRadius);
        roundedBitmapDrawable.setAntiAlias(true);
        holder.ivImage.setImageDrawable(roundedBitmapDrawable);

        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickProduct.onAdd();
                holder.ivRemove.setVisibility(View.VISIBLE);
                if (mapCartProduct.get(position) == null)
                    mapCartProduct.put(position, 1);
                else
                    mapCartProduct.put(position, mapCartProduct.get(position) + 1);
                holder.tvCart.setText(mapCartProduct.get(position) + "");
                holder.llCart.setVisibility(View.VISIBLE);
            }
        });


        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapCartProduct.get(position) - 1 >= 0) {
                    mapCartProduct.put(position, mapCartProduct.get(position) - 1);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onClickProduct.onRemove();
                                    holder.tvCart.setText(mapCartProduct.get(position) + "");
                                    if (mapCartProduct.get(position) == 0) {
                                        holder.ivRemove.setVisibility(View.GONE);
                                        holder.llCart.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }, 100);
                }
            }
        });


        if (mapCartProduct.get(position) == null || mapCartProduct.get(position) == 0) {
            holder.llCart.setVisibility(View.GONE);
            holder.ivRemove.setVisibility(View.GONE);
        } else {
            holder.tvCart.setText(mapCartProduct.get(position) + "");
            holder.llCart.setVisibility(View.VISIBLE);
            holder.ivRemove.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return countProduct;
    }


    public static class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.style_line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public Bitmap scaleImage(Bitmap bm, ImageView.ScaleType scaleType) {
        if (bm == null)
            return bm;

        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            int min = bm.getWidth() < bm.getHeight() ? bm.getWidth() : bm.getHeight();

            if (bm.getWidth() < bm.getHeight()) {
                return Bitmap.createBitmap(bm, 0, bm.getHeight() / 2 - min / 2, min, min);
            } else {
                return Bitmap.createBitmap(bm, bm.getWidth() / 2 - min / 2, 0, min, min);
            }

        }

        return bm;
    }
}
