package com.customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Максим on 08.08.2016.
 */
public class InfoFragment extends Fragment {
    View view;
    FancyButton map;
    ImageView play;
    boolean playrec=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

                super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_info, container, false);
        map=(FancyButton)view.findViewById(R.id.map);
        play=(ImageView)view.findViewById(R.id.plaer);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new FragmentMap()).commit();

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playrec==false){
                    play.setImageResource(R.drawable.pause);
                    playrec=true;
                    return;
                }
                else {
                    play.setImageResource(R.drawable.play);
                    playrec=false;
                }
            }
        });

        return view; }
}
