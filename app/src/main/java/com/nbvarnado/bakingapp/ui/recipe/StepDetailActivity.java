package com.nbvarnado.bakingapp.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.NavUtils;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.nbvarnado.bakingapp.R;
import com.nbvarnado.bakingapp.data.database.recipe.Step;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

    public static final String ARG_STEP_MAP = "step_map";

    // Views
    @BindView(R.id.detail_toolbar) Toolbar toolbar;
    @BindView(R.id.button_previous_step) Button prevButton;
    @BindView(R.id.button_next_step) Button nextButton;

    private Step mStep;
    private HashMap<Integer, Step> mStepMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mStep = getIntent().getParcelableExtra(StepDetailFragment.ARG_STEP);
        mStepMap = (HashMap<Integer, Step>) getIntent().getSerializableExtra(ARG_STEP_MAP);
        // Remove the previous and next button from the view on the first and last step respectively.
        if (mStep.getId() == 0) {
            prevButton.setVisibility(View.GONE);
        } else if (mStep.getId() == mStepMap.size() - 1) {
            nextButton.setVisibility(View.GONE);
        }
        prevButton.setOnClickListener(view -> {
            if (mStep != null && mStepMap != null) {
                int stepId = mStep.getId();
                if (stepId == 0) return;
                Step prevStep = mStepMap.get(stepId - 1);
                Context context = view.getContext();
                Intent intent = new Intent(context, StepDetailActivity.class);
                intent.putExtra(StepDetailFragment.ARG_STEP, prevStep);
                intent.putExtra(ARG_STEP_MAP, mStepMap);
                context.startActivity(intent);
            }
        });
        nextButton.setOnClickListener(view -> {
            if (mStep != null && mStepMap != null) {
                int stepId = mStep.getId();
                if (stepId == mStepMap.size() - 1) return;
                Step nextStep = mStepMap.get(stepId + 1);
                Context context = view.getContext();
                Intent intent = new Intent(context, StepDetailActivity.class);
                intent.putExtra(StepDetailFragment.ARG_STEP, nextStep);
                intent.putExtra(ARG_STEP_MAP, mStepMap);
                context.startActivity(intent);
            }

        });

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Step step = getIntent().getParcelableExtra(StepDetailFragment.ARG_STEP);
            Bundle arguments = new Bundle();
            arguments.putParcelable(StepDetailFragment.ARG_STEP, step);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
