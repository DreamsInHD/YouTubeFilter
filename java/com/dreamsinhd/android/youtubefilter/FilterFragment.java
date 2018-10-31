package com.dreamsinhd.android.youtubefilter;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dreamsinhd.android.youtubefilter.interfaces.BottomNavActivity;
import com.dreamsinhd.android.youtubefilter.interfaces.FilterActivity;
import com.dreamsinhd.android.youtubefilter.model.Filter;

import java.math.BigInteger;

public class FilterFragment extends Fragment {
    private static final String FILTER_EXTRA = "com.dreamsinhd.android.youtubefilter.FilterFragment.filter";

    private BackdropEditTextList backdropEditTextList;

    private TextInputEditText searchEditText;
    private TextInputEditText minViewsEditText;
    private TextInputEditText maxViewsEditText;
    private TextInputEditText minLikesEditText;
    private TextInputEditText maxLikesEditText;
    private TextInputEditText minDislikesEditText;
    private TextInputEditText maxDislikesEditText;

    private RadioGroup orderByRadioGroup;

    private FilterViewModel viewModel;

    public static FilterFragment newInstance(Filter filter) {
        FilterFragment fragment = new FilterFragment();

        Bundle args = new Bundle();
        args.putSerializable(FILTER_EXTRA, filter);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(FilterViewModel.class);

        Bundle args = getArguments();
        if(args != null) {
            viewModel.setFilter((Filter) args.getSerializable(FILTER_EXTRA));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter, container, false);

        searchEditText = v.findViewById(R.id.search_query_edit_text);
        minViewsEditText = v.findViewById(R.id.min_views_edit_text);
        maxViewsEditText = v.findViewById(R.id.max_views_edit_text);
        minLikesEditText = v.findViewById(R.id.min_likes_edit_text);
        maxLikesEditText = v.findViewById(R.id.max_likes_edit_text);
        minDislikesEditText = v.findViewById(R.id.min_dislikes_edit_text);
        maxDislikesEditText = v.findViewById(R.id.max_dislikes_edit_text);
        orderByRadioGroup = v.findViewById(R.id.order_by_radio_group);
        MaterialButton searchButton = v.findViewById(R.id.button_search);

        setFilterEditTextFields();

        backdropEditTextList = new BackdropEditTextList(getActivity(), minViewsEditText, maxViewsEditText, minLikesEditText, maxLikesEditText, minDislikesEditText, maxDislikesEditText);

        backdropEditTextList.addOnKeyListenerToEditTextViews((view, i, keyEvent) -> {
            TextInputEditText editText = (TextInputEditText) view;

            if (editText.getText() == null || editText.getText().toString().equals("") || isValidBigInt(editText.getText().toString())) {
                editText.setError(null);
            }
            return true;
        });
        searchButton.setOnClickListener(view -> {
            if (backdropEditTextList.validInput()) {
                setFilter(v);
                ((FilterActivity) getActivity()).setFilter(viewModel.getFilter());
                ((BottomNavActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.video_page_menu_item);
            }
        });

        return v;
    }

    private void setFilter(View v) {
        viewModel.setSearch(searchEditText.getText().toString());
        viewModel.setMinViews(nullOrBigInt(minViewsEditText.getText().toString()));
        viewModel.setMaxViews(nullOrBigInt(maxViewsEditText.getText().toString()));
        viewModel.setMinLikes(nullOrBigInt(minLikesEditText.getText().toString()));
        viewModel.setMaxLikes(nullOrBigInt(maxLikesEditText.getText().toString()));
        viewModel.setMinDislikes(nullOrBigInt(minDislikesEditText.getText().toString()));
        viewModel.setMaxDislikes(nullOrBigInt(maxDislikesEditText.getText().toString()));

        int id = orderByRadioGroup.getCheckedRadioButtonId();
        RadioButton selected = v.findViewById(id);
        viewModel.setOrderBy(selected != null ? selected.getText().toString() : null);
    }

    private boolean isValidBigInt(String bigIntString) {
        if (bigIntString != null) {
            try {
                if (new BigInteger(bigIntString).compareTo(BigInteger.valueOf(0)) < 0) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    private BigInteger nullOrBigInt(String bigIntString) {
        return bigIntString == null || bigIntString.equals("") ? null : new BigInteger(bigIntString);
    }

    private String bigIntToString(BigInteger bigInteger) {
        return bigInteger == null ? "" : bigInteger.toString();
    }

    private void setFilterEditTextFields() {
        searchEditText.setText(viewModel.getSearch());
        minViewsEditText.setText(bigIntToString(viewModel.getMinViews()));
        maxViewsEditText.setText(bigIntToString(viewModel.getMaxViews()));
        minLikesEditText.setText(bigIntToString(viewModel.getMinLikes()));
        maxLikesEditText.setText(bigIntToString(viewModel.getMaxLikes()));
        minDislikesEditText.setText(bigIntToString(viewModel.getMinDislikes()));
        maxDislikesEditText.setText(bigIntToString(viewModel.getMaxDislikes()));
    }
}
