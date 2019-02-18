package com.jordanmadrigal.lendsum.Adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jordanmadrigal.lendsum.Model.Package;
import com.jordanmadrigal.lendsum.R;
import com.jordanmadrigal.lendsum.Utility.FirebaseService;

import java.util.List;

import static com.jordanmadrigal.lendsum.Utility.Constants.BORROW_PACKAGE_COLLECTION;
import static com.jordanmadrigal.lendsum.Utility.Constants.LEND_PACKAGE_COLLECTION;
import static com.jordanmadrigal.lendsum.Utility.Constants.USER_COLLECTION;

public class LendPackageAdapter extends FirestoreRecyclerAdapter<Package, LendPackageAdapter.PackageViewHolder> {

    private Context context;
    private FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final String LOG_TAG = LendPackageAdapter.class.getSimpleName();

    public LendPackageAdapter(@NonNull FirestoreRecyclerOptions<Package> options, Context context) {
        super(options);
        this.context = context;
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder{

        private int rotationAngle = 0;
        private int minHeight;
        private CardView mCardView;
        private TextView mPackageHeaderText, mItemListText, mRateParaText, mReturnDateParaText, mItemsSubtext, mRateSubtext, mReturnDateSubtext, mUserNameText, mEmailText;
        private ImageButton mExpandBtn, mEditBtn, mDeleteBtn;


        public PackageViewHolder(View itemView) {
            super(itemView);

            mUserNameText = itemView.findViewById(R.id.dummyUsernameTextview);
            mItemsSubtext = itemView.findViewById(R.id.dummyItemsSubTextView);
            mRateSubtext = itemView.findViewById(R.id.dummyDaysLeftTextView);
            mReturnDateSubtext = itemView.findViewById(R.id.dummyReturnDateSubTextView);
            mCardView = itemView.findViewById(R.id.list);
            mExpandBtn = itemView.findViewById(R.id.packageExpandBtn);
            mEditBtn = itemView.findViewById(R.id.editBtn);
            mPackageHeaderText = itemView.findViewById(R.id.dummyBundleTitleTextView);
            mItemListText = itemView.findViewById(R.id.dummyItemListTextView);
            mRateParaText = itemView.findViewById(R.id.dummyDaysLeftParagraphTextView);
            mReturnDateParaText = itemView.findViewById(R.id.dummyReturnDateParagraphTextView);
            mDeleteBtn = itemView.findViewById(R.id.packageDeleteBtn);
            mEmailText = itemView.findViewById(R.id.packageEmailTextView);

            hideViews();

            mEmailText.setVisibility(View.INVISIBLE);
            //collapse and expand functionality and animation

            //Assign min height to cardvView
            final int height = 600;

            mCardView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mCardView.getViewTreeObserver().removeOnPreDrawListener(this);
                    minHeight = mCardView.getHeight();
                    ViewGroup.LayoutParams layoutParams = mCardView.getLayoutParams();
                    layoutParams.height = minHeight;
                    mCardView.setLayoutParams(layoutParams);

                    return true;
                }
            });

            //toggle height button
            mExpandBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleCardViewHeight(height);

                    //animate expand button
                    rotationAngle = rotationAngle == 0 ? 180 : 0;
                    mExpandBtn.animate().rotation(rotationAngle).setDuration(500).start();
                }
            });

        }

        private void toggleCardViewHeight(int height) {
            if(mCardView.getHeight() == minHeight){
                expandView(height);
            }else{
                collapseView();
            }
        }

        private void collapseView() {

            ValueAnimator animator = ValueAnimator.ofInt(mCardView.getMeasuredHeightAndState(), minHeight);

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int animatedValue = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = mCardView.getLayoutParams();
                    layoutParams.height = animatedValue;
                    mCardView.setLayoutParams(layoutParams);
                }
            });

            hideViews();

            animator.start();
        }

        private void expandView(int height) {
            ValueAnimator animator = ValueAnimator.ofInt(mCardView.getMeasuredHeightAndState(),
                    height);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int animatedValue = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = mCardView.getLayoutParams();
                    layoutParams.height = animatedValue;
                    mCardView.setLayoutParams(layoutParams);
                }
            });

            showViews();

            animator.start();
        }

        private void showViews(){
            mItemsSubtext.setVisibility(View.VISIBLE);
            mRateSubtext.setVisibility(View.VISIBLE);
            mReturnDateSubtext.setVisibility(View.VISIBLE);
            mItemListText.setVisibility(View.VISIBLE);
            mRateParaText.setVisibility(View.VISIBLE);
            mReturnDateParaText.setVisibility(View.VISIBLE);
            mEditBtn.setVisibility(View.VISIBLE);
            mDeleteBtn.setVisibility(View.VISIBLE);

        }

        private void hideViews(){
            mItemsSubtext.setVisibility(View.INVISIBLE);
            mRateSubtext.setVisibility(View.INVISIBLE);
            mReturnDateSubtext.setVisibility(View.INVISIBLE);
            mItemListText.setVisibility(View.INVISIBLE);
            mRateParaText.setVisibility(View.INVISIBLE);
            mReturnDateParaText.setVisibility(View.INVISIBLE);
            mEditBtn.setVisibility(View.INVISIBLE);
            mDeleteBtn.setVisibility(View.INVISIBLE);
        }


    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_view_items, parent, false);

        return new PackageViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position, @NonNull Package model) {

        holder.mUserNameText.setText(model.getBorrowerName());
        holder.mEmailText.setText(model.getBorrowerEmail());
        holder.mPackageHeaderText.setText(model.getPackageName());
        holder.mItemListText.setText(model.getItemList());
        holder.mRateParaText.setText(model.getPackageRate());

        if(model.getReturnDate() != null){
            holder.mReturnDateParaText.setText(model.getReturnDate());
        }else{
            holder.mReturnDateParaText.setText(R.string.indef_abbr);
        }

        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packHeader = holder.mPackageHeaderText.getText().toString();
                String borrowerEmail = holder.mEmailText.getText().toString();

                FirebaseService fbService = new FirebaseService(mUser, mDatabase);
                fbService.deleteDataFromFirebase(packHeader, borrowerEmail);

            }
        });

    }

}
