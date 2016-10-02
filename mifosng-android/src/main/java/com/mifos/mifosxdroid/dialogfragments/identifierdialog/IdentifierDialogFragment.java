package com.mifos.mifosxdroid.dialogfragments.identifierdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mifos.mifosxdroid.R;
import com.mifos.mifosxdroid.core.MifosBaseActivity;
import com.mifos.objects.noncore.IdentifierTemplate;
import com.mifos.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rajan Maurya on 01/10/16.
 */

public class IdentifierDialogFragment extends DialogFragment implements
        IdentifierDialogMvpView, AdapterView.OnItemSelectedListener {

    @BindView(R.id.sp_identifier_type)
    Spinner sp_identifier_type;

    @BindView(R.id.sp_identifier_status)
    Spinner sp_identifier_status;

    @BindView(R.id.et_description)
    EditText et_description;

    @BindView(R.id.et_unique_id)
    EditText et_unique_id;

    @BindView(R.id.btn_create_identifier)
    Button btn_create_identifier;

    @BindArray(R.array.status)
    String [] identifierStatus;

    @Inject
    IdentifierDialogPresenter mIdentifierDialogPresenter;

    View rootView;
    private int clientId;

    List<String> mListIdentifierType = new ArrayList<>();

    ArrayAdapter<String> mIdentifierTypeAdapter;
    ArrayAdapter<String> mIdentifierStatusAdapter;

    public static IdentifierDialogFragment newInstance(int clientId) {
        IdentifierDialogFragment documentDialogFragment = new IdentifierDialogFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.CLIENT_ID, clientId);
        documentDialogFragment.setArguments(args);
        return documentDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            clientId = getArguments().getInt(Constants.CLIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_fragment_identifier, container, false);

        ButterKnife.bind(this, rootView);
        mIdentifierDialogPresenter.attachView(this);

        showIdentifierSpinners();

        mIdentifierDialogPresenter.loadClientIdentifierTemplate(clientId);

        return rootView;
    }


    @Override
    public void showIdentifierSpinners() {

        mIdentifierTypeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, mListIdentifierType);
        mIdentifierTypeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_identifier_type.setAdapter(mIdentifierTypeAdapter);
        sp_identifier_type.setOnItemSelectedListener(this);

        mIdentifierStatusAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, identifierStatus);
        mIdentifierStatusAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_identifier_status.setAdapter(mIdentifierStatusAdapter);
        sp_identifier_status.setOnItemSelectedListener(this);

    }

    @Override
    public void showClientIdentifierTemplate(IdentifierTemplate identifierTemplate) {

        mListIdentifierType.addAll(mIdentifierDialogPresenter.getIdentifierDocumentTypeNames
                (identifierTemplate.getAllowedDocumentTypes()));
        mIdentifierTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void showIdentifierCreatedSuccessfully() {

    }

    @Override
    public void showError(int errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressbar(boolean b) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIdentifierDialogPresenter.detachView();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_identifier_type:

                break;
            case R.id.sp_identifier_status:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
