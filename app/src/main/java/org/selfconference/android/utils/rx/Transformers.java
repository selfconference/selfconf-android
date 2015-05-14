package org.selfconference.android.utils.rx;

import android.widget.ProgressBar;

import rx.Observable;
import rx.Observable.Transformer;
import rx.functions.Action0;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public final class Transformers {
    public static <T> Transformer<T, T> showAndHideProgressBar(final ProgressBar progressBar) {
        return new Transformer<T, T>() {
            @Override public Observable<T> call(Observable<T> observable) {
                return observable
                        .doOnSubscribe(new Action0() {
                            @Override public void call() {
                                progressBar.setVisibility(VISIBLE);
                            }
                        })
                        .doOnTerminate(new Action0() {
                            @Override public void call() {
                                progressBar.setVisibility(GONE);
                            }
                        });
            }
        };
    }

    private Transformers() {}
}
