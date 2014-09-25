package com.intel.android.yamba;


import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.intel.android.yamba.StatusContract.StatusColumns.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatusDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class StatusDetailFragment extends Fragment {

    public static final String ARG_CONTENT = "content_uri";

    private TextView mUserText, mMessageText, mCreatedAtText;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatusDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatusDetailFragment newInstance(Uri selectedItem) {
        StatusDetailFragment fragment = new StatusDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONTENT, selectedItem);
        fragment.setArguments(args);

        return fragment;
    }

    public StatusDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_status_detail, container, false);

        mUserText = (TextView) root.findViewById(R.id.text_user);
        mMessageText = (TextView) root.findViewById(R.id.text_message);
        mCreatedAtText = (TextView) root.findViewById(R.id.text_created_at);

        Uri content = getArguments().getParcelable(ARG_CONTENT);
        Cursor cursor = getActivity().getContentResolver().query(content, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String user = cursor.getString(cursor.getColumnIndex(USER));
                String message = cursor.getString(cursor.getColumnIndex(MESSAGE));
                long createdAt = cursor.getLong(cursor.getColumnIndex(CREATED_AT));
                CharSequence createdAtText = DateUtils.getRelativeTimeSpanString(createdAt,
                        System.currentTimeMillis(), 0);

                mUserText.setText(user);
                mMessageText.setText(message);
                mCreatedAtText.setText(createdAtText);
            }

            cursor.close();
        }

        return root;
    }


}
