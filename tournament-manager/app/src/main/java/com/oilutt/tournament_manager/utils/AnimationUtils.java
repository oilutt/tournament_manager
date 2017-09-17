package com.oilutt.tournament_manager.utils;

import android.os.Handler;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

/**
 * Created by oilut on 21/08/17.
 */
public class AnimationUtils {

    public static void fadeOut(final View view) {
        if (view != null) {
            if (view.getVisibility() == View.INVISIBLE) return;
            YoYo.with(Techniques.FadeOut)
                    .duration(400)
                    .playOn(view);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            }, 410);
        }
    }

    public static void fadeOutWithDuration(final View view, int duration, Animator.AnimatorListener animatorListener) {
        if (view != null) {
            YoYo.with(Techniques.FadeOut)
                    .duration(duration)
                    .withListener(animatorListener)
                    .playOn(view);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.INVISIBLE);
                }
            }, duration);
        }
    }

    public static void fadeIn(View view) {
        if (view != null) {
            if (view.getVisibility() == View.VISIBLE) return;
            YoYo.with(Techniques.FadeIn)
                    .duration(400)
                    .playOn(view);
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void fadeInWithDuration(View view, int duration) {
        if (view != null) {
            YoYo.with(Techniques.FadeIn)
                    .duration(duration)
                    .playOn(view);
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void crossView(final View viewShow, View viewHide) {
        try {
            fadeOut(viewHide);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fadeIn(viewShow);
                }
            }, 410);
        } catch (Exception ex) {
        }
    }

    public static void shakeError(View view) {
        if (view != null) {
            YoYo.with(Techniques.Shake)
                    .duration(200)
                    .playOn(view);
        }
    }

    public static void fadeOutInvisible(final View view) {
        if (view != null) {
            YoYo.with(Techniques.FadeOut)
                    .duration(400)
                    .playOn(view);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.INVISIBLE);
                }
            }, 410);
        }
    }


    public static void zoomIn(View view) {
        if (view != null) {
            YoYo.with(Techniques.ZoomIn)
                    .duration(400)
                    .playOn(view);
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void zoomOut(final View view) {
        if (view != null) {
            YoYo.with(Techniques.ZoomOut)
                    .duration(400)
                    .playOn(view);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            }, 410);
        }
    }


    public static void slideOutUp(final View view) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            YoYo.with(Techniques.SlideOutUp)
                    .duration(400)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            view.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .playOn(view);

        }
    }

    public static void slideInDown(final View view) {
        if (view != null && view.getVisibility() == View.GONE) {
            YoYo.with(Techniques.SlideInDown)
                    .duration(400)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            view.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .playOn(view);
        }
    }

    public static void fadeOutRight(final View view) {
        if (view.getVisibility() == View.GONE) return;
        YoYo.with(Techniques.FadeOutRight)
                .duration(200)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(view);
    }

    public static void fadeInRight(final View view) {
        if (view.getVisibility() == View.VISIBLE) return;
        YoYo.with(Techniques.FadeInRight)
                .duration(200)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(view);
    }

    public static void piscaPisca(final View view) {
        YoYo.with(Techniques.FadeOut)
                .duration(500)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        YoYo.with(Techniques.FadeIn)
                                .duration(500)
                                .withListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                })
                                .playOn(view);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(view);
    }

}