package ru.tp.buy_places.authentication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.tp.buy_places.R;

/**
 * Created by Ivan on 22.07.2015.
 */
public class LoginFragment extends Fragment {
    private static final int VK_ACTIVITY_REQUEST_CODE = 0;
    private Button mLoginViaVKButton;
    private OnLoginListener mListener;

    public static Fragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case VK_ACTIVITY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String code = data.getStringExtra("EXTRA_CODE");
                    if (code != null) {
                        mListener.onLoginViaVkButtonClickListener(code);
                    }
                }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginViaVKButton = (Button) rootView.findViewById(R.id.button_login_via_vk);
        mLoginViaVKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKOAuthActivity.startForResult(LoginFragment.this, VK_ACTIVITY_REQUEST_CODE);
            }
        });
        return rootView;
    }

    interface OnLoginListener {
        void onLoginViaVkButtonClickListener(String code);
        void onLoginClickListener(String username, String password);
    }
}
