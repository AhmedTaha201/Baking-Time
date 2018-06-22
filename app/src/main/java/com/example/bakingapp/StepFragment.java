package com.example.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.data.Recipe;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Taha on 6/17/2018.
 */

public class StepFragment extends Fragment {

    public static final String BUNDLE_KEY_POSITION = "position_key";
    public static final String BUNDLE_KEY_PLAY_WHEN_READY = "play_key";
    Recipe.Step mStep;
    Uri mMediaUri;
    Bundle mSavedInstanceState = null;
    SimpleExoPlayer mPlayer;
    long mMediaPosition;
    boolean mPlayWhenReady;
    @BindView(R.id.media_view)
    SimpleExoPlayerView mPlayerView;

    @Nullable //Available only in portrait mode
    @BindView(R.id.step_data)
    TextView mStepData;

    @BindView(R.id.player_empty_view)
    ImageView mPlayerEmptyView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        if (mSavedInstanceState != null) {
            initPlayer(mMediaUri);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);
        if (mStep != null) {
            //Step description
            if (mStepData != null) { //Present only in portrait mode
                mStepData.setText(mStep.getDescription());
            }

            mMediaUri = getMediaUri();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mPlayer != null) {
            mMediaPosition = mPlayer.getCurrentPosition();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
        }

        outState.putLong(BUNDLE_KEY_POSITION, mMediaPosition);
        outState.putBoolean(BUNDLE_KEY_PLAY_WHEN_READY, mPlayWhenReady);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) initPlayer(mMediaUri);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) initPlayer(mMediaUri);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23 || mPlayer == null) releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) releasePlayer();
    }

    public void setStep(Recipe.Step mStep) {
        this.mStep = mStep;
    }

    //Prepare the player with the view and the data source
    public void initPlayer(Uri mediaSourceUri) {//Skipping if the uri is empty or null
        if (mediaSourceUri == null || TextUtils.isEmpty(mediaSourceUri.toString())) {
            return;
        }
        //Showing the Player view
        mPlayerView.setVisibility(View.VISIBLE);
        mPlayerEmptyView.setVisibility(View.GONE);
        //MediaPlayer
        mPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        mPlayerView.setPlayer(mPlayer);
        //MediaSource
        MediaSource mediaSource = buildMediaSource(mediaSourceUri);
        if (mSavedInstanceState != null
                && mSavedInstanceState.containsKey(BUNDLE_KEY_POSITION)
                && mSavedInstanceState.containsKey(BUNDLE_KEY_PLAY_WHEN_READY)) {

            mPlayer.seekTo(mSavedInstanceState.getLong(BUNDLE_KEY_POSITION));
            mPlayer.setPlayWhenReady(mSavedInstanceState.getBoolean(BUNDLE_KEY_PLAY_WHEN_READY));
            mPlayer.prepare(mediaSource);
            return;
        }
        mPlayer.prepare(mediaSource);
        mPlayer.setPlayWhenReady(true);

    }

    //Release the player and save the associated data
    private void releasePlayer() {
        if (mPlayer == null) return;
        ///Saving the player data to save it in the out bundle as the releasePlayer method is called in onPause or onStop always before the activity`s onSaveInstanceState
        mMediaPosition = mPlayer.getContentPosition();
        mPlayWhenReady = mPlayer.getPlayWhenReady();

        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    //A helper method to build the media source using a give uri
    private MediaSource buildMediaSource(Uri uri) {
        String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri);
    }

    private Uri getMediaUri() {
        //Step media
        String url = mStep.getVideoURL(); //Video url
        if (url == null || TextUtils.isEmpty(url)) {//No video url instead we would use the thumbnail
            url = mStep.getThumbnailURL();
        }
        if (url == null || TextUtils.isEmpty(url)) {//No video or thumbnail instead we use the empty view
            mPlayerView.setVisibility(View.GONE);
            mPlayerEmptyView.setVisibility(View.VISIBLE);
        }
        return Uri.parse(url);
    }

}