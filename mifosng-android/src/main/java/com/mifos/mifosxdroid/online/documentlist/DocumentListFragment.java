/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package com.mifos.mifosxdroid.online.documentlist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mifos.mifosxdroid.R;
import com.mifos.mifosxdroid.adapters.DocumentListAdapter;
import com.mifos.mifosxdroid.core.MifosBaseActivity;
import com.mifos.mifosxdroid.core.MifosBaseFragment;
import com.mifos.mifosxdroid.core.RecyclerItemClickListener;
import com.mifos.mifosxdroid.core.util.Toaster;
import com.mifos.mifosxdroid.dialogfragments.documentdialog.DocumentDialogFragment;
import com.mifos.objects.noncore.Document;
import com.mifos.utils.CheckSelfPermissionAndRequest;
import com.mifos.utils.Constants;
import com.mifos.utils.FragmentConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

public class DocumentListFragment extends MifosBaseFragment implements DocumentListMvpView,
        RecyclerItemClickListener.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final int MENU_ITEM_ADD_NEW_DOCUMENT = 1000;

    public static final String LOG_TAG = DocumentListFragment.class.getSimpleName();

    @BindView(R.id.rv_documents)
    RecyclerView rv_documents;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.noDocumentText)
    TextView mNoChargesText;

    @BindView(R.id.noDocumentIcon)
    ImageView mNoChargesIcon;

    @BindView(R.id.ll_error)
    LinearLayout ll_error;

    @Inject
    DocumentListPresenter mDocumentListPresenter;

    @Inject
    DocumentListAdapter mDocumentListAdapter;

    private View rootView;
    private String entityType;
    private int entityId;
    private Document document;
    private ResponseBody documentBody;
    private List<Document> mDocumentList;

    public static DocumentListFragment newInstance(String entityType, int entiyId) {
        DocumentListFragment fragment = new DocumentListFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ENTITY_TYPE, entityType);
        args.putInt(Constants.ENTITY_ID, entiyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(View childView, int position) {
        document = mDocumentList.get(position);
        showDocumentPopUpMenu(mDocumentList.get(position).getId());
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        mDocumentList = new ArrayList<>();
        if (getArguments() != null) {
            entityType = getArguments().getString(Constants.ENTITY_TYPE);
            entityId = getArguments().getInt(Constants.ENTITY_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_document_list, container, false);

        ButterKnife.bind(this, rootView);
        mDocumentListPresenter.attachView(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_documents.setLayoutManager(layoutManager);
        rv_documents.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        rv_documents.setHasFixedSize(true);
        rv_documents.setAdapter(mDocumentListAdapter);
        swipeRefreshLayout.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);

        mDocumentListPresenter.loadDocumentList(entityType, entityId);

        return rootView;
    }

    @Override
    public void onRefresh() {
        mDocumentListPresenter.loadDocumentList(entityType, entityId);
    }

    @OnClick(R.id.noDocumentIcon)
    public void reloadOnError() {
        ll_error.setVisibility(View.GONE);
        mDocumentListPresenter.loadDocumentList(entityType, entityId);
    }

    /**
     * This Method Checking the Permission WRITE_EXTERNAL_STORAGE is granted or not.
     * If not then prompt user a dialog to grant the WRITE_EXTERNAL_STORAGE permission.
     * and If Permission is granted already then Save the documentBody in external storage;
     */
    @Override
    public void checkPermissionAndRequest() {
        if (CheckSelfPermissionAndRequest.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            checkExternalStorageAndCreateDocument();
        } else {
            requestPermission();
        }
    }

    @Override
    public void requestPermission() {
        CheckSelfPermissionAndRequest.requestPermission(
                (MifosBaseActivity) getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE,
                getResources().getString(
                        R.string.dialog_message_write_external_storage_permission_denied),
                getResources().getString(R.string.dialog_message_permission_never_ask_again_write),
                Constants.WRITE_EXTERNAL_STORAGE_STATUS);
    }

    /**
     * This Method getting the Response after User Grant or denied the Permission
     *
     * @param requestCode  Request Code
     * @param permissions  Permission
     * @param grantResults GrantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    checkExternalStorageAndCreateDocument();

                } else {

                    // permission denied, boo! Disable the
                    Toaster.show(rootView, getResources()
                            .getString(R.string.permission_denied_to_read_external_document));
                }
            }
        }
    }


    @Override
    public void showDocumentList(final List<Document> documents) {
        mDocumentList = documents;
        mDocumentListAdapter.setDocuments(mDocumentList);
    }

    @Override
    public void showDocumentSuccessfully(ResponseBody responseBody) {
        documentBody = responseBody;
        checkPermissionAndRequest();
    }

    @Override
    public void showDocumentPopUpMenu(final int documentId) {
        PopupMenu popup =
                new PopupMenu(getContext(), getActivity().findViewById(R.id.rv_documents));
        popup.getMenuInflater().inflate(R.menu.document_options, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.document_download:
                        mDocumentListPresenter.downloadDocument(entityType, entityId, documentId);
                        break;

                    case R.id.document_delete:

                        break;

                    default:
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void checkExternalStorageAndCreateDocument() {
        // Create a path where we will place our documents in the user's
        // public pictures directory and check if the file exists.  If
        // external storage is not currently mounted this will think the
        // picture doesn't exist.
        File mifosDirectory = new File(Environment.getExternalStorageDirectory(),
                getResources().getString(R.string.document_directory));
        if (!mifosDirectory.exists()) {
            mifosDirectory.mkdirs();
        }

        try {
            File documentFile = new File(mifosDirectory.getPath(), document.getName());
            OutputStream output = new FileOutputStream(documentFile);
            try {
                try {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;

                    while ((read = documentBody.byteStream().read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                } finally {
                    output.close();
                }
            } catch (Exception e) {
                Log.d(LOG_TAG, e.getLocalizedMessage()); // handle exception, define IOException
                // and others
            }
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG, e.getLocalizedMessage());
        } finally {
            documentBody.close();
        }

        //Opening the Saved Document
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.parse(mifosDirectory.getPath()), document.getType());
        startActivity(Intent.createChooser(intent,
                getResources().getString(R.string.open_document)));
    }

    @Override
    public void showEmptyDocuments() {
        ll_error.setVisibility(View.VISIBLE);
        mNoChargesText.setText(getResources().getString(R.string.no_document_to_show));
        mNoChargesIcon.setImageResource(R.drawable.ic_assignment_turned_in_black_24dp);
    }

    @Override
    public void showFetchingError(int message) {
        if (mDocumentListAdapter.getItemCount() == 0) {
            ll_error.setVisibility(View.VISIBLE);
            String errorMessage = getStringMessage(message) + getStringMessage(R.string.new_line) +
                    getStringMessage(R.string.click_to_refresh);
            mNoChargesText.setText(errorMessage);
        } else {
            Toaster.show(rootView, getStringMessage(message));
        }

    }

    @Override
    public void showProgressbar(boolean show) {
        swipeRefreshLayout.setRefreshing(show);
        if (show && mDocumentListAdapter.getItemCount() == 0) {
            showMifosProgressBar();
            swipeRefreshLayout.setRefreshing(false);
        } else {
            hideMifosProgressBar();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDocumentListPresenter.detachView();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuItem menuItemAddNewDocument = menu.add(Menu.NONE, MENU_ITEM_ADD_NEW_DOCUMENT, Menu
                .NONE, getString(R.string.add_new));
        menuItemAddNewDocument
                .setIcon(ContextCompat
                        .getDrawable(getActivity(), R.drawable.ic_action_content_new));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            menuItemAddNewDocument.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == MENU_ITEM_ADD_NEW_DOCUMENT) {
            DocumentDialogFragment documentDialogFragment = DocumentDialogFragment.newInstance
                    (entityType, entityId);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.addToBackStack(FragmentConstants.FRAG_DOCUMENT_LIST);
            documentDialogFragment.show(fragmentTransaction, "Document Dialog Fragment");
        }
        return super.onOptionsItemSelected(item);
    }
}
