/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package com.mifos.mifosxdroid.online.documentlistfragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mifos.api.DataManager;
import com.mifos.mifosxdroid.R;
import com.mifos.mifosxdroid.adapters.DocumentListAdapter;
import com.mifos.mifosxdroid.core.MifosBaseFragment;
import com.mifos.mifosxdroid.core.util.Toaster;
import com.mifos.mifosxdroid.dialogfragments.documentdialogfragment.DocumentDialogFragment;
import com.mifos.objects.noncore.Document;
import com.mifos.utils.AsyncFileDownloader;
import com.mifos.utils.Constants;
import com.mifos.utils.FragmentConstants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DocumentListFragment extends MifosBaseFragment implements DocumentListMvpView{

    public static final int MENU_ITEM_ADD_NEW_DOCUMENT = 1000;

    @InjectView(R.id.lv_documents)
    ListView lv_documents;

    private View rootView;
    private String entityType;
    private int entityId;
    private DataManager dataManager;
    private DocumentListPresenter mDocumentListPresenter;

    public static DocumentListFragment newInstance(String entityType, int entiyId) {
        DocumentListFragment fragment = new DocumentListFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ENTITY_TYPE, entityType);
        args.putInt(Constants.ENTITY_ID, entiyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entityType = getArguments().getString(Constants.ENTITY_TYPE);
            entityId = getArguments().getInt(Constants.ENTITY_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_document_list, container, false);
        ButterKnife.inject(this, rootView);
        dataManager = new DataManager();
        mDocumentListPresenter = new DocumentListPresenter(dataManager);
        mDocumentListPresenter.attachView(this);
        inflateDocumentList();
        return rootView;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuItem menuItemAddNewDocument = menu.add(Menu.NONE, MENU_ITEM_ADD_NEW_DOCUMENT, Menu.NONE, getString(R.string.add_new));
        menuItemAddNewDocument.setIcon(getResources().getDrawable(R.drawable.ic_action_content_new));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            menuItemAddNewDocument.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == MENU_ITEM_ADD_NEW_DOCUMENT) {
            DocumentDialogFragment documentDialogFragment = DocumentDialogFragment.newInstance(entityType, entityId);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(FragmentConstants.FRAG_DOCUMENT_LIST);
            documentDialogFragment.show(fragmentTransaction, "Document Dialog Fragment");
        }

        return super.onOptionsItemSelected(item);
    }

    public void inflateDocumentList() {
        showProgress();
        mDocumentListPresenter.loadDocumentList(entityType,entityId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDocumentListPresenter.detachView();
    }

    @Override
    public void showDocumentList(final List<Document> documents) {
        hideProgress();
        if (documents != null) {
            for (Document document : documents) {
                Log.w(document.getFileName(), document.getSize() + " bytes");
            }

            DocumentListAdapter documentListAdapter = new DocumentListAdapter(getActivity(), documents);
            lv_documents.setAdapter(documentListAdapter);
            lv_documents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    AsyncFileDownloader asyncFileDownloader = new AsyncFileDownloader(getActivity(), documents.get(i).getFileName());
                    asyncFileDownloader.execute(entityType, String.valueOf(entityId), String.valueOf(documents.get(i).getId()));
                }
            });
        }
    }

    @Override
    public void ResponseErrorDocumentList(String s) {
        hideProgress();
        Toaster.show(rootView,s);
    }
}
