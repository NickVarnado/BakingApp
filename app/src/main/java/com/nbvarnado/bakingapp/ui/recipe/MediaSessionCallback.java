package com.nbvarnado.bakingapp.ui.recipe;

import android.support.v4.media.session.MediaSessionCompat;

import com.google.android.exoplayer2.ExoPlayer;

public class MediaSessionCallback extends MediaSessionCompat.Callback {

    private ExoPlayer mExoPlayer;

    MediaSessionCallback(ExoPlayer player) {
        this.mExoPlayer = player;
    }

    @Override
    public void onPlay() {
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
        mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onSkipToPrevious() {
        mExoPlayer.seekTo(0);
    }

}
