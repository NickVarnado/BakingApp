package com.nbvarnado.bakingapp.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nbvarnado.bakingapp.R;

import com.nbvarnado.bakingapp.data.database.recipe.Recipe;
import com.nbvarnado.bakingapp.data.database.recipe.Step;
import com.nbvarnado.bakingapp.ui.ingredients.IngredientsActivity;
import com.nbvarnado.bakingapp.ui.ingredients.IngredientsFragment;
import com.nbvarnado.bakingapp.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity {

    private static final String RECIPE_KEY = "recipe";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Recipe mRecipe;
    private HashMap<Integer, Step> mStepMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle((mRecipe != null) ? mRecipe.getName() : "");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
        } else {
            Intent intent = getIntent();
            mRecipe = intent.getParcelableExtra(MainActivity.EXTRA_RECIPE);
        }

        if (mRecipe != null) {
            mStepMap = new HashMap();
            for (Step step : mRecipe.getSteps()) {
                mStepMap.put(step.getId(), step);
            }
        }

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        Button ingredientsButton = findViewById(R.id.button_steps_ingredients);
        ingredientsButton.setOnClickListener(view -> {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(IngredientsFragment.ARG_RECIPE, mRecipe);
                IngredientsFragment fragment = new IngredientsFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, IngredientsActivity.class);
                intent.putExtra(MainActivity.EXTRA_RECIPE, mRecipe);
                context.startActivity(intent);
            }
        });

        View recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<Step> steps = (mRecipe != null) ? mRecipe.getSteps() : new ArrayList<>();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, steps, mStepMap, mTwoPane));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_KEY, mRecipe);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final StepListActivity mParentActivity;
        private final List<Step> mSteps;
        private final HashMap<Integer, Step> mStepMap;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step step = (Step) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(StepDetailFragment.ARG_STEP, step);
                    StepDetailFragment fragment = new StepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepDetailActivity.class);
                    intent.putExtra(StepDetailFragment.ARG_STEP, step);
                    intent.putExtra(StepDetailActivity.ARG_STEP_MAP, mStepMap);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(StepListActivity parent,
                                      List<Step> items,
                                      HashMap<Integer, Step> stepMap,
                                      boolean twoPane) {
            mSteps = items;
            mParentActivity = parent;
            mStepMap = stepMap;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Step step = mSteps.get(position);
            if (step.getId() > 0) {
                holder.mIdView.setText(String.valueOf(step.getId()));
            }
            holder.mContentView.setText(step.getShortDescription());



            holder.itemView.setTag(mSteps.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if (mSteps == null) return 0;
            return mSteps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }

    }
}
