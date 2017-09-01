package me.twobirds.twobirdsexer.host.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twobirds.sdk.animation.ExplosionField.ExplosionField;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.twobirds.twobirdsexer.host.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartActivity extends BaseActivity {

    @BindView(R.id.tv_start_page_app_name)
    TextView tvAppName;
    @BindView(R.id.rl_start_page_ring)
    RelativeLayout rlRing;
    @BindView(R.id.rl_start_page)
    RelativeLayout rlStartPage;

    private ExplosionField explosionField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        ButterKnife.bind(this);

        explosionField = ExplosionField.attach2Window(this);
        explosionField.setOnExplosionAnimationListener(new ExplosionField.onExplosionAnimationListener() {
            @Override
            public void onExplosionAnimationStart(Animator animator) {

            }

            @Override
            public void onExplosionAnimationEnd(Animator animator) {
                finish();
            }

            @Override
            public void onExplosionAnimationCancel(Animator animator) {

            }

            @Override
            public void onExplosionAnimationRepeat(Animator animator) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                explosionField.explode(rlRing);
            }
        }, 2000);

    }

}
