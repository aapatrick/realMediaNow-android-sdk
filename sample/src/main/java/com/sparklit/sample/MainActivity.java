package com.sparklit.sample;

import android.content.Intent;
import android.drm.DrmStore;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.sparklit.adbutler.AdButler;
import com.sparklit.adbutler.Placement;
import com.sparklit.adbutler.PlacementRequestConfig;
import com.sparklit.adbutler.PlacementResponse;
import com.sparklit.adbutler.PlacementResponseListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends YouTubeBaseActivity {

    YouTubePlayerView youTubePlayerView;
    Button button;
    Button button2;

    YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = (Button)findViewById(R.id.bn);
        button2 = (Button)findViewById(R.id.button1);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
                youTubePlayer.setPlaybackEventListener(playbackEventListener);
                youTubePlayerView.setVisibility(View.VISIBLE);
                youTubePlayer.loadVideo("ATETjEhT4a8");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        button.setOnClickListener(new OnClickListener() {
            /*Initialising YouTube Player View when button clicked*/
            @Override
            public void onClick(View view) {
                youTubePlayerView.initialize(PlayerConfig.API_KEY,onInitializedListener);
                button.setVisibility(View.INVISIBLE);
            }
        });
    }

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {
            button2.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };


    public void requestPlacement(View view) {
        button2.setVisibility(View.INVISIBLE);
        String[] keywords = {"sample2"};
        Set<String> keywordSet = new HashSet<>();
        Collections.addAll(keywordSet, keywords);
        PlacementRequestConfig config = new PlacementRequestConfig.Builder(153105, 214764, 300, 250)
                .setKeywords(keywordSet)
                .build();
        AdButler adbutler = new AdButler();
        adbutler.requestPlacement(config, new PlacementResponseListener() {
            @Override
            public void success(PlacementResponse response) {
                System.out.println(response.getStatus());
                for (Placement placement : response.getPlacements()) {
                    System.out.println(placement.getBannerId());
                }

                if (response.getPlacements().size() > 0) {
                    final Placement placement = response.getPlacements().get(0);
                    if (placement != null) {
                        ImageView imageView = (ImageView) findViewById(R.id.imageButton);

                        imageView.setVisibility(View.VISIBLE);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(placement.getRedirectUrl()));
                                startActivity(intent);
                            }
                        });

                        Picasso.with(getBaseContext())
                                .load(placement.getImageUrl())
                                .resize(placement.getWidth(), placement.getHeight())
                                .into(imageView);

                        placement.recordImpression();
                    }
                }
            }

            @Override
            public void error(Throwable throwable) {

            }
        });
    }

    public void requestPlacements(View view) {
        PlacementRequestConfig config1 = new PlacementRequestConfig.Builder(153105, 214764, 300, 250).build();

        String[] keywords = {"sample2"};
        Set<String> keywordSet = new HashSet<>();
        Collections.addAll(keywordSet, keywords);
        PlacementRequestConfig config2 = new PlacementRequestConfig.Builder(153105, 214764, 300, 250)
                .setKeywords(keywordSet)
                .build();

        List<PlacementRequestConfig> configs = new ArrayList<>();
        configs.add(config1);
        configs.add(config2);

        AdButler adbutler = new AdButler();
        adbutler.requestPlacements(configs, new PlacementResponseListener() {
            @Override
            public void success(PlacementResponse response) {
                System.out.println(response.getStatus());
                for (Placement placement : response.getPlacements()) {
                    System.out.println(placement.getBannerId());
                }
            }

            @Override
            public void error(Throwable throwable) {

            }
        });
    }

    public void requestPixel(View view) {
        AdButler adbutler = new AdButler();
        adbutler.requestPixel("https://servedbyadbutler.com/default_banner.gif");
    }

    public void recordImpression(View view) {
        String[] keywords = {"sample2"};
        Set<String> keywordSet = new HashSet<>();
        Collections.addAll(keywordSet, keywords);
        PlacementRequestConfig config = new PlacementRequestConfig.Builder(153105, 214764, 300, 250)
                .setKeywords(keywordSet)
                .build();

        AdButler adbutler = new AdButler();
        adbutler.requestPlacement(config, new PlacementResponseListener() {
            @Override
            public void success(PlacementResponse response) {
                System.out.println(response.getStatus());
                for (Placement placement : response.getPlacements()) {
                    placement.recordImpression();
                }
            }

            @Override
            public void error(Throwable throwable) {

            }
        });
    }

    public void recordClick(View view) {
        String[] keywords = {"sample2"};
        Set<String> keywordSet = new HashSet<>();
        Collections.addAll(keywordSet, keywords);
        PlacementRequestConfig config = new PlacementRequestConfig.Builder(153105, 214764, 300, 250)
                .setKeywords(keywordSet)
                .build();

        AdButler adbutler = new AdButler();
        adbutler.requestPlacement(config, new PlacementResponseListener() {
            @Override
            public void success(PlacementResponse response) {
                System.out.println(response.getStatus());
                for (Placement placement : response.getPlacements()) {
                    placement.recordClick();
                }
            }

            @Override
            public void error(Throwable throwable) {

            }
        });
    }
}
