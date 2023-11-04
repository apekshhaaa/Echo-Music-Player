package com.project.musicplayerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // Key for storing the state of 'Shake to change song' feature in SharedPreferences
    private static final String SHARED_PREF_KEY = "SHAKE_TO_CHANGE_SONG";


    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Settings");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Find the Switch in the layout
        Switch shakeSwitch = view.findViewById(R.id.shake_switch);

        // Set the initial state of the Switch based on the preference
        shakeSwitch.setChecked(getShakeToChangeSongPreference(requireContext()));

        // Set an OnCheckedChangeListener for the Switch
        shakeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the preference when the Switch state changes
                setShakeToChangeSongPreference(requireContext(), isChecked);
            }
        });
        return view;
       // return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    // Helper method to get the current state of 'Shake to change song' feature
    private boolean getShakeToChangeSongPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHARED_PREF_KEY, false);
    }
    // Helper method to set the state of 'Shake to change song' feature
    private void setShakeToChangeSongPreference(Context context, boolean isEnabled) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREF_KEY, isEnabled);
        editor.apply();
    }
}