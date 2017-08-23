package br.com.uilquemessias.favoritemovies.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Locale;

import br.com.uilquemessias.favoritemovies.R;
import br.com.uilquemessias.favoritemovies.services.models.Movie;
import br.com.uilquemessias.favoritemovies.services.models.Review;
import br.com.uilquemessias.favoritemovies.ui.adapters.MoviesAdapter;
import br.com.uilquemessias.favoritemovies.ui.adapters.ReviewsAdapter;
import br.com.uilquemessias.favoritemovies.ui.adapters.VideosAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailsActivity extends AppCompatActivity implements VideosAdapter.ListItemClickListener {

    private static final String TAG = "MovieDetailsActivity";

    @BindView(R.id.iv_movie_backdrop)
    ImageView mIvBackdrop;
    @BindView(R.id.iv_movie_poster)
    ImageView mIvPoster;
    @BindView(R.id.tv_movie_title)
    TextView mTvTitle;
    @BindView(R.id.tv_movie_release_date)
    TextView mTvReleaseDate;
    @BindView(R.id.tv_movie_overview)
    TextView mTvOverview;
    @BindView(R.id.tv_movie_vote_average)
    TextView mTvVoteAverage;
    @BindView(R.id.rv_video_list)
    RecyclerView mRvVideoList;
    @BindView(R.id.rv_review_list)
    RecyclerView mRvReviewList;

    private Movie mMovie;

    private VideosAdapter mVideosAdapter;
    private ReviewsAdapter mReviewsAdapter;

    private Picasso mPicasso;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mUnbinder = ButterKnife.bind(this);

        if (getIntent().hasExtra(MovieListActivity.SELECTED_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(MovieListActivity.SELECTED_MOVIE);
        }


        if (mMovie == null) {
            onBackPressed();
            return;
        }

        bindViews();

        mVideosAdapter = new VideosAdapter(this, Arrays.asList(
                "AFN67cRapmM", "HJUI2Il3xnI",
                "pXXyJX2yy5Y", "mGNdqsTIMFc"
        ));
        mRvVideoList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRvVideoList.setHasFixedSize(true);
        mRvVideoList.setAdapter(mVideosAdapter);

        final Review myReview = new Review();
        myReview.setId("blablabla");
        myReview.setAuthor("Uilque Messias");
        myReview.setContent("I used to study a lot, but now I just broke my own record. I am studying more than never.\n\nBy the way, this movie is great!");

        mReviewsAdapter = new ReviewsAdapter(Arrays.asList(
                myReview, myReview,
                myReview, myReview,
                myReview, myReview,
                myReview, myReview,
                myReview, myReview,
                myReview, myReview
        ));
        mRvReviewList.setLayoutManager(new LinearLayoutManager(this));
        mRvReviewList.setHasFixedSize(true);
        mRvReviewList.setAdapter(mReviewsAdapter);
    }

    @Override
    protected void onDestroy() {
        if (mPicasso != null) {
            mPicasso.cancelRequest(mIvPoster);
            mPicasso.cancelRequest(mIvBackdrop);
            mPicasso = null;
        }

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

        super.onDestroy();
    }

    private void bindViews() {
        final Uri urlPoster = Uri.parse(MoviesAdapter.BASE_IMAGE_URL + mMovie.getPosterPath());
        final Uri urlBackdrop = Uri.parse(MoviesAdapter.BASE_IMAGE_LARGER_URL + mMovie.getBackdropPath());

        mPicasso = Picasso.with(this);
        mPicasso.load(urlPoster)
                .placeholder(R.drawable.movie_poster)
                .into(mIvPoster);
        mPicasso.load(urlBackdrop)
                .placeholder(R.drawable.movie_backdrop)
                .into(mIvBackdrop);

        mTvTitle.setText(mMovie.getTitle());
        mTvReleaseDate.setText(mMovie.getReleaseDate());
        mTvVoteAverage.setText(String.format(Locale.US, "%.2f", mMovie.getVoteAverage()));
        mTvOverview.setText(mMovie.getOverview());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(String key, Uri videoUri) {
        Toast.makeText(this, "key: " + key + " - videoUri: " + videoUri, Toast.LENGTH_SHORT).show();
    }
}
