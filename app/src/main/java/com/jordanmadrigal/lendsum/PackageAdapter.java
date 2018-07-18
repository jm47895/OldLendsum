package com.jordanmadrigal.lendsum;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder>{

    private List<Package> mPackageDataSet;

    public PackageAdapter(List<Package> packageList) {
        mPackageDataSet = packageList;
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder{

        private int rotationAngle = 0;
        private int minHeight;
        private CardView cardView;
        private TextView packageHeaderText, itemsParaText, rateParaText, returnDateParaText, itemsSubtext, rateSubtext, returnDateSubtext, userNameText;
        private ImageButton expandBtn, msgBtn, editBtn;

        public PackageViewHolder(View itemView) {
            super(itemView);

            userNameText = itemView.findViewById(R.id.dummyUsernameTextview);
            itemsSubtext = itemView.findViewById(R.id.dummyItemsSubTextView);
            rateSubtext = itemView.findViewById(R.id.dummyRateSubTextView);
            returnDateSubtext = itemView.findViewById(R.id.dummyReturnDateSubTextView);
            cardView = itemView.findViewById(R.id.list);
            expandBtn = itemView.findViewById(R.id.packageExpandBtn);
            msgBtn = itemView.findViewById(R.id.packageMsgBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
            packageHeaderText = itemView.findViewById(R.id.dummyBundleTitleTextView);
            itemsParaText = itemView.findViewById(R.id.dummyItemsParagraphTextView);
            rateParaText = itemView.findViewById(R.id.dummyRateParagraphTextView);
            returnDateParaText = itemView.findViewById(R.id.dummyReturnDateParagraphTextView);

            hideViews();

            //collapse and expand functionality and animation
            final int height = 600;

            //Assign min height to cardvView
            cardView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cardView.getViewTreeObserver().removeOnPreDrawListener(this);
                    minHeight = cardView.getHeight();
                    ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                    layoutParams.height = minHeight;
                    cardView.setLayoutParams(layoutParams);

                    return true;
                }
            });

            //toggle height button
            expandBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleCardViewHeight(height);

                    //animate expand button
                    rotationAngle = rotationAngle == 0 ? 180 : 0;
                    expandBtn.animate().rotation(rotationAngle).setDuration(500).start();
                }
            });


        }

        private void toggleCardViewHeight(int height) {
            if(cardView.getHeight() == minHeight){
                expandView(height);
            }else{
                collapseView();
            }
        }

        private void collapseView() {

            ValueAnimator animator = ValueAnimator.ofInt(cardView.getMeasuredHeightAndState(), minHeight);

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int animatedValue = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                    layoutParams.height = animatedValue;
                    cardView.setLayoutParams(layoutParams);
                }
            });

            hideViews();

            animator.start();
        }

        private void expandView(int height) {
            ValueAnimator animator = ValueAnimator.ofInt(cardView.getMeasuredHeightAndState(),
                    height);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int animatedValue = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                    layoutParams.height = animatedValue;
                    cardView.setLayoutParams(layoutParams);
                }
            });

            showViews();

            animator.start();
        }

        private void showViews(){
            itemsSubtext.setVisibility(View.VISIBLE);
            rateSubtext.setVisibility(View.VISIBLE);
            returnDateSubtext.setVisibility(View.VISIBLE);
            itemsParaText.setVisibility(View.VISIBLE);
            rateParaText.setVisibility(View.VISIBLE);
            returnDateParaText.setVisibility(View.VISIBLE);
            msgBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.VISIBLE);
        }

        private void hideViews(){
            itemsSubtext.setVisibility(View.INVISIBLE);
            rateSubtext.setVisibility(View.INVISIBLE);
            returnDateSubtext.setVisibility(View.INVISIBLE);
            itemsParaText.setVisibility(View.INVISIBLE);
            rateParaText.setVisibility(View.INVISIBLE);
            returnDateParaText.setVisibility(View.INVISIBLE);
            msgBtn.setVisibility(View.INVISIBLE);
            editBtn.setVisibility(View.INVISIBLE);
        }


    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_view_items, parent, false);

        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PackageViewHolder holder, int position) {

        final Package packageData = mPackageDataSet.get(position);

        holder.userNameText.setText(packageData.getUserName());
        holder.packageHeaderText.setText(packageData.getPackageName());
        holder.itemsParaText.setText(packageData.getItemDescription());
        holder.rateParaText.setText(packageData.getPackageRate());
        holder.returnDateParaText.setText(packageData.getReturnDate());
    }


    @Override
    public int getItemCount() {
        return mPackageDataSet.size();
    }


}
