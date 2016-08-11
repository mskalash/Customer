package com.customer;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Максим on 08.08.2016.
 */
public class InfoFragment extends Fragment {
    View view;
    FancyButton map;
    ImageView play;
    TextView lastname;
    TextView name;
    TextView description;
    CircularImageView avatar;
    boolean playrec=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_info, container, false);
        map=(FancyButton)view.findViewById(R.id.map);
        play=(ImageView)view.findViewById(R.id.plaer);
        lastname=(TextView) view.findViewById(R.id.lastname);
        name=(TextView)view.findViewById(R.id.name);
        description=(TextView)view.findViewById(R.id.description);
        avatar=(CircularImageView)view.findViewById(R.id.profile_avatar);
        if (((MainActivity) getActivity()).getClient().getImagename()!=null){
            Uri image=Uri.parse(((MainActivity) getActivity()).getClient().getImagename());

        Glide.with(getActivity())
                .load(image)
                .into(avatar);}
        lastname.setText(((MainActivity) getActivity()).getClient().getLast());
        name.setText(((MainActivity) getActivity()).getClient().getProfilename());
        description.setText(((MainActivity) getActivity()).getClient().getDesc());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new FirstFragment()).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
