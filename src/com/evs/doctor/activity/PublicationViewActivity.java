package com.evs.doctor.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;

import com.evs.doctor.R;
import com.evs.doctor.model.Comment;
import com.evs.doctor.model.Publication;
import com.evs.doctor.service.ApiService;
import com.evs.doctor.service.ApplicationException;
import com.evs.doctor.util.*;
import com.evs.doctor.view.*;
import com.evs.doctor.view.SlideGalleryView.OnImageClickListener;

public class PublicationViewActivity extends AsyncActivity<Object, Publication> {

    private Long mPublicationID;
    private ApiService mApiService;
    private Publication mPublication;
    private List<Comment> mCommentList;

    private SlideGalleryView mGalleryView;
    private NavigationBarView mNavigationBarView;

    // Content related views
    private TextView mContentHeader;
    private WebView mContent;
    private View mContentContainer;
    private View mPostCommentView;
    private TextView mPublicationDate;
    private TextView mPublicationCommentsCounter;
    private LinksView mLinksView;
    private ScrollView mScrollView;

    private LinearLayout mCommentsHolder;
    private EditText mCommentEditText;
    private LinearLayout mCommentsLoadingProgress;
    private ImageView mCommentsToggler;
    private ImageView mContentToggler;
    private Button mSendCommentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication_view);
        initUI();
        mApiService = ApiService.getInstance();
        mPublicationID = getPublicationID();

        if (savedInstanceState != null) {
            mPublication = (Publication) savedInstanceState.getSerializable(EXTRA_PUBLICATION);
            onTaskComplete(mPublication);
        } else {
            loadPublication();
        }
    }

    private void initUI() {
        mNavigationBarView = ((NavigationBarView) findViewById(R.id.view_navbar));
        mNavigationBarView.updateBar(getString(R.string.publication_list),
                getResources().getDrawable(R.drawable.ic_icon_arrow_left_white_back), this, this);

        mContentHeader = (TextView) findTextViewById(R.id.publication_view_content_header);
        mContent = (WebView) findViewById(R.id.publication_view_content);
        mContent.getSettings().setDefaultFontSize(14);

        mContentContainer = findViewById(R.id.publication_view_content_container);
        mLinksView = new LinksView((ViewGroup) findViewById(R.id.publication_links));
        mPublicationCommentsCounter = (TextView) findViewById(R.id.publication_view_comments_counter_tv);
        mScrollView = (ScrollView) findViewById(R.id.publication_view_scroll_view);

        mPublicationDate = (TextView) findViewById(R.id.publication_view_date);
        mCommentsHolder = (LinearLayout) findViewById(R.id.publication_view_comments_holder);
        mPostCommentView = (LinearLayout) findViewById(R.id.publication_view_post_comment_view);
        mCommentEditText = (EditText) findViewById(R.id.publication_view_write_comment);
        mCommentsLoadingProgress = (LinearLayout) findViewById(R.id.publication_view_comment_progressbar);
        mCommentsToggler = (ImageView) findViewById(R.id.publication_view_comment_arrow);
        mContentToggler = (ImageView) findViewById(R.id.publication_view_content_arrow);
        mSendCommentButton = (Button) findViewById(R.id.publication_view_send_comment);

        Button mAddCommentTextView = (Button) findViewById(R.id.publication_view_add_comment);
        Button mCancelCommentButton = (Button) findViewById(R.id.publication_view_cancel_comment);
        FrameLayout toggleCommentArrow = (FrameLayout) findViewById(R.id.publication_view_arrow_holder);
        FrameLayout toogleContentArrow = (FrameLayout) findViewById(R.id.publication_view_content_arrow_holder);

        mGalleryView = (SlideGalleryView) findViewById(R.id.publication_images);

        toogleContentArrow.setOnClickListener(this);
        toggleCommentArrow.setOnClickListener(this);
        mAddCommentTextView.setOnClickListener(this);
        mSendCommentButton.setOnClickListener(this);
        mCancelCommentButton.setOnClickListener(this);
    }

    /**
     * Loads publication by received in Intent ID
     */
    private void loadPublication() {
        doAsync();
    }

    /**{@inheritDoc}*/
    @Override
    public Publication doInBackgroundThread(Object... objects) throws ApplicationException {
        if (getPublicationID() != null) {
            mPublication = mApiService.getPublicationByID(getPublicationID());
        }
        return mPublication;
    }

    /**{@inheritDoc}*/
    @Override
    public void onTaskComplete(Publication publication) {
        if (publication != null) {
            mPublication = publication;
            bindPublicationData(publication);
            loadComments();
        } else {
            if (AppConfig.isDevMode()) {
                Logger.e(getClass(), "publication is null");
            }
        }

    }

    private void bindPublicationData(Publication publication) {
        mPublicationDate.setText(DateParser.parseDate(publication.getCreatedAt()));
        mPublicationCommentsCounter.setText(String.valueOf(publication.getCommentsCount()));
        mContentHeader.setText(publication.getTitle());
        mGalleryView.init(publication.getImages());

        final ArrayList<String> images = AndroidUtil.convertToStringArray(publication.getImages());
        mGalleryView.setOnImageClickListener(new OnImageClickListener() {
            @Override
            public void clickedItem(View v, String path) {
                Intent i = new Intent(AppConfig.getAppContext(), AttachmentActivity.class);
                i.putStringArrayListExtra(AttachmentActivity.EXTRA_IMAGES, images);
                i.putExtra(AttachmentActivity.EXTRA_EDITABLE, false);
                startActivity(i);
            }
        });

        if (publication.getPdfs() != null && publication.getPdfs().size() > 0) {
            mLinksView.addLinks(publication.getPdfs(), true);
        }
        if (publication.getLinks() != null && publication.getLinks().size() > 0) {
            mLinksView.addLinks(publication.getLinks(), false);
        }

        if (publication.getVideos() != null && publication.getVideos().size() > 0) {
            mLinksView.addLinks(publication.getVideos(), false);
        }

        toggleContentView();
    }

    /**
     * Shows or hides Publication's 'content' data in WebView
     */
    private void toggleContentView() {

        if (mContentContainer.getVisibility() == View.VISIBLE) {
            mContentContainer.setVisibility(View.GONE);
            mContentToggler.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_arrow_up_blue));
        } else {
            mContentContainer.setVisibility(View.VISIBLE);
            String content = mPublication.getContent();
            mContent.loadDataWithBaseURL("", content, "text/html", "UTF-8", null);
            mContentToggler.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_arrow_down_blue));
        }
    }

    /**{@inheritDoc}*/
    @Override
    protected void handleClick(View v) {
        switch (v.getId()) {
        case R.id.publication_view_content_arrow_holder:
            toggleContentView();
            break;
        case R.id.publication_view_arrow_holder:
            showCommentsView(mCommentsHolder.getVisibility() != View.VISIBLE);
            break;
        case R.id.publication_view_add_comment:
            showPostCommentView();
            break;
        case R.id.publication_view_send_comment:
            sendComment();
            break;
        case R.id.publication_view_cancel_comment:
            hidePostCommentView();
            break;
        case R.id.nav_title:
            finish();
            break;
        case R.id.nav_back_to_publication_list:
            finish();
            break;
        }
        super.handleClick(v);
    }

    /**{@inheritDoc}*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPublication != null) {
            outState.putSerializable(EXTRA_PUBLICATION, mPublication);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publication_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_update_comments:
            refreshCommentsList();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    // Comments related methods

    /**
     * Sends comment to the server
     */
    private void sendComment() {
        CommentSender commentSender = new CommentSender();
        commentSender.execute(mCommentEditText.getText().toString());
    }

    /**
     * Shows or hides comments
     */
    private void showCommentsView(boolean isVisible) {
        if (mCommentsHolder.getVisibility() == View.VISIBLE) {
            mCommentsHolder.setVisibility(View.GONE);
            mCommentsToggler.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_arrow_up_blue));
        } else {
            mCommentsHolder.setVisibility(View.VISIBLE);
            mCommentsToggler.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_arrow_down_blue));
        }
    }

    /**
     * Hides post comment view
     */
    private void hidePostCommentView() {
        mPostCommentView.setVisibility(View.GONE);
    }

    /**
     * Shows post comment view
     */
    private void showPostCommentView() {
        mPostCommentView.setVisibility(View.VISIBLE);
    }

    /**
     * Get publication's id
     * @return publication's ID
     */
    private Long getPublicationID() {
        Long publicationId = getIntent().getLongExtra(EXTRA_PUBLICATION_ID, 0);

        if (publicationId == null || publicationId == 0) {
            DialogUtils.showErrorDialog(this, getString(R.string.error_incorrect_publication_id), new Callback() {

                @Override
                public int doIt() {
                    finish();
                    return 0;
                }
            });
        }
        return publicationId;
    }

    /**
     * Loads comments for publication
     */
    private void loadComments() {
        CommentsLoader commentsLoader = new CommentsLoader();
        commentsLoader.execute();
    }

    private void refreshCommentsList() {
        mCommentsHolder.setVisibility(View.GONE);
        CommentsRefresher commentsRefresher = new CommentsRefresher();
        commentsRefresher.execute();
    }

    private void fillCommentsHolder() {
        if (mCommentList == null) {
            return;
        }

        LayoutInflater inflater = getLayoutInflater();
        mCommentsHolder.removeAllViews();

        for (Comment comment : mCommentList) {
            View commentView = inflater.inflate(R.layout.comment, null);
            bindComment(commentView, comment);
            mCommentsHolder.addView(commentView);
        }

        updateCommentsCounter(mCommentList.size());
    }

    private void updateCommentsCounter(int commentCounter) {
        mPublicationCommentsCounter.setText(String.valueOf(commentCounter));
    }

    private void bindComment(View view, Comment comment) {
        ((CachedImageView) view.findViewById(R.id.comment_userpic)).loadImage(comment.getAuthorAvatarUrl());
        AndroidUtil.setText(view, R.id.comment_tv_author_specialty, comment.getAuthorSpecialty());
        AndroidUtil.setText(view, R.id.comment_tv_author_full_name, comment.getAuthorFullName());
        AndroidUtil.setText(view, R.id.comment_tv_created_at, DateParser.parseDate(comment.getCreatedAt()));
        AndroidUtil.setText(view, R.id.comment_tv_content, comment.getContent());
    }

    class CommentsLoader extends android.os.AsyncTask<Void, Void, List<Comment>> {

        @Override
        protected List<Comment> doInBackground(Void... voids) {
            List<Comment> list = null;
            try {
                publishProgress();
                list = mApiService.getCommentsByPublicationID(getPublicationID());
            } catch (ApplicationException e) {
                // TODO: show for user
                Logger.e(getClass(), e);
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            super.onPostExecute(comments);
            mCommentsLoadingProgress.setVisibility(View.GONE);
            showCommentsView(comments != null);
            if (comments != null) {
                mCommentList = comments;
                fillCommentsHolder();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            mCommentsLoadingProgress.setVisibility(View.VISIBLE);
        }
    }

    class CommentsRefresher extends CommentsLoader {

        @Override
        protected void onPostExecute(List<Comment> comments) {
            super.onPostExecute(comments);
            mCommentsHolder.setVisibility(View.VISIBLE);
            mScrollView.post(new Runnable() {

                @Override
                public void run() {
                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    class CommentSender extends android.os.AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String commentText = strings[0];

            try {
                mApiService.createComment(mPublicationID, commentText, false, 0);
            } catch (ApplicationException e) {
                Logger.e(getClass(), e);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSendCommentButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCommentEditText.setText("");
            AndroidUtil.hideSoftKeyboard(PublicationViewActivity.this);
            hidePostCommentView();
            refreshCommentsList();
            mSendCommentButton.setEnabled(true);
            Toast.makeText(getApplicationContext(), getString(R.string.comment_created), Toast.LENGTH_LONG).show();
        }
    }

}
