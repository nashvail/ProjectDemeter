package com.nashvail.projectdemeter;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

/*
* Class : MainFragment
* --------------------------------------------
* Holds the views for the main view of the app
 */

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private TextView mAmountView;

    private TextSwitcher mSwitcher;
    private TextView amountView;

    private static final int DECREASE = -1;
    private static final int INCREASE = 1;
    private static final int SMALL_STEP = 1;
    private static final int LARGE_STEP = 10;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mSwitcher = (TextSwitcher) v.findViewById(R.id.textSwitcher);
        mAmountView = (TextView) v.findViewById(R.id.text_view_amount);
        mSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                amountView = new TextView(getActivity());
                amountView.setGravity(Gravity.CENTER);
                amountView.setTextSize(115);
                amountView.setTextColor(Color.WHITE);
                amountView.setTypeface(null, Typeface.BOLD);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                amountView.setLayoutParams(params);
                return amountView;
            }
        });

        // The required animations for animating the text in view
        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slidedown_in);
        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slidedown_out);
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);

        // Set the default text for the TextSwitcher
        mSwitcher.setText(Integer.toString(0));

        new SwipeDetector(mSwitcher).setOnSwipeListener(new SwipeDetector.onSwipeEvent(){
            @Override
            public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
                if (swipeType == SwipeDetector.SwipeTypeEnum.LEFT_TO_RIGHT) {
                    changeAmountInView(LARGE_STEP, DECREASE);
                } else if (swipeType == SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT) {
                    changeAmountInView(LARGE_STEP, INCREASE);
                } else if(swipeType == SwipeDetector.SwipeTypeEnum.BOTTOM_TO_TOP){
                    changeAmountInView(SMALL_STEP, INCREASE);
                }else{
                    changeAmountInView(SMALL_STEP,DECREASE);
                }
            }
        });

        return v;
    }

    /*
    * Function : changeAmountInView(amount to change by, INCREASE or DECREASE)
    * ------------------------------------------------------------------------
    * Changes the amount visible in the Text View in the app by the difference
    * that is equal to what is supplied as the first argument
    * INCREASE = 1, DECREASE = -1, are initialized as constants
    * In the second argument if INCREASE(1) is supplied then increases the amount in
    * the view by the supplied amount, if DECREASE(-1) is supplied then decreases the amount
    *
    * Avoids going negative.
    */

    private void changeAmountInView(int changeBy, int increaseOrDecrease) {
        int currentAmount = (int) Integer.parseInt(amountView.getText().toString());
        // increaseOrDecrease can hold a value of only 1 or -1
        int newAmount = currentAmount + ((changeBy) * increaseOrDecrease);
        if(newAmount < 0) return;

//        mAmountView.setText(Integer.toString(newAmount));
        amountView.setText(Integer.toString(newAmount));
        mSwitcher.setText(Integer.toString(newAmount));
    }


}
