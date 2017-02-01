package com.mifos.mifosxdroid.online.attacheddatatable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mifos.mifosxdroid.R;
import com.mifos.mifosxdroid.adapters.FragmentAdapter;
import com.mifos.mifosxdroid.core.MifosBaseActivity;
import com.mifos.mifosxdroid.core.MifosBaseFragment;
import com.mifos.objects.noncore.DataTable;
import com.mifos.services.data.GroupLoanPayload;
import com.mifos.services.data.LoansPayload;
import com.mifos.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rajan Maurya on 31/01/17.
 */

public class DataTableViewPager extends MifosBaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.vp_data_table)
    ViewPager vpDataTable;

    @BindView(R.id.tl_data_table_name)
    TabLayout tlDataTableName;

    View rootView;

    private FragmentAdapter fragmentAdapter;

    private ArrayList<DataTable> dataTables;
    private LoansPayload clientLoansPayload;
    private GroupLoanPayload groupLoanPayload;
    private int requestType;

    public static DataTableViewPager newInstance(ArrayList<DataTable> dataTables,
                                                 Object payload, int type) {
        DataTableViewPager dataTableViewPager = new DataTableViewPager();
        Bundle args = new Bundle();
        dataTableViewPager.dataTables = dataTables;
        dataTableViewPager.requestType = type;
        switch (type) {
            case Constants.CLIENT_LOAN:
                dataTableViewPager.clientLoansPayload = (LoansPayload) payload;
                break;
            case Constants.GROUP_LOAN:
                dataTableViewPager.groupLoanPayload = (GroupLoanPayload) payload;
                break;
        }
        dataTableViewPager.setArguments(args);
        return dataTableViewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_data_table_viewpager, container, false);
        ButterKnife.bind(this, rootView);

        fragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        for (int i = 0; i<dataTables.size(); ++i) {
            fragmentAdapter.addFragment(DataTableListFragment.newInstance(dataTables,
                    clientLoansPayload, Constants.CLIENT_LOAN),
                    dataTables.get(i).getRegisteredTableName());
        }

        vpDataTable.setAdapter(fragmentAdapter);
        tlDataTableName.setupWithViewPager(vpDataTable);

        return rootView;
    }
}
